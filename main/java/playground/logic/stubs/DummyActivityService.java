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
	private ArrayList<ElementEntity> messageBoardDB;
	private ArrayList<ElementEntity> questionDB;
	private IdGeneratorActivityDao IdGeneratorActivity;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public DummyActivityService(ActivityDao activity, IdGeneratorActivityDao IdGeneratorActivity) {
		this.activityDB = new ArrayList();
		this.messageBoardDB=new ArrayList();
		this.questionDB=new ArrayList();
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
			if(elEn.getType().equals(Constants.ACTIVITY_MESSAGE_KEY))
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
			return activity.getAttribute().get(Constants.ACTIVITY_MESSAGE_KEY);
		}
		return null;
	}


	@Override
	public Object addMessage(ActivityEntity activity) {
		for(ElementEntity e:messageBoardDB) {
			if(e.getSuperkey().equals(activity.getElementId())) {
				e.getAttributes().put(Constants.ACTIVITY_MESSAGE_KEY, activity.getAttribute().get(Constants.ACTIVITY_MESSAGE_KEY));
				return e;
			}
		}
		return null;
	}


	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String Superkey, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		activityDB.add(e);
		return e;
	}


	@Override
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		for(ElementEntity e:questionDB)
		if (e.getSuperkey().equals(id)) {
			return e;
		}
		return null;
	}


	@Override
	public Object setQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		for(ElementEntity e:questionDB)
		if (e.getSuperkey().equals(id)) {
			e.getAttributes().put(Constants.ELEMENT_ANSWER_KEY,activity.getAttribute().get(Constants.ELEMENT_ANSWER_KEY));
			return activity.getAttribute().get(Constants.ELEMENT_ANSWER_KEY);
		}
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
		ElementEntity e= new ElementEntity(activity.getElementId(), activity.getPlayground(), 
				activity.getPlayerEmail(),(double)activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY),
				(double)activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY));
		questionDB.add(e);
		return e;
	}


	@Override
	public Object addQuestion(ActivityEntity activity) {
		ElementEntity e= new ElementEntity(activity.getElementId(), activity.getPlayground(), 
				activity.getPlayerEmail(),(double)activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY),
				(double)activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY));
		messageBoardDB.add(e);
		return e;
	}


	@Override
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
