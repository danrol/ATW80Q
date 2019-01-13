package playground.logic.jpa;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import playground.aop.MyLog;
import playground.aop.PlayerLogin;
import playground.constants.Activity;
import playground.constants.Element;
import playground.dal.ActivityDao;
import playground.logic.ActivityDataException;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

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
		switch (activityType) {
		case Activity.DEFAULT_ACTIVITY_TYPE: {
			return activity;
		}
		case Activity.GET_MESSAGES_ACTIVITY: {

			return getAllMessagesActivitiesInMessageBoard(activity.getElementId(), pageable);
		}
		case Activity.MESSAGE_ACTIVITY: {
			return addMessage(activity);
		}
		case Activity.QUESTION_READ_ACTIVITY: {
			return getQuestion(activity);
		}
		case Activity.QUESTION_ANSWER_ACTIVITY: {
			return answerQuestion(activity);
		}
		case Activity.GET_SCORES_ACTIVITY: {

			return userService.getHighScoresFromHighestToLowest(pageable);
		}
		case Activity.GET_GAME_RULES_ACTIVITY: {
			return getGameRules(activity);
		}
		}
		throw new ActivityDataException(Activity.ACTIVITY_TYPE_NOT_RECOGNIZED_ERROR + activityType);

	}

	@Override
	public String getGameRules(ActivityEntity activity) {
		return Activity.GAME_RULES;
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
			messageBoard.getAttributes().put(String.valueOf(num), activity.getSuperkey());
			elementService.updateElementInDatabaseFromExternalElementNoLogin(messageBoard);
		} else
			throw new ElementDataException(Element.MESSAGEBOARD_NOT_FOUND_ERROR + msgboard_superkey);
		return activity;
	}

	@Override
	public ActivityEntity createActivityEntity(String json) {
		ActivityEntity activity = null;
		try {
			activity = new ActivityEntity(json);
		} catch (Exception e) {
			throw new ActivityDataException(e.getMessage());
		}
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

	@MyLog
	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String superkey, Pageable pageable) {
		return activityDB.findAllByTypeAndElementId(superkey, Activity.MESSAGE_ACTIVITY, pageable);
	}

	@MyLog
	@Override
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();

		if (elementService.getElementNoLogin(id) != null) {
			ElementEntity question = elementService.getElementNoLogin(id);
			return question;
		}
		throw new ElementDataException(Element.QUESTION_NOT_FOUND_ERROR);

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
					return Activity.CORRECT_ANSWER;
				} else {
					return Activity.WRONG_ANSWER;
				}
			}
			throw new ElementDataException(Element.QUESTION_TYPE_ELEMENT_EXPECTED_ERROR);

		}
		throw new ElementDataException(Element.NO_SUCH_ELEMENT_ERROR + id);

	}

	@Override
	@MyLog
	public void cleanActivityService() {
		activityDB.deleteAll();
	}

}
