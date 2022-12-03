package es.indra.tools.route.generator.dto.train;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import es.indra.tools.route.generator.dto.topology.Movement;

@Entity
public class RouteNode {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String nodeId;
  
  private String name;
  
  private String stTrackId;
  
  private String circTrackId;
  
  @Transient
  private Movement inputMov;
  
  @Transient
  private Movement outputMov;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Movement getInputMov() {
    return inputMov;
  }

  public void setInputMov(Movement inputMov) {
    this.inputMov = inputMov;
  }

  public Movement getOutputMov() {
    return outputMov;
  }

  public void setOutputMov(Movement outputMov) {
    this.outputMov = outputMov;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return "RouteNode [id=" + id + ", nodeId=" + nodeId + ", name=" + name + ", stTrackId=" + stTrackId
        + ", circTrackId=" + circTrackId + ", inputMov=" + inputMov + ", outputMov=" + outputMov + "]";
  }
  
}
