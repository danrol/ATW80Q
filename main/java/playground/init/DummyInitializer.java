package playground.init;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import playground.Constants;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.RegisterNewUserException;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Component
@Profile("demo")
public class DummyInitializer {
	private ElementService elementService;
	private ActivityService activityService;
	private UserService userService;

	@Autowired
	public DummyInitializer(ElementService elementService, ActivityService activityService, UserService userService) {
		this();
		this.elementService = elementService;
		this.activityService = activityService;
		this.userService = userService;
	}

	public DummyInitializer() {
		// empty
	}

	@PostConstruct
	public void init() {
		ElementEntity msgBoard = createMessageBoard("DummyMessageBoard", 0, 0);

		UserEntity mod = new UserEntity("manager", "demoManager@playground.rolnik", "avatar", Constants.MANAGER_ROLE,
				Constants.PLAYGROUND_NAME);

		UserEntity player = new UserEntity("player", "demoPlayer@playground.rolnik", "avatar", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		mod.verifyUser();
		player.verifyUser();
		try {
			mod = userService.addUser(mod);
		} catch (RegisterNewUserException e) {
			mod = userService.getUser(mod.getSuperkey());
		}
		try {
			player = userService.addUser(player);

		} catch (RegisterNewUserException e) {
			player = userService.getUser(player.getSuperkey());
		}
		msgBoard = elementService.addElement(mod.getPlayground(),mod.getEmail(),msgBoard);
		String msg = "msg";
		for (int i = 0; i < 30; i++) {
			ActivityEntity entity = createMessage(msgBoard.getSuperkey(), msg + i);
			activityService.executeActivity(player.getPlayground(), player.getEmail(), entity, null);

			ElementEntity q_entity = createQuestionElement("Demo question " + String.valueOf(i),
					String.valueOf(i) + " + " + String.valueOf(i), String.valueOf(2 * i), i, i, i);
			elementService.addElement(mod.getPlayground(), mod.getEmail(), q_entity);
		}
	}

	public ActivityEntity createMessage(String messageboard_key, String message) {
		ActivityEntity entity = new ActivityEntity();
		entity.setType(Constants.MESSAGE_ACTIVITY);
		entity.getAttribute().put(Constants.ACTIVITY_MESSAGE_KEY, message);
		entity.setElementId(messageboard_key);
		return entity;
	}

	public ElementEntity createQuestionElement(String questionTitle, String questionBody, String answer, int points,
			double x, double y) {
		ElementEntity question = new ElementEntity(questionTitle, x, y);
		question.setType(Constants.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Constants.ELEMENT_QUESTION_KEY, questionBody);
		question.getAttributes().put(Constants.ELEMENT_ANSWER_KEY, answer);
		question.getAttributes().put(Constants.ELEMENT_POINT_KEY, points);
		return question;
	}

	public ElementEntity createMessageBoard(String messageBoardName, double x, double y) {
		ElementEntity board = new ElementEntity(messageBoardName, x, y);
		board.setType(Constants.ELEMENT_MESSAGEBOARD_TYPE);
		board.getAttributes().put(Constants.MESSAGEBOARD_MESSAGE_COUNT, 0);
		return board;
	}

}
