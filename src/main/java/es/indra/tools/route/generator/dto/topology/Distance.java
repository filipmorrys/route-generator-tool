package es.indra.tools.route.generator.dto.topology;

public class Distance {
  
  private Integer distance;
  
  private NodeGraph previous;

  /**
   * Constructor
   * @param distance
   * @param previous
   */
  public Distance(Integer distance, NodeGraph previous) {
    super();
    this.distance = distance;
    this.previous = previous;
  }
  
  /**
   * Si no se especifica distancia se asume distancia infinita
   * @param previous
   */
  public Distance(NodeGraph previous) {
    this(Integer.MAX_VALUE, previous);
  }

  public Integer getDistance() {
    return distance;
  }

  public NodeGraph getPrevious() {
    return previous;
  }

  @Override
  public String toString() {
    return "Distance [distance=" + distance + ", previous=" + previous + "]";
  }
  
}
