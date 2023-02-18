package es.indra.tools.route.generator.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.indra.tools.route.generator.dao.TopologyDao;
import es.indra.tools.route.generator.dto.topology.Arc;
import es.indra.tools.route.generator.dto.topology.ArcGraph;
import es.indra.tools.route.generator.dto.topology.Distance;
import es.indra.tools.route.generator.dto.topology.GeoNode;
import es.indra.tools.route.generator.dto.topology.Line;
import es.indra.tools.route.generator.dto.topology.Movement;
import es.indra.tools.route.generator.dto.topology.Node;
import es.indra.tools.route.generator.dto.topology.NodeGraph;

@Service
public class TopologyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TopologyService.class);

  @Autowired
  private TopologyDao dao;

  private Map<String, NodeGraph> graph;

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
   * Devuelve la lista de nodos de una línea y un grupo de topología por en el orden de la linea
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
   * Devuelve los movimientos pertenecientes a una linea indexados por una clave con el formato
   * <mov_type;st_track;circ_track_id>
   * @param groupId
   * @param lineId
   * @return
   */
  public Map<String, Movement> getMovementsByLine(String groupId, String lineId) {
    return dao.getMovementsByLine(groupId, lineId);
  }

  public Map<String, GeoNode> getGeoNodes(String groupId) {
    return dao.getGeoNodes(groupId);
  }

  /**
   * Carga un grafo en memoria
   * @param groupId
   */
  public void loadGraph(String groupId) {
    graph = dao.getNodesGraph(groupId);
    List<ArcGraph> arcsGraph = dao.getArcsGraph(groupId);

    arcsGraph.stream().forEach(
        a -> join(graph.get(a.getNodeIdA()), graph.get(a.getNodeIdB()), a));
  }

  /**
   * Une los nodos y arcos del grafo
   * @param nodeA
   * @param nodeB
   * @param arc
   */
  private void join(NodeGraph nodeA, NodeGraph nodeB, ArcGraph arc) {
    arc.setNodeA(nodeA);
    arc.setNodeB(nodeB);
    nodeA.addArc(arc);
    nodeB.addArc(arc);
  }

  /**
   * Realiza la búsqueda del camino mas corto entre dos nodos. 
   * @param groupId
   * @param originNodeId
   * @param destinationNodeId
   * @return
   */
  public List<String> dijkstra(String groupId, String originNodeId, String destinationNodeId) {
    if (graph == null) {
      this.loadGraph(groupId);
    }

    // Nodos no marcados
    Map<String, NodeGraph> unmarkedNodes = new TreeMap<>(graph);
    List<NodeGraph> markedNodes = new LinkedList<>();
    // Distancia desde el origen a todos los nodos
    Map<String, Distance> distances = new TreeMap<>();

    // Nodo actual
    NodeGraph initialNode = graph.get(originNodeId);
    distances.put(originNodeId, new Distance(0, null));
    unmarkedNodes.remove(originNodeId);
    markedNodes.add(initialNode);

    dijkstraRec(markedNodes, unmarkedNodes, distances, destinationNodeId);
    
    return shortRoute(destinationNodeId, distances);

  }

  /**
   * Devuelve el recorrido mas corto desde el origen hasta el destino
   * @param destinationNodeId
   * @param distances
   * @return
   */
  private List<String> shortRoute(String destinationNodeId, Map<String, Distance> distances) {
    LinkedList<String> route = new LinkedList<>();
    route.addLast(destinationNodeId);
    Distance distance = distances.get(destinationNodeId);
    NodeGraph previous = distance.getPrevious();
    while (previous != null) {
      Distance nextDistance = distances.get(previous.getId());
      route.addLast(previous.getId());
      previous = nextDistance.getPrevious();
    }
    
    return route;
  }

  private void dijkstraRec(List<NodeGraph> markedNodes, Map<String, NodeGraph> unmarkedNodes,
    Map<String, Distance> distances, String destinationNodeId) {
    
    // Calculamos la distancia del nodo actual con todos los adyacentes
    Map<NodeGraph, Distance> evaluatedDistances = new HashMap<>();
    for (NodeGraph currNode : markedNodes) {
      Distance currNodeDistance = distances.get(currNode.getId());
      for (ArcGraph arc : currNode.getArcs()) {
        NodeGraph nextNode = (arc.getNodeIdA().equals(currNode.getId())) ? arc.getNodeB() : arc.getNodeA();
        if (unmarkedNodes.containsKey(nextNode.getId())) {
          // Las distancias se van almacenando en el mapa de distancias
          Integer d = arc.getDistance() + currNodeDistance.getDistance();
          Distance nextNodeDistance = distances.get(nextNode.getId());
          if (nextNodeDistance == null || nextNodeDistance.getDistance() > d) {
            nextNodeDistance = new Distance(d, currNode);
          }
          distances.put(nextNode.getId(), nextNodeDistance);         
          evaluatedDistances.put(nextNode, nextNodeDistance);
        }
      }
      
    }

    // De los nodos evaluados marcamos el de menor distancia
    NodeGraph minorDistanceNode = minorDistanceNode(evaluatedDistances);
    markedNodes.add(minorDistanceNode);
    unmarkedNodes.remove(minorDistanceNode.getId());
    
    
    if (!minorDistanceNode.getId().equals(destinationNodeId)) {
      dijkstraRec(markedNodes, unmarkedNodes, distances, destinationNodeId);
    }
  }

  /**
   * Devuelve el nodo de menor distancia de todos los evaluados
   * @param evaluatedDistances
   * @return
   */
  private NodeGraph minorDistanceNode(Map<NodeGraph, Distance> evaluatedDistances) {
    
    NodeGraph minorDistanceNode = null;
    Distance minorDistance = null;
    for (NodeGraph node: evaluatedDistances.keySet()) {
      Distance nodeDistance = evaluatedDistances.get(node);
      if(minorDistance == null || nodeDistance.getDistance() < minorDistance.getDistance()) {
        minorDistanceNode = node;
        minorDistance = nodeDistance;
      }
    }
    
    return minorDistanceNode;
  }

}
