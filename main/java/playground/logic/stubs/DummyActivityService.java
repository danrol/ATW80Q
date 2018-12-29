package playground.logic.stubs;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import playground.Constants;
import playground.dal.ActivityDao;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;
import playground.logic.jpa.IdGeneratorActivityDao;

//@Service
public class DummyActivityService implements ActivityService {

	private ArrayList<ActivityEntity> activityDB;
	private IdGeneratorActivityDao IdGeneratorActivity;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public DummyActivityService(ActivityDao activity, IdGeneratorActivityDao IdGeneratorActivity) {
		this.activityDB = new ArrayList();
		this.IdGeneratorActivity = IdGeneratorActivity;
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;

	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;

	}
	
	public String[] readMessagesFromMessageboard() {
		ArrayList<String> messages = new ArrayList<>();
		for(ElementEntity elEn: elementService.getAllElements()) {
			if(elEn.getType() == Constants.MESSAGE_ATTR_MESSAGE_TYPE)
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

	

	@Override
	public ActivityEntity getActivity(String superkey) {
		for(ActivityEntity e:activityDB) {
			if(e.getSuperkey().equals(superkey)) {
				return e;
			}
		}
		return null;
	}


	@Override
	public Object getMessage(ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			return activity.getAttribute().get(Constants.MESSAGE_ATTR_MESSAGE_TYPE);
		}
		return null;
	}


	@Override
	public Object addMessage(ActivityEntity activity) {
		return null;
	}


	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String Superkey, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object getQuestion(ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object setQuestion(ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object answerQuestion(ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void cleanActivityService() {
		activityDB.clear();
		
	}


	@Override
	public Object addMessageBoard(ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object addQuestion(ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
