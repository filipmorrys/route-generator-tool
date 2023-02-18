package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;

public class SearchInfo {
  private SearchResult result = SearchResult.Continue;

  private Integer distance = 0;

  private List<String> route = new LinkedList<>();

  public SearchInfo() {
  }

  public SearchInfo(SearchResult result, Integer distance, List<String> route) {
    super();
    this.result = result;
    this.distance = distance;
    this.route = route;
  }

  public SearchResult getResult() {
    return result;
  }

  public void setResult(SearchResult result) {
    this.result = result;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public List<String> getRoute() {
    return route;
  }

  public void setRoute(List<String> route) {
    this.route = route;
  }

  @Override
  public String toString() {
    return "SearchInfo [result=" + result + ", distance=" + distance + ", route=" + route + "]";
  }
  
}