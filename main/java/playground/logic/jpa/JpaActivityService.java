package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import playground.Constants;
import playground.aop.LoginRequired;
import playground.aop.MyLog;
import playground.dal.ActivityDao;
import playground.exceptions.ActivityDataException;
import playground.exceptions.ElementDataException;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

/*
 * 		Element Type : Question
 * 			Attributes:
 * 				
 * 
 * 
 * 		Activity types attributes index:
 * 			DEFAULT_ACTIVITY_TYPE: 
 * 				*NONE*
 * 
 * 			GET_MESSAGES_ACTIVITY:
 * 				ACTIVITY_MESSAGEBOARD_ID_KEY	
 * 
 * 			ADD_MESSAGES_ACTIVITY:
 * 				ACTIVITY_MESSAGEBOARD_ID_KEY 	
 * 				ACTIVITY_MESSAGE_KEY			
 * 
 * 			QUESTION_READ_ACTIVITY:
 * 				
 * 			ADD_QUESTION_ACTIVITY:
 * 			QUESTION_ANSWER_ACTIVITY:
 * 			ADD_MESSAGE_BOARD_ACTIVITY:
 * 			GET_SCORES_ACTIVITY:
 * 
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
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable) {

		String activityType = activity.getType();

		switch (activityType) {
		case Constants.DEFAULT_ACTIVITY_TYPE: {
			/*
			 * Default activity is ECHO
			 */
			return activity;
		}
		case Constants.GET_MESSAGES_ACTIVITY: {
			return getAllMessagesActivitiesInMessageBoard(
					(String) activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY), pageable);
		}
		case Constants.MESSAGE_ACTIVITY: {
			return addMessage(activity);
		}
		case Constants.QUESTION_READ_ACTIVITY: {
			return getQuestion(activity);
		}
		case Constants.ADD_QUESTION_ACTIVITY: {
			return setQuestion(activity);
		}
		case Constants.QUESTION_ANSWER_ACTIVITY: {
			return answerQuestion(activity);
		}
		case Constants.ADD_MESSAGE_BOARD_ACTIVITY: {
			return null;
		}
		case Constants.GET_SCORES_ACTIVITY: {
			return null;
			// userService.getHighScores(pageable);
		}

		}

		return null;
	}

	/*
	 * Input: activity of type GET_MESSAGE_ACTIVITY Output: activity of type
	 * MESSAGE_WRITE_ACTIVITY
	 */
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
		String msgboard_superkey = (String) activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY);
		ElementEntity messageBoard = elementService.getElementNoLogin(msgboard_superkey);
		if (messageBoard != null)
			this.addActivityNoLogin(activity);
		return activity;
	}

	@Override
	@LoginRequired
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		return addActivityNoLogin(e);
	}

	private ActivityEntity addActivityNoLogin(ActivityEntity activity) {
		// TODO: "Field 'id' doesn't have a default value" exception
		IdGeneratorActivity tmp = IdGeneratorActivity.save(new IdGeneratorActivity());
		Long id = tmp.getId();
		IdGeneratorActivity.delete(tmp);
		activity.setId(id + "");

		if (activityDB.existsById(activity.getSuperkey())) {
			throw new ElementDataException("activity data already exist in database");
		} else {
			activityDB.save(activity);
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

	/*
	 * TODO Fix this method - make a new query
	 */
	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String superkey, Pageable pageable) {
		ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
		ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
		for (ActivityEntity a : activityDB.findAllByTypeAndElementId(superkey, Constants.MESSAGE_ACTIVITY, pageable))
			lst.add(a);
		return lst;
	}

	@Override
	public Object setQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		if(elementService.getElementNoLogin(id) == null)
			throw new ElementDataException("No such element: " + id);
		if (activity.getAttribute().get(Constants.ELEMENT_QUESTION_KEY) != null
						|| activity.getAttribute().get(Constants.ELEMENT_ANSWER_KEY) != null) {

			activityDB.save(activity);
			return activity.getAttribute().get(Constants.ELEMENT_QUESTION_KEY);
		}
		else
			throw new ActivityDataException("Question fields are empty.");
	}

	@Override
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			return elementService.getElementNoLogin(id);
		}
		return null;

	}

	@Override
	public boolean answerQuestion(ActivityEntity activity) {
		String id = (String)activity.getAttribute().get(Constants.ACTIVITY_QUESTION_ID_KEY);
		if (elementService.getElementNoLogin(id) != null) {
			ElementEntity a = elementService.getElementNoLogin(id);
			if (a!=null && a.getType().equals(Constants.ELEMENT_QUESTION_TYPE)) {
				String user_answer = ((String) activity.getAttribute().get(Constants.ACTIVITY_USER_ANSWER_KEY)).toLowerCase();
				String actual_answer = ((String)a.getAttributes().get(Constants.ELEMENT_ANSWER_KEY)).toLowerCase();
			
				if (actual_answer.equals(user_answer)) {
					return Constants.CORRECT_ANSWER;
				} else {
					return Constants.WRONG_ANSWER;
				}
			}
			throw new ElementDataException("Invalid element - expected ELEMENT_QUESTION_TYPE");

		}
		throw new ElementDataException("No such element : " + id);

	}

	// when message board is created , in the attributes in map:
	// attribute name. attribute x, attribute y,
	@Override
	public Object addMessageBoard(ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			Object name = activity.getAttribute().get(Constants.ACTIVITY_MESSAGE_BOARD_NAME_KEY);
			Object x = activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY);
			Object y = activity.getAttribute().get(Constants.ACTIVITY_Y_LOCATION_KEY);
			if (name.getClass().isInstance(String.class) && x.getClass().isInstance(Double.class)
					&& y.getClass().isInstance(Double.class)) {
				ElementEntity e = new ElementEntity((String) name, activity.getPlayground(), activity.getPlayerEmail(),
						(double) x, (double) y);
				elementService.addElementNoLogin(e);
				activityDB.save(activity);
				return activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY);
			}

		}
		return null;
	}

	@Override
	public Object addQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			Object name = activity.getAttribute().get(Constants.ELEMENT_QUESTION_NAME);
			Object x = activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY);
			Object y = activity.getAttribute().get(Constants.ACTIVITY_Y_LOCATION_KEY);
			if (name.getClass().isInstance(String.class) && x.getClass().isInstance(Double.class)
					&& y.getClass().isInstance(Double.class)) {
				ElementEntity e = new ElementEntity((String) name, activity.getPlayground(), activity.getPlayerEmail(),
						(double) x, (double) y);
				elementService.addElementNoLogin(e);
				activityDB.save(activity);
				return activity.getAttribute().get(Constants.ELEMENT_QUESTION_KEY);
			}

		}
		return null;
	}

	@Override
	@MyLog
	public void cleanActivityService() {
		activityDB.deleteAll();
	}

}
