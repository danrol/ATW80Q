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
	private ActivityDao activityDao;
	private IdGeneratorDao idGenerator;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public JpaActivityService(ActivityDao activity, IdGeneratorDao idGenerator) {
		this.activityDao = activity;
		this.idGenerator = idGenerator;
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
			return setMessage(activity);
		}
		case Constants.MESSAGE_DELETE:{
			return deleteMessage(activity);
		}
			
		}
	
		return null;
	}

	@Override
	public Object getMessage(ActivityEntity activity) {
		ElementEntity el = elementService.getElementByType(Constants.MESSAGEBOARD);
		String messageId = (String) el.getAttributes().get(Constants.MESSAGE_ID_ATTR);
		return el.getAttributes().get(messageId);
	}
	@Override
	public Object deleteMessage(ActivityEntity activity) {
		ElementEntity el = elementService.getElementByType(Constants.MESSAGEBOARD);
		String messageId = (String) el.getAttributes().get(Constants.MESSAGE_ID_ATTR);
		return el.getAttributes().remove(messageId);
	}
	@Override
	public Object setMessage(ActivityEntity activity) {
		ElementEntity el = elementService.getElementByType(Constants.MESSAGEBOARD);
		String messageId = (String) el.getAttributes().get(Constants.MESSAGE_ID_ATTR);
		return el.getAttributes().put(messageId, activity.getAttribute());
	}

	@Override
	public ActivityEntity addActivity(ActivityEntity e) {
		activityDao.save(e);
		executeActivity(e);
		return getActivity(e.getSuperkey());
	}

	@Override
	public ActivityEntity getActivity(String superkey) {
		Optional<ActivityEntity> e = activityDao.findById(superkey);
		if (e.isPresent()) {
			return e.get();
		}
		return null;
	}
}
