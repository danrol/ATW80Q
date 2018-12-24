package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.Constants;
import playground.aop.LoginRequired;
import playground.dal.ActivityDao;
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
 * get question
 * set question
 * answer question
 * get rules
 */
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
	@LoginRequired
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity) {

		String activityType = activity.getType();

		switch (activityType) {
		case Constants.MESSAGE_READ: {
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
		String id = (String) activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			return activity.getAttribute().get(Constants.MESSAGE_ATTR);
		}
		return null;
	}

	@Override
	public Object addMessage(ActivityEntity activity) {
		String id = (String) activity.getAttribute().get(Constants.MESSAGEBOARD_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
			ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
			for (ActivityEntity a : lst) {
				if (a.getAttribute().get(Constants.MESSAGE_ATTR)
						.equals(activity.getAttribute().get(Constants.MESSAGE_ATTR))) {
					return a.getAttribute().get(Constants.MESSAGE_ATTR);
				}
			}
			activityDB.save(activity);
		}
		return null;
	}

	@Override
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		executeActivity(userPlayground, email, e);
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
	public ArrayList<ActivityEntity> getAllByElementAttributeSuperkey(String superkey, Pageable pageable) {
		ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
		ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
		for (ActivityEntity a : activityDB.findAll(pageable))
			lst.add(a);
		for (ActivityEntity a : lst) {
			if (a.getAttribute().get(Constants.MESSAGEBOARD_KEY).equals(superkey)) {
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
			return activity.getAttribute().get(Constants.ANSWER_ATTR);
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
}
