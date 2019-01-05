package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import playground.Constants;
import playground.aop.LoginRequired;
import playground.aop.ManagerLogin;
import playground.aop.MyLog;
import playground.aop.PlayerLogin;
import playground.dal.ActivityDao;
import playground.exceptions.ActivityDataException;
import playground.exceptions.ElementDataException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

/*
 * 		Element Type : Question
 * 			Attributes:
 * 				ELEMENT_QUESTION_KEY
 * 				ELEMENT_QUESTION_TITLE_KEY
 * 				ELEMENT_ANSWER_KEY
 * 				ELEMENT_POINT_KEY
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 		Activity types attributes index:
 * 			DEFAULT_ACTIVITY_TYPE:
 * 				USER_ID_OF_ACTIVITY_KEY 
 * 
 * 			GET_MESSAGES_ACTIVITY:
 * 				ACTIVITY_MESSAGEBOARD_ID_KEY	
 * 				USER_ID_OF_ACTIVITY_KEY
 * 			ADD_MESSAGES_ACTIVITY:
 * 				ACTIVITY_MESSAGEBOARD_ID_KEY 	
 * 				ACTIVITY_MESSAGE_KEY			
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 
 * 			QUESTION_READ_ACTIVITY:
 * 				ACTIVITY_QUESTION_ID_KEY
 * 				ACTIVITY_USER_ANSWER_KEY
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 			ADD_QUESTION_ACTIVITY:
 * 				ACTIVITY_SET_QUESTION_QUESTION_TITLE
 * 				ACTIVITY_SET_QUESTION_QUESTION
 * 				ACTIVITY_SET_QUESTION_ANSWER
 * 				ACTIVITY_SET_QUESTION_POINTS
 * 				ACTIVITY_X_LOCATION_KEY
 * 				ACTIVITY_Y_LOCATION_KEY
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 			QUESTION_ANSWER_ACTIVITY:
 * 				ACTIVITY_QUESTION_ID_KEY
 * 				ACTIVITY_USER_ANSWER_KEY
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 			ADD_MESSAGE_BOARD_ACTIVITY:
 * 				USER_ID_OF_ACTIVITY_KEY
 * 
 * 			GET_SCORES_ACTIVITY:
 * 				USER_ID_OF_ACTIVITY_KEY
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
			// return getAllMessagesActivitiesInMessageBoard(
			// (String) activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY),
			// pageable);

			return getAllMessagesActivitiesInMessageBoard(activity.getElementId(), pageable);
		}
		case Constants.MESSAGE_ACTIVITY: {
			return addMessage(userPlayground, email, activity);
		}
		case Constants.QUESTION_READ_ACTIVITY: {
			return getQuestion(activity);
		}
		case Constants.ADD_QUESTION_ACTIVITY: {
			return addQuestion(userPlayground, email, activity);
		}
		case Constants.QUESTION_ANSWER_ACTIVITY: {
			return answerQuestion(activity);
		}
		case Constants.ADD_MESSAGE_BOARD_ACTIVITY: {
			return this.addMessageBoard(userPlayground, email, activity);
		}
		case Constants.GET_SCORES_ACTIVITY: {
			return null;
			// userService.getHighScores(pageable);
		}

		}
		throw new ActivityDataException("No such activity type: " + activityType);
	}

	@Override
	@PlayerLogin
	public Object addMessage(String userPlayground, String email, ActivityEntity activity) {
		// String msgboard_superkey = (String)
		// activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY);
		String msgboard_superkey = activity.getElementId();
		ElementEntity messageBoard = elementService.getElementNoLogin(msgboard_superkey);
		if (messageBoard != null)
			this.addActivityNoLogin(activity);
		else
			throw new ElementDataException("No such Message Board : " + msgboard_superkey);
		return activity;
	}

	@Override
	@LoginRequired
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		return addActivityNoLogin(e);
	}

	private ActivityEntity addActivityNoLogin(ActivityEntity activity) {
		if (activityDB.existsById(activity.getSuperkey()))
			throw new ActivityDataException("Activity exists: " + activity.getSuperkey());

		IdGeneratorActivity tmp = IdGeneratorActivity.save(new IdGeneratorActivity());
		Long id = tmp.getId();
		IdGeneratorActivity.delete(tmp);
		activity.setId(id + "");

		activityDB.save(activity);
		return getActivity(activity.getSuperkey());

	}

	@Override
	public ActivityEntity getActivity(String superkey) {
		return activityDB.findById(superkey).orElse(null);

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
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();

		if (elementService.getElementNoLogin(id) != null) {
			return elementService.getElementNoLogin(id);
		}
		throw new ElementDataException("No question found in database");

	}

	@Override
	public boolean answerQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			ElementEntity a = elementService.getElementNoLogin(id);
			if (a != null && a.getType().equals(Constants.ELEMENT_QUESTION_TYPE)) {
				String user_answer = ((String) activity.getAttribute().get(Constants.ACTIVITY_USER_ANSWER_KEY))
						.toLowerCase();
				String actual_answer = ((String) a.getAttributes().get(Constants.ELEMENT_ANSWER_KEY)).toLowerCase();
				long points = (long) a.getAttributes().get(Constants.ELEMENT_POINT_KEY);
				String answering_user_email = activity.getPlayerEmail();
				String answering_user_playground = activity.getPlayerPlayground();
				if (actual_answer.equals(user_answer)) {
					userService.addPointsToUser(UserEntity.createKey(answering_user_email, answering_user_playground),
							points);
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
	@ManagerLogin
	public Object addMessageBoard(String userPlayground, String email, ActivityEntity activity) {
		String id = activity.getElementId();
		if (elementService.getElementNoLogin(id) != null) {
			Object name = activity.getAttribute().get(Constants.ACTIVITY_MESSAGE_BOARD_NAME_KEY);
			Object x = activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY);
			Object y = activity.getAttribute().get(Constants.ACTIVITY_Y_LOCATION_KEY);
			if (name.getClass().isInstance(String.class) && x.getClass().isInstance(Double.class)
					&& y.getClass().isInstance(Double.class)) {
				ElementEntity e = new ElementEntity((String) name, activity.getPlayground(), activity.getPlayerEmail(),
						(double) x, (double) y);
				e.setType(Constants.ELEMENT_MESSAGEBOARD_TYPE);
				e.setCreatorEmail(activity.getPlayerEmail());
				e = elementService.addElementNoLogin(e);
				return e;
			}

		}
		throw new ElementDataException("Cannot add message");
	}

	@ManagerLogin
	@Override
	public Object addQuestion(String userPlayground, String email, ActivityEntity activity) {
		String question = (String) activity.getAttribute().get(Constants.ACTIVITY_SET_QUESTION_QUESTION);
		String question_title = (String) activity.getAttribute().get(Constants.ACTIVITY_SET_QUESTION_QUESTION_TITLE);
		String answer = (String) activity.getAttribute().get(Constants.ACTIVITY_SET_QUESTION_ANSWER);
		long points = (long) activity.getAttribute().get(Constants.ACTIVITY_SET_QUESTION_POINTS);
		double x = (double) activity.getAttribute().get(Constants.ACTIVITY_X_LOCATION_KEY);
		double y = (double) activity.getAttribute().get(Constants.ACTIVITY_Y_LOCATION_KEY);

		if (question != null || question_title != null || answer != null)
			throw new ActivityDataException("Attribute is missin in question");

		ElementEntity question_element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME,
				activity.getElementPlayground(), activity.getPlayerEmail(), x, y);
		question_element.setType(Constants.ELEMENT_QUESTION_TYPE);
		question_element.getAttributes().put(Constants.ELEMENT_QUESTION_TITLE_KEY, question_title);
		question_element.getAttributes().put(Constants.ELEMENT_QUESTION_KEY, question);
		question_element.getAttributes().put(Constants.ELEMENT_ANSWER_KEY, answer);
		question_element.getAttributes().put(Constants.ELEMENT_POINT_KEY, points);
		ElementEntity element = elementService.addElementNoLogin(question_element);
		this.addActivityNoLogin(activity);
		return element;
	}

	@Override
	@MyLog
	public void cleanActivityService() {
		activityDB.deleteAll();
	}

}
