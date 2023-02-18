package es.indra.tools.route.generator.dto.topology;

public class ArcGraph {
  
  private String lineId;
  
  private Integer distance;
  
  private String nodeIdA;
  
  private String nodeIdB;
  
  private NodeGraph nodeA;

  private NodeGraph nodeB;

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }
  
  public String getNodeIdA() {
    return nodeIdA;
  }

  public void setNodeIdA(String nodeIdA) {
    this.nodeIdA = nodeIdA;
  }

  public String getNodeIdB() {
    return nodeIdB;
  }

  public void setNodeIdB(String nodeIdB) {
    this.nodeIdB = nodeIdB;
  }

  public NodeGraph getNodeA() {
    return nodeA;
  }

  public void setNodeA(NodeGraph nodeA) {
    this.nodeA = nodeA;
  }

  public NodeGraph getNodeB() {
    return nodeB;
  }

  public void setNodeB(NodeGraph nodeB) {
    this.nodeB = nodeB;
  }

  @Override
  public String toString() {
    return "ArcGraph [lineId=" + lineId + ", distance=" + distance + ", nodeIdA=" + nodeIdA + ", nodeIdB=" + nodeIdB
        + "]";
  }

}
