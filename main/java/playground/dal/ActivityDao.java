package playground.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import playground.logic.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, String> {

}
