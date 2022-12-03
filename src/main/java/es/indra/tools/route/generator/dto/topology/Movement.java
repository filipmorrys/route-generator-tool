package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;

public class Movement {
  
  private String id;
  
  private String stTrackId;
  
  private String circTrackId;
  
  private String movType;
  
  private String powerTypes;
  
  private String wideTypes;
  
  private String trainUsage;
  
  private List<Edge> edges;
  
  public Movement() {
    this.edges = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStTrackId() {
    return stTrackId;
  }

  public void setStTrackId(String stTrackId) {
    this.stTrackId = stTrackId;
  }

  public String getCircTrackId() {
    return circTrackId;
  }

  public void setCircTrackId(String circTrackId) {
    this.circTrackId = circTrackId;
  }

  public String getMovType() {
    return movType;
  }

  public void setMovType(String movType) {
    this.movType = movType;
  }

  public String getPowerTypes() {
    return powerTypes;
  }

  public void setPowerTypes(String powerTypes) {
    this.powerTypes = powerTypes;
  }

  public String getWideTypes() {
    return wideTypes;
  }

  public void setWideTypes(String wideTypes) {
    this.wideTypes = wideTypes;
  }

  public String getTrainUsage() {
    return trainUsage;
  }

  public void setTrainUsage(String trainUsage) {
    this.trainUsage = trainUsage;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public void setEdges(List<Edge> edges) {
    this.edges = edges;
  }
  
  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }
  
}
