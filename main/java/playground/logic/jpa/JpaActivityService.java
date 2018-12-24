package playground.logic.jpa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.Constants;
import playground.dal.ActivityDao;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class JpaActivityService implements ActivityService {
	private ActivityDao activityDB;
	private IdGeneratorActivityDao idGeneratorActivity;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public JpaActivityService(ActivityDao activity, IdGeneratorActivityDao idGeneratorActivity) {
		this.activityDB = activity;
		this.idGeneratorActivity = idGeneratorActivity;
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;

	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;

	}

	@Override
	public Object executeActivity(ActivityEntity activity) {
		
		String activityType=activity.getType();
		
		switch (activityType) {
		case Constants.MESSAGE_READ:{
			return getMessage(activity);
		}
		case Constants.MESSAGE_WRITE:{
			return addMessage(activity);
		}
		case Constants.MESSAGE_DELETE:{
			return deleteMessage(activity);
		}
			
		}
	
		return null;
	}
	//atributes: key:Constants.MESSAGEBOARD_KEY,val:message board id
	//key:Constants.MESSAGE_ID_ATTR,val:the message to put in

	@Override
	public Object getMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			return activity.getAttribute().get(Constants.MESSAGE_ID_ATTR);
		}
		return null;
	}
	@Override
	public Object deleteMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			if(activityDB.existsById(activity.getId())) {
				activityDB.deleteById(activity.getId());
				return activity.getAttribute().get(Constants.MESSAGE_ID_ATTR);
			}
			
		}
		return null;
	}
	@Override
	public Object addMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			if(!activityDB.existsById(activity.getId())) {
				activityDB.deleteById(activity.getId());
				activityDB.save(activity);
				return activity.getAttribute().get(Constants.MESSAGE_ID_ATTR);
			}
		}
		return null;
	}

	@Override
	public ActivityEntity addActivity(ActivityEntity e) {
		executeActivity(e);
		return getActivity(e.getSuperkey());
	}

	@Override
	public ActivityEntity getActivity(String superkey) {
		Optional<ActivityEntity> e = activityDB.findById(superkey);
		if (e.isPresent()) {
			return e.get();
		}
		return null;
	}
}
