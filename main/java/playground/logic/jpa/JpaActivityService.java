package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.aop.LoginRequired;
import playground.aop.ManagerLogin;
import playground.aop.MyLog;
import playground.aop.PlayerLogin;
import playground.constants.Activity;
import playground.constants.Constants;
import playground.constants.Element;
import playground.dal.ActivityDao;
import playground.logic.ActivityDataException;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.RegisterNewUserException;
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

	@MyLog
	@Override
	@PlayerLogin
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable) {
		activity.setPlayerEmail(email);
		activity.setPlayerPlayground(userPlayground);
		String activityType = activity.getType();
		System.err.println("activityType: " + activityType);
		switch (activityType) {
		case Activity.DEFAULT_ACTIVITY_TYPE: {
			System.err.println("1");
			/*
			 * Default activity is ECHO
			 */
			return activity;
		}
		case Activity.GET_MESSAGES_ACTIVITY: {
			System.err.println("2");
			/*
			 * return getAllMessagesActivitiesInMessageBoard( (String)
			 * activity.getAttribute().get(Constants.ACTIVITY_MESSAGEBOARD_ID_KEY),
			 * pageable);
			 */
			return getAllMessagesActivitiesInMessageBoard(activity.getElementId(), pageable);
		}
		case Activity.MESSAGE_ACTIVITY: {
			return addMessage(activity);
		}
		case Activity.QUESTION_READ_ACTIVITY: {
			return getQuestion(activity);
		}
		case Activity.QUESTION_ANSWER_ACTIVITY: {
			System.err.println("4");
			return answerQuestion(activity);
		}
		case Activity.GET_SCORES_ACTIVITY: {
			System.err.println("5");
			return null;
			// userService.getHighScores(pageable);
		}
		case Activity.GET_GAME_RULES_ACTIVITY:{
			return getGameRules(activity);
		}

		}
		throw new ActivityDataException("No such activity type: " + activityType);
	}

	@Override
	public String getGameRules(ActivityEntity activity) {
		return Constants.GAME_RULES;
	}



	@MyLog
	@Override
	public Object addMessage(ActivityEntity activity) {
		
		String msgboard_superkey = activity.getElementId();
		ElementEntity messageBoard = elementService.getElementNoLogin(msgboard_superkey);
		if (messageBoard != null) {
			this.addActivityNoLogin(activity);
			int num = (int) messageBoard.getAttributes().get(Element.MESSAGEBOARD_MESSAGE_COUNT);
			messageBoard.getAttributes().replace(Element.MESSAGEBOARD_MESSAGE_COUNT, ++num);
			messageBoard.getAttributes().put(String.valueOf(num),activity.getSuperkey());
			elementService.updateElementInDatabaseFromExternalElementNoLogin(messageBoard);
		} else
			throw new ElementDataException("No such Message Board : " + msgboard_superkey);
		return activity;
	}

	@MyLog
	@Override
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

	@MyLog
	@Override
	public ActivityEntity getActivity(String superkey) {
		return activityDB.findById(superkey).orElse(null);

	}

	/*
	 * TODO Fix this method - make a new query
	 */
	@MyLog
	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String superkey, Pageable pageable) {
		ArrayList<ActivityEntity> lst = new ArrayList<ActivityEntity>();
		ArrayList<ActivityEntity> lst2 = new ArrayList<ActivityEntity>();
		for (ActivityEntity a : activityDB.findAllByTypeAndElementId(superkey, Activity.MESSAGE_ACTIVITY, pageable))
			lst.add(a);
		return lst;
	}

	@MyLog
	@Override
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();

		if (elementService.getElementNoLogin(id) != null) {
			ElementEntity question = elementService.getElementNoLogin(id);
			question.getAttributes().replace(Element.ELEMENT_ANSWER_KEY, "CENSORED!! You will have to try harder.");
			return question;
		}
		throw new ElementDataException("No question found in database");

	}

	@MyLog
	@Override
	public boolean answerQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		ElementEntity a = elementService.getElementNoLogin(id);
		if (a != null) {
			if (a.getType().equals(Element.ELEMENT_QUESTION_TYPE)) {
				String user_answer = ((String) activity.getAttribute().get(Activity.ACTIVITY_USER_ANSWER_KEY))
						.toLowerCase();
				String actual_answer = ((String) a.getAttributes().get(Element.ELEMENT_ANSWER_KEY)).toLowerCase();
				int points = (int) a.getAttributes().get(Element.ELEMENT_POINT_KEY);
				String answering_user_email = activity.getPlayerEmail();
				String answering_user_playground = activity.getPlayerPlayground();
				if (actual_answer.equals(user_answer)) {
					userService.addPointsToUser(userService.createKey(answering_user_email, answering_user_playground),
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


	@Override
	@MyLog
	public void cleanActivityService() {
		activityDB.deleteAll();
	}

}
