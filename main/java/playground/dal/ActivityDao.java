package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.ActivityEntity;
public interface ActivityDao extends CrudRepository<ActivityEntity, String>{

}
