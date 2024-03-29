package es.indra.tools.route.generator.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.indra.tools.route.generator.dao.TrainDao;
import es.indra.tools.route.generator.dto.train.Train;

@Service
public class TrainService {

  @Autowired
  private TrainDao trainDao;
  
  public Train save(Train train) {
    return trainDao.save(train);
  }

  public List<Train> getTrains() {
    List<Train> trains = new LinkedList<>();
    trainDao.findAll().forEach(trains::add);
    return trains;
  }
}

