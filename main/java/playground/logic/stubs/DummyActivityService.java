package playground.logic.stubs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class DummyActivityService implements ActivityService {

	private ArrayList<ActivityEntity> activities = new ArrayList<ActivityEntity>();

	@Override
	public void setElementService(ElementService elementService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserService(UserService userService) {
		// TODO Auto-generated method stub
		
	}
	public List<ActivityEntity> getAll(int size, int page) {
	return new ArrayList<>(this.activities)
		.stream()
		.skip(size * page) 
		.limit(size) 
		.collect(Collectors.toList()); 
	}
}
