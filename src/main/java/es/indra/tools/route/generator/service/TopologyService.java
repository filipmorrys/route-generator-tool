package es.indra.tools.route.generator.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.indra.tools.route.generator.dao.TopologyDao;
import es.indra.tools.route.generator.dto.topology.Arc;
import es.indra.tools.route.generator.dto.topology.Line;
import es.indra.tools.route.generator.dto.topology.Movement;
import es.indra.tools.route.generator.dto.topology.Node;

@Service
public class TopologyService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TopologyService.class);
  
  @Autowired
  private TopologyDao dao;

  /**
   * Devuelve el grupo de topologia actual
   * @return
   */
  public String getCurrengGroupId() {
    return dao.getCurrengGroupId();
  }
  
  /**
   * Devuelve las lineas de un grupo de topología
   * @param groupIdParam
   * @return
   */
  public List<Line> getLines(String groupIdParam) {
    String groupId = groupIdParam;
    if (groupIdParam == null) {
      groupId = dao.getCurrengGroupId();
    }
    return dao.getLines(groupId);
  }

  
  /**
   * Devuelve la lista de nodos de una línea y un grupo de topología
   * por en el orden de la linea
   * 
   * @param groupId
   * @param lineId
   * @return
   */
  public List<Node> getNodesByLine(String groupId, String lineId) {
    List<Node> sortedNodes = new LinkedList<>();
    
    Map<String, Arc> arcNodeIds = dao.getArcsFromLine(groupId, lineId);
    Map<String, Node> nodesById = dao.getNodesFromLine(groupId, lineId);
    Line line = dao.getLine(groupId, lineId);
    
    String nextNodeId = line.getInitialNodeId(); 
    while (true) {
      LOGGER.info("Siguiente nodo: {}", nextNodeId);
      
      Node nextNode = nodesById.get(nextNodeId);
      sortedNodes.add(nextNode);
      
      if (nextNode.getId().equals(line.getFinalNodeId())) {
        break;
      } else {
        Arc arc = arcNodeIds.get(nextNode.getId());
        nextNode.setArc(arc);
        nextNodeId = arc.getFinalNodeId();
      }

    } 
    
    return sortedNodes;
  }
  
  /** 
   * Devuelve los movimientos pertenecientes a una linea indexados por 
   * una clave con el formato <mov_type;st_track;circ_track_id>
   * @param groupId
   * @param lineId
   * @return
   */
  public Map<String, Movement> getMovementsByLine(String groupId, String lineId) {
    return dao.getMovementsByLine(groupId, lineId);
  }

}
