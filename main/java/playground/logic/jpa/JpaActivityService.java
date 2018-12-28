package playground.logic.jpa;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import playground.Constants;
import playground.aop.LoginRequired;
import playground.aop.MyLog;
import playground.dal.ActivityDao;
import playground.exceptions.ElementDataException;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;
/* 
 * TYPE Message_write
 * attributes: 
 * 		key:Constants.MESSAGEBOARD_KEY
 * 		val:message board id
 * 
 * 
 *		key:Constants.MESSAGE_ID_ATTR
 *		val:the message to put in
*/

/*
 * ELEMENT:question ,ELEMENT->ATTRIBUTE:answer
 *	there is a problem that with the paging in method addMessage()
 */
@Service
public class JpaActivityService implements ActivityService {
	private ActivityDao activityDB;
	private IdGeneratorActivityDao IdGeneratorActivity;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public JpaActivityService(ActivityDao activity, IdGeneratorActivityDao IdGeneratorActivity) {
		this.activityDB = activity;
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

	@Override
	@LoginRequired
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity) {

		this.addActivityNoLogin(activity);
		String activityType = activity.getType();

		switch (activityType) {
		case Constants.DEFAULT_ACTIVITY_TYPE:
		{
			/*
			 * Default activity is ECHO
			 * */
			return activity;
		}
		case Constants.MESSAGE: {
			return getMessage(activity);
		}
		case Constants.MESSAGE_WRITE: {
			return addMessage(activity);
		}
		case Constants.QUESTION_READ:{
			return getQuestion(activity);
		}
		case Constants.QUESTION_WRITE:{
			return setQuestion(activity);
		}
		case Constants.QUESTION_ANSWER:{
			return answerQuestion(activity);
		}
			
		}

		return null;
	}



	@Override
	public Object getMessage(ActivityEntity activity) {
		String id = (String) activity.getAttribute().get(Constants.MESSAGEBOARD_ID_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			return activity.getAttribute().get(Constants.MESSAGE_ATTR);
		}
		return null;
	}

	@Override
	public Object addMessage(ActivityEntity activity) {
		int PAGE_NUM = 0, PAGE_SIZE=8;
		String messageBoard_ID = (String) activity.getAttribute().get(Constants.MESSAGEBOARD_ID_KEY);
		if (elementService.getElementNoLogin(messageBoard_ID) != null) {
			ArrayList<ActivityEntity> activities = getAllActivitiesInMessageBoard(messageBoard_ID,PageRequest.of(PAGE_NUM,PAGE_SIZE,Direction.ASC,messageBoard_ID));

			//checking if message already exists, returns if yes
			for (ActivityEntity message_activity : activities) {
				if (message_activity.getAttribute().get(Constants.MESSAGE_ATTR)
						.equals(activity.getAttribute().get(Constants.MESSAGE_ATTR))) {
					return message_activity.getAttribute().get(Constants.MESSAGE_ATTR);
				}
			}
			
			activityDB.save(activity);
		}
		return null;
	}

	@Override
	@LoginRequired
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		return addActivityNoLogin(e);
	}
	
	private ActivityEntity addActivityNoLogin(ActivityEntity activity) {
		if (activityDB.existsById(activity.getSuperkey())) {
			throw new ElementDataException("activity data already exist in database");
		} else {
			IdGeneratorActivity tmp = IdGeneratorActivity.save(new IdGeneratorActivity());
			//TODO: "Field 'id' doesn't have a default value" exception 
			Long id = tmp.getId();
			IdGeneratorActivity.delete(tmp);
			activity.setId(id +"");
			//return activityDB.save(e);
			return getActivity(activity.getSuperkey());
		}
		
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
	public ArrayList<ActivityEntity> getAllActivitiesInMessageBoard(String superkey, Pageable pageable) {
		ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
		ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
		for (ActivityEntity a : activityDB.findAll(pageable))
			lst.add(a);
		for (ActivityEntity a : lst) {
			if (a.getAttribute().get(Constants.MESSAGEBOARD_ID_KEY).equals(superkey)) {
				lst2.add(a);
			}
		}
		return lst2;
	}
	
	@Override 
	public Object setQuestion(ActivityEntity activity) {
		String id = (String) activity.getAttribute().get(Constants.QUESTION_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
			ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
			for (ActivityEntity a : lst) {
				if (a.getAttribute().get(Constants.ANSWER_ATTR)
						.equals(activity.getAttribute().get(Constants.ANSWER_ATTR))) {
					return activity.getAttribute().get(Constants.QUESTION_KEY);
				}
			}
			activityDB.save(activity);
			return activity.getAttribute().get(Constants.QUESTION_KEY);
		}
		return null;
	}
	@Override 
	public Object getQuestion(ActivityEntity activity) {
		String id = (String) activity.getAttribute().get(Constants.QUESTION_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			return elementService.getElementNoLogin(id);
		}
		return null;
		
	}
	@Override 
	public Object answerQuestion(ActivityEntity activity) {
		String id = (String) activity.getAttribute().get(Constants.QUESTION_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			Optional<ActivityEntity> a =activityDB.findById(id);
			if(a.isPresent()) {
				if(a.get().getAttribute().get(Constants.ANSWER_ATTR).equals(activity.getAttribute().get(Constants.ANSWER_ATTR))) {
					return "answer is correct";
				}else
				{
					return "answer is incorrect";
				}
			}
			return null;
				
		}
		return null;
		
	}
	
	@Override
	@MyLog
	public void cleanActivityService() {
		activityDB.deleteAll();
	}
	
	
}
