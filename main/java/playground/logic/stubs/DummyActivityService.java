package playground.logic.stubs;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.Constants;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class DummyActivityService implements ActivityService {

	private ArrayList<ActivityEntity> activities = new ArrayList<ActivityEntity>();
	private UserService userService;
	private ElementService elementService;


	
	@Override
	@Autowired
	public void setElementService(ElementService elementService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Autowired
	public void setUserService(UserService userService) {
		// TODO Auto-generated method stub
		
	}
	
	public String[] readMessagesFromMessageboard() {
		ArrayList<String> messages = new ArrayList<>();
		for(ElementEntity elEn: elementService.getAllElements()) {
			if(elEn.getType() == Constants.MESSAGEBOARD)
				messages.add(elEn.getAttributes().toString());
		}
		return messages.toArray(new String[messages.size()]);
		//TODO check if works and improvement needed
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
