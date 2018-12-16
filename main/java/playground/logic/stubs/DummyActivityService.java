package playground.logic.stubs;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;

//@Service
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
	
	
	
	public ActivityEntity[] getAll(ArrayList<ActivityEntity> lst, int size, int page) {
	return lst
		.stream()
		.skip(size * page) 
		.limit(size) 
		.collect(Collectors.toList())
		.toArray(new ActivityEntity[lst.size()]); 
	}
	
}
