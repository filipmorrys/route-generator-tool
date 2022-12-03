package es.indra.tools.route.generator.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.indra.tools.route.generator.dto.train.Train;

@Repository
public interface TrainDao extends CrudRepository<Train, String> {

}
