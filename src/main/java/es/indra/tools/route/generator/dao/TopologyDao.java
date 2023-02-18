package es.indra.tools.route.generator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.indra.tools.route.generator.dto.topology.Arc;
import es.indra.tools.route.generator.dto.topology.ArcGraph;
import es.indra.tools.route.generator.dto.topology.CirculationTrack;
import es.indra.tools.route.generator.dto.topology.Edge;
import es.indra.tools.route.generator.dto.topology.GeoNode;
import es.indra.tools.route.generator.dto.topology.Line;
import es.indra.tools.route.generator.dto.topology.Movement;
import es.indra.tools.route.generator.dto.topology.Node;
import es.indra.tools.route.generator.dto.topology.NodeGraph;
import es.indra.tools.route.generator.dto.topology.Tcz;
import es.indra.tools.route.generator.dto.topology.Track;
import es.indra.tools.route.generator.exception.DataAccessException;

@Component
public class TopologyDao {
  
 
  @Autowired
  @Qualifier("topologyDataSource")
  private DataSource dataSource;
  
  private static final String QUERY_CURRENT_GROUP_ID =
      "select id "
      + "from tp_groups tg, "
      + "  (select max(validity) as validity "
      + "  from tp_groups tg "
      + "  where validity <= TO_CHAR(NOW()::date, 'yyyymmdd')) mv "
      + "where tg.validity = mv.validity";
  
  private static final String QUERY_LINES =
      "select tl.line_id, tl.description, tl.node_id_a, tl.node_id_b "
      + "from tp_lines tl "
      + "where tl.group_id = ? ";
  
  private static final String QUERY_LINE =
      "select tl.line_id, tl.description, tl.node_id_a, tl.node_id_b "
      + "from tp_lines tl "
      + "where tl.group_id = ? "
      + "and tl.line_id = ? ";
  
  private static final String QUERY_ARCS_FROM_LINE = 
      "select tct.mnemonic as track_id, tct.name as track_name, tct.node_id_a, tct.node_id_b, "
      + "  tct.line_id, tct.is_principal, ta.uuid as arc_uuid, "
      + "  tct.parity_type as track_parity_type, ta.parity_type "
      + "from tp_circulation_tracks tct, "
      + "  tp_arcs ta "
      + "where ta.node_id_a = tct.node_id_a "
      + "  and ta.node_id_b = tct.node_id_b "
      + "  and ta.line_id = tct.line_id "
      + "  and ta.line_id = ? "
      + "  and ta.group_id = ? "
      + "  and tct.group_id = ? "
      + "order by ta.uuid, track_name ";
  
  private static final String QUERY_NODES_FROM_LINE =
      "select tn.id as node_id, tn.name as node_name, tz.id as tcz_id, "
      + "  tz.name as tcz_name, tes.id as track_id, tes.name as track_name, "
      + "  tes.is_principal, te.parity  "
      + "from tp_nodes tn, "
      + "  tp_zcs tz, "
      + "  tp_edges_stops tes,"
      + "  tp_edges te, "
      + "  (select distinct tn.id "
      + "  from tp_nodes tn, "
      + "    tp_arcs ta "
      + "  where (tn.id = ta.node_id_a "
      + "    or tn.id = ta.node_id_b) "
      + "    and ta.line_id  = ? "
      + "    and ta.group_id = ? "
      + "    and tn.group_id = ?) ti "
      + "where ti.id = tn.id"
      + "  and te.id = tes.edge_id  "
      + "  and tz.node_id = tn.id "
      + "  and tz.id = tes.zc_id "
      + "  and tz.group_id = ? "
      + "  and tn.group_id = ? "
      + "  and tes.group_id = ? "
      + "order by node_id, tcz_id, track_id";
  
  private static final String QUERY_MOVEMENTS_BY_LINE = 
      "select tm.mov_id, tm.sct_mnemo, tm.cct_mnemo, tm.mov_type, tm.power_types, "
      + "  tm.wide_type, tm.train_usage, tme.edge_id, tme.tc_order, tme.str_pos, tme.end_pos  "
      + "from tp_mov tm, "
      + "  tp_circulation_tracks tct, "
      + "  tp_mov_edges tme "
      + "where tm.cct_mnemo = tct.mnemonic "
      + "  and tm.mov_id = tme.mov_id "
      + "  and tct.line_id = ? "
      + "  and tct.group_id = ? "
      + "  and tm.group_id = ? "
      + "  and tme.group_id = ? "
      + "order by tm.mov_id, tme.tc_order";
  
  
  private static final String QUERY_GEO_NODES = 
      "select id, name, longitude, latitude, priority_level "
      + "from tp_nodes tn "
      + "where group_id = ? "
      + "  and latitude > 0 ";
  
