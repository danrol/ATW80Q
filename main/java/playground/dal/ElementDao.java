package playground.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import playground.logic.ElementEntity;

@RepositoryRestResource
public interface ElementDao extends CrudRepository<ElementEntity, String>{

}
