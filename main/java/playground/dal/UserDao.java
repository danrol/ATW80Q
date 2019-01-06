package playground.dal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import playground.logic.UserEntity;


@RepositoryRestResource
public interface UserDao  extends PagingAndSortingRepository<UserEntity, String>{


	public UserEntity findUserByPlaygroundAndEmail(
			@Param("Playground") String Playground, 
			@Param("Email") String Email);
	
}
