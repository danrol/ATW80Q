package playground.dal;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import playground.logic.ElementEntity;

@RepositoryRestResource
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,String>{

	public ArrayList<ElementEntity> findAllByCreatorPlaygroundAndCreatorEmail( 
			@Param("creatorPlayground") String creatorPlayground, 
			@Param("creatorEmail") String creatorEmail, 
			Pageable pageable);
	
//	public ArrayList<ElementEntity> findByXBetweenDistanceAndYBetweenDistance(
//			double x,
//			double y,
//			double distance,
//			Pageable pageable);
}
