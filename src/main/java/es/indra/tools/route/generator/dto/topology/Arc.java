package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;

public class Arc {
  
  private String id;
  
  private String initialNodeId;
  
  private String finalNodeId;
  
  private String parityType;
  
  private List<CirculationTrack> circulationTracks;
  
  public Arc() {
    circulationTracks = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public List<CirculationTrack> getCirculationTracks() {
    return circulationTracks;
  }

  public void setCirculationTracks(List<CirculationTrack> circulationTracks) {
    this.circulationTracks = circulationTracks;
  }

  public void addCirculationTrack(CirculationTrack track) {
    this.circulationTracks.add(track);
  }

  public String getParityType() {
    return parityType;
  }

  public void setParityType(String parityType) {
    this.parityType = parityType;
  }
  
}
