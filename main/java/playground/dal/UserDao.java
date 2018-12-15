package playground.dal;



import org.springframework.data.repository.PagingAndSortingRepository;

import playground.logic.UserEntity;

public interface UserDao  extends PagingAndSortingRepository<UserEntity, String>{

}
