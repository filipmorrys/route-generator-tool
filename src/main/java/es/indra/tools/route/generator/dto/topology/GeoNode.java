package es.indra.tools.route.generator.dto.topology;

public class GeoNode {
  
  private String id;
  
  private String name;
  
  private Double longitude;
  
  private Double latitude;
  
  private Integer priorityLevel;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getLongitude() {
    return longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Integer getPriorityLevel() {
    return priorityLevel;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public void setPriorityLevel(Integer priorityLevel) {
    this.priorityLevel = priorityLevel;
  }

  @Override
  public String toString() {
    return "GeoNode [id=" + id + ", name=" + name + ", longitude=" + longitude + ", latitude=" + latitude
        + ", priorityLevel=" + priorityLevel + "]";
  }

}
