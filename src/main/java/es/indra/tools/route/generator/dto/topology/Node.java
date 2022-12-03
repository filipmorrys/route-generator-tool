package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;

public class Node {
  
  private String id;
  
  private String name;
  
  private List<Tcz> tczs;
  
  private Arc arc;

  public Node() {
    this.tczs = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Tcz> getTczs() {
    return tczs;
  }

  public void setTczs(List<Tcz> tczs) {
    this.tczs = tczs;
  }

  public void addTcz(Tcz tcz) {
    this.tczs.add(tcz);
  }

  public Arc getArc() {
    return arc;
  }

  public void setArc(Arc arc) {
    this.arc = arc;
  }
  

}
