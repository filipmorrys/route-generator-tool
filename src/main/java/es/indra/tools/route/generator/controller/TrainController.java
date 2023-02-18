package es.indra.tools.route.generator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.indra.tools.route.generator.dto.train.Train;
import es.indra.tools.route.generator.service.TrainService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequestMapping(value = "api/routegenerator/train", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainController {
  
  @Autowired
  private TrainService trainService;

  @PostMapping("")
  public Train save(@RequestBody Train train) {
    return trainService.save(train);
  }
  
  @GetMapping("")
  public List<Train> getTrains() {
    return trainService.getTrains();
  }
  
}
