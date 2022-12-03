package es.indra.tools.route.generator.dto.topology;

public class Track {

  private String id;
  
  private String name;
  
  private boolean principal;
  
  private String parity;

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

  public boolean isPrincipal() {
    return principal;
  }

  public void setPrincipal(boolean principal) {
    this.principal = principal;
  }

  public String getParity() {
    return parity;
  }

  public void setParity(String parity) {
    this.parity = parity;
  }
  
}
