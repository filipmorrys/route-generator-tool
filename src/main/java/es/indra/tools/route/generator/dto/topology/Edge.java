package es.indra.tools.route.generator.dto.topology;

public class Edge {
  
  private String id;
  
  private Integer initPos;
  
  private Integer endPos;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getInitPos() {
    return initPos;
  }

  public void setInitPos(Integer initPos) {
    this.initPos = initPos;
  }

  public Integer getEndPos() {
    return endPos;
  }

  public void setEndPos(Integer endPos) {
    this.endPos = endPos;
  }

  
}
