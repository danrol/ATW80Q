package playground.logic.jpa;

import org.springframework.data.repository.CrudRepository;

public interface IdGeneratorDao extends CrudRepository<IdGenerator, Long>{

}
