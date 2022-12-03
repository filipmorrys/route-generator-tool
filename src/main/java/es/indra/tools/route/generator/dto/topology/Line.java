package es.indra.tools.route.generator.dto.topology;

public class Line {
  
  private String id;
  
  private String description;
  
  private String initialNodeId;
  
  private String finalNodeId;
  
  public Line() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getInitialNodeId() {
    return initialNodeId;
  }

  public void setInitialNodeId(String initialNodeId) {
    this.initialNodeId = initialNodeId;
  }

  public String getFinalNodeId() {
    return finalNodeId;
  }

  public void setFinalNodeId(String finalNodeId) {
    this.finalNodeId = finalNodeId;
  }

}
