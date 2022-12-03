package es.indra.tools.route.generator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.indra.tools.route.generator.dto.topology.Line;
import es.indra.tools.route.generator.dto.topology.Movement;
import es.indra.tools.route.generator.dto.topology.Node;
import es.indra.tools.route.generator.service.TopologyService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/routegenerator/topology", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopologyController {

  @Autowired
  private TopologyService service;
  
  @GetMapping(value = "/group", produces= MediaType.TEXT_PLAIN_VALUE)
  public String getGroup() {
    String groupId = service.getCurrengGroupId();
    return groupId;
  }
  
  @GetMapping("/lines/{groupId}")
  public List<Line> getLines(@PathVariable String groupId) {
    return service.getLines(groupId);
  }
  
  @GetMapping("/nodes/{groupId}/{lineId}")
  public List<Node> getNodesByLine(@PathVariable String groupId, @PathVariable String lineId) {
    return service.getNodesByLine(groupId, lineId);
  }
  
  @GetMapping("/movements/{groupId}/{lineId}")
  public Map<String, Movement> getMovementsByLine(@PathVariable String groupId, @PathVariable String lineId) {
    return service.getMovementsByLine(groupId, lineId);
  }
  
}
