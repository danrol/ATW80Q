package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.ElementEntity;

public interface ElementDao extends CrudRepository<ElementEntity, String> {

}


