package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	private IdGeneratorDao idGenerator;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public JpaActivityService(ActivityDao activity, IdGeneratorDao idGenerator) {
		this.activityDB = activity;
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
			return addMessage(activity);
		}
		case Constants.MESSAGE_DELETE:{
			return deleteMessage(activity);
		}
			
		}
	
		return null;
	}
	//attributes: key:Constants.MESSAGEBOARD_KEY,val:message board id
	//key:Constants.MESSAGE_ID_ATTR,val:the message to put in

	@Override
	public Object getMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			return activity.getAttribute().get(Constants.MESSAGE_ATTR);
		}
		return null;
	}
	@Override
	public Object deleteMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			if(activityDB.existsById(activity.getId())) {
				activityDB.deleteById(activity.getId());
				return activity.getAttribute().get(Constants.MESSAGE_ATTR);
			}
			
		}
		return null;
	}
	@Override
	public Object addMessage(ActivityEntity activity) {
		String id =(String)activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if(elementService.getElementNoLogin(id) !=null) {
			ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
			ArrayList<ActivityEntity> lst2= new ArrayList<ActivityEntity>();
			for(ActivityEntity a:lst) {
				if(a.getAttribute().get(Constants.MESSAGE_ATTR).equals(activity.getAttribute().get(Constants.MESSAGE_ATTR))) {
					return a.getAttribute().get(Constants.MESSAGE_ATTR);
				}
			}
			activityDB.save(activity);
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
	
	@Override
	public ArrayList<ActivityEntity> getAllByElementAttributeSuperkey(String superkey,Pageable pageable){
		ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
		ArrayList<ActivityEntity> lst2= new ArrayList<ActivityEntity>();
		for (ActivityEntity a : activityDB.findAll( pageable))
			lst.add(a);
		for(ActivityEntity a :lst) {
			if(a.getAttribute().get(Constants.MESSAGEBOARD_KEY).equals(superkey)) {
				lst2.add(a);
			}
		}
		return lst2;
	}
}
