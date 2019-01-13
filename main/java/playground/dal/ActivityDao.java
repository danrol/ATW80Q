package playground.dal;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import playground.logic.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, String> {

	public ArrayList<ActivityEntity> findAllByTypeAndElementId(@Param("elementId") String elementId,
			@Param("type") String type, Pageable pageable);
}
