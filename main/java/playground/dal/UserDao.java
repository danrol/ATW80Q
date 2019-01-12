package playground.dal;

import java.util.ArrayList;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.domain.Pageable;
import playground.logic.UserEntity;

@RepositoryRestResource
public interface UserDao  extends PagingAndSortingRepository<UserEntity, String>{


	public UserEntity findUserByPlaygroundAndEmail(
			@Param("Playground") String playground, 
			@Param("Email") String email);
	
	public ArrayList<UserEntity> findAllByOrderByPointsDesc(Pageable pageable);
}