  private static final String QUERY_NODES_GRAPH = 
      "select id, name "
      + "from tp_nodes tn "
      + "where tn.group_id = ? ";
  
  private static final String QUERY_ARCS_GRAPH = 
      "select line_id, distance_even, node_id_a, node_id_b  "
      + "from tp_arcs ta "
      + "where ta.group_id = ? ";
  
  /**
   * Devuelve el grupo de topología correspondiente a la fecha actual
   * @return
   */
  public String getCurrengGroupId() {
    String groupId = null;
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_CURRENT_GROUP_ID);
      rs = stm.executeQuery();
      
      if (rs.next()) {
        groupId = rs.getString("id");
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo el groupId", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }

    }
    
    return groupId;
  }
  
  /**
   * Devuelve todas las lineas pertenecientes al grupo de topología
   * @param groupId
   * @return
   */
  public List<Line> getLines(String groupId) {
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    List<Line> lines = new LinkedList<>();
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_LINES);
      stm.setString(1, groupId);
      rs = stm.executeQuery();
      
      while (rs.next()) {
        Line line = new Line();
        line.setId(rs.getString("line_id"));
        line.setDescription(rs.getString("description"));
        line.setInitialNodeId(rs.getString("node_id_a"));
        line.setFinalNodeId(rs.getString("node_id_b"));
        
        lines.add(line);
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo líneas", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }
    }
    
    return lines;
  }
  
  /**
   * Devuelve una línea por id y grupo de topología
   * @param groupId
   * @param lineId
   * @return
   */
  public Line getLine(String groupId, String lineId) {
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    Line line = new Line();

    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_LINE);
      stm.setString(1, groupId);
      stm.setString(2, lineId);
      rs = stm.executeQuery();
      
      if (rs.next()) {
        line.setId(rs.getString("line_id"));
        line.setDescription(rs.getString("description"));
        line.setInitialNodeId(rs.getString("node_id_a"));
        line.setFinalNodeId(rs.getString("node_id_b"));
       
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo línea", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }
    }
    
    return line;
  }
  
  /**
   * Devuelve un mapa de todos los arcos de una linea y un gropo de topología
   * indexados por el id de arco
   * @param groupId
   * @param lineId
   * @return
   */
  public Map<String, Arc> getArcsFromLine(String groupId, String lineId) {
    Map<String, Arc> arcsFromLine = new HashMap<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_ARCS_FROM_LINE);
      stm.setString(1, lineId);
      stm.setString(2, groupId);
      stm.setString(3, groupId);
      rs = stm.executeQuery();
      
      Arc arc = null;
      while (rs.next()) {
        String arcId = rs.getString("arc_uuid");
        if (arc == null || !arcId.equals(arc.getId())) {
          arc = new Arc();
          arc.setId(arcId);
          arc.setInitialNodeId(rs.getString("node_id_a"));
          arc.setFinalNodeId(rs.getString("node_id_b"));
          arc.setParityType(rs.getString("parity_type"));
          
          arcsFromLine.put(arc.getInitialNodeId(), arc);
        }
        CirculationTrack track = new CirculationTrack();
        track.setId(rs.getString("track_id"));
        track.setName(rs.getString("track_name"));
        int principal = rs.getInt("is_principal");
        track.setPrincipal(principal == 1);
        track.setParityType(rs.getString("track_parity_type"));
        
        arc.addCirculationTrack(track);
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo arcos", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }

    }

    return arcsFromLine;
  }


  /**
   * Devuelve un mapa con todos los nodos pertenecientes a una linea indexado
   * por id del nodo
   * @param groupId
   * @param lineId
   * @return
   */
  public Map<String, Node> getNodesFromLine(String groupId, String lineId) {
    Map<String, Node> nodes = new HashMap<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_NODES_FROM_LINE);
      stm.setString(1, lineId);
      stm.setString(2, groupId);
      stm.setString(3, groupId);
      stm.setString(4, groupId);
      stm.setString(5, groupId);
      stm.setString(6, groupId);
      rs = stm.executeQuery();

      Node node = null;
      while (rs.next()) {
        // Información del nodo
        String nodeId = rs.getString("node_id");
        if (node == null || !node.getId().equals(nodeId)) {
          node = new Node();
          node.setId(nodeId);
          node.setName(rs.getString("node_name"));
          nodes.put(node.getId(), node);
        }
        
        // Información del tcz
        String tczId = rs.getString("tcz_id");
        Tcz tcz = node.getTczs().stream()
          .filter(t -> t.getId().equals(tczId))
          .findFirst()
          .orElse(null);
        
        if (tcz == null) {
          tcz = new Tcz();
          tcz.setId(tczId);
          tcz.setName(rs.getString("tcz_name"));
          node.addTcz(tcz);
        }

        Track track = new Track();
        track.setId(rs.getString("track_id"));
        track.setName(rs.getString("track_name"));
        int principal = rs.getInt("is_principal");
        track.setPrincipal(principal == 1);
        track.setParity(rs.getString("parity"));
        
        tcz.addTrack(track);
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo nodos", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }

    }

    return nodes;
  }

  /** 
   * Devuelve los movimientos pertenecientes a una linea indexados por 
   * una clave con el formato <mov_type;st_track;circ_track_id>
   * @param groupId
   * @param lineId
   * @return
   */
  public Map<String, Movement> getMovementsByLine(String groupId, String lineId) {
    Map<String, Movement> movements = new HashMap<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_MOVEMENTS_BY_LINE);
      stm.setString(1, lineId);
      stm.setString(2, groupId);
      stm.setString(3, groupId);
      stm.setString(4, groupId);
      rs = stm.executeQuery();

      Movement mov = null;
      while (rs.next()) {
        String movId = rs.getString("mov_id");
        if (mov == null || !movId.equals(mov.getId())) {
          mov = new Movement();
          mov.setId(movId);
          mov.setCircTrackId(rs.getString("cct_mnemo"));
          mov.setStTrackId(rs.getString("sct_mnemo"));
          mov.setMovType(rs.getString("mov_type"));
          mov.setPowerTypes(rs.getString("power_types"));
          mov.setWideTypes(rs.getString("wide_type"));
          mov.setTrainUsage(rs.getString("train_usage"));
          
          movements.put(getMovementKey(mov), mov);
        }
        
        Edge edge = new Edge();
        edge.setId(rs.getString("edge_id"));
        edge.setInitPos(rs.getInt("str_pos"));
        edge.setEndPos(rs.getInt("end_pos"));
        
        mov.addEdge(edge);
        
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo movimientos", e);
    } finally {
      try { conn.close(); } catch (SQLException e) { }
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }

    }    
    return movements;
  }

  private String getMovementKey(Movement mov) {
    return mov.getMovType().concat(";")
        .concat(mov.getStTrackId()).concat(";")
        .concat(mov.getCircTrackId());
  }
  
  public Map<String, GeoNode> getGeoNodes(String groupId) {
    Map<String, GeoNode> nodes = new HashMap<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_GEO_NODES);
      stm.setString(1, groupId);
      rs = stm.executeQuery();

      while (rs.next()) {
        GeoNode node = new GeoNode();
        node.setId(rs.getString("id"));
        node.setName(rs.getString("name"));
        node.setLongitude(rs.getDouble("longitude"));
        node.setLatitude(rs.getDouble("latitude"));
        node.setPriorityLevel(rs.getInt("priority_level"));

        nodes.put(node.getId(), node);
        
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo nodos", e);
    } finally {
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }
      try { conn.close(); } catch (SQLException e) { }

    }    
    return nodes;
  }

  public Map<String, NodeGraph> getNodesGraph(String groupId) {
    Map<String, NodeGraph> nodes = new TreeMap<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_NODES_GRAPH);
      stm.setString(1, groupId);
      rs = stm.executeQuery();

      while (rs.next()) {
        NodeGraph node = new NodeGraph();
        node.setId(rs.getString("id"));
        node.setName(rs.getString("name"));

        nodes.put(node.getId(), node);
        
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo nodos del grafo", e);
    } finally {
      try { stm.close(); } catch (SQLException e) { }
      try { rs.close(); } catch (SQLException e) { }
      try { conn.close(); } catch (SQLException e) { }

    }    
    return nodes;
  }

  public List<ArcGraph> getArcsGraph(String groupId) {
    List<ArcGraph> arcs = new LinkedList<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      stm = conn.prepareStatement(QUERY_ARCS_GRAPH);
      stm.setString(1, groupId);
      rs = stm.executeQuery();

      while (rs.next()) {
        ArcGraph arc = new ArcGraph();
        arc.setLineId(rs.getString("line_id"));
        arc.setDistance(rs.getInt("distance_even"));
        arc.setNodeIdA(rs.getString("node_id_a"));
        arc.setNodeIdB(rs.getString("node_id_b"));

        arcs.add(arc);
        
      }
      
    } catch (SQLException e) {
      throw new DataAccessException("Error obteniendo nodos del grafo", e);
    } finally {
      try { conn.close(); } catch (Exception e) { }
      try { stm.close(); } catch (Exception e) { }
      try { rs.close(); } catch (Exception e) { }

    }    
    return arcs;
  }
  
}
