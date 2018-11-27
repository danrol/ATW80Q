package playground.dal;
import org.springframework.data.repository.CrudRepository;


import playground.logic.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, String>{

}
