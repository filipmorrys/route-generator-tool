package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;

public class NodeGraph {
  
  private String id;
  
  private String name;
  
  private List<ArcGraph> arcs = new LinkedList<>();
  
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

  public List<ArcGraph> getArcs() {
    return arcs;
  }

  public void setArcs(List<ArcGraph> arcs) {
    this.arcs = arcs;
  }
  
  public void addArc(ArcGraph arc) {
    this.arcs.add(arc);
  }

}
