package playground.dal;



import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import playground.logic.UserEntity;

public interface UserDao  extends PagingAndSortingRepository<UserEntity, String>{


}
