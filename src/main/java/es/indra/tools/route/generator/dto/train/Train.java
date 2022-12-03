package es.indra.tools.route.generator.dto.train;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Train {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String number;
  
  private String lineId;
  
  private String initialNodeId;
  
  private String finalNodeId;
  
  @OneToMany(cascade = CascadeType.ALL)
  private List<RouteNode> nodes;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public List<RouteNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<RouteNode> nodes) {
    this.nodes = nodes;
  }

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
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

  @Override
  public String toString() {
    return "Train [id=" + id + ", number=" + number + ", lineId=" + lineId + ", initialNodeId=" + initialNodeId
        + ", finalNodeId=" + finalNodeId + ", nodes=" + nodes + "]";
  }

}
