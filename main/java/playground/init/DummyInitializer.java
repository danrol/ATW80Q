package playground.init;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import playground.constants.Activity;
import playground.constants.Element;
import playground.constants.Playground;
import playground.constants.User;
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

		UserEntity mod = new UserEntity(Playground.DUMMY_MANAGER_USERNAME, "demoManager@playground.rolnik", "avatar",
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		UserEntity admin = new UserEntity("admin", "admin@playground.rolnik", "avatar", User.MANAGER_ROLE,
				Playground.PLAYGROUND_NAME);
		admin.verifyUser();

		UserEntity player = new UserEntity(Playground.DUMMY_PLAYER_USERNAME, "demoPlayer@playground.rolnik", "avatar",
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		mod.verifyUser();
		player.verifyUser();

		try {
			userService.addUser(admin);

		} catch (RegisterNewUserException e) {

		}
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
		msgBoard = elementService.addElement(mod.getPlayground(), mod.getEmail(), msgBoard);
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
		entity.setType(Activity.MESSAGE_ACTIVITY);
		entity.getAttribute().put(Activity.ACTIVITY_MESSAGE_KEY, message);
		entity.setElementId(messageboard_key);
		return entity;
	}

	public ElementEntity createQuestionElement(String questionTitle, String questionBody, String answer, int points,
			double x, double y) {
		ElementEntity question = new ElementEntity(questionTitle, x, y);
		question.setType(Element.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Element.ELEMENT_QUESTION_KEY, questionBody);
		question.getAttributes().put(Element.ELEMENT_ANSWER_KEY, answer);
		question.getAttributes().put(Element.ELEMENT_POINT_KEY, points);
		return question;
	}

	public ElementEntity createMessageBoard(String messageBoardName, double x, double y) {
		ElementEntity board = new ElementEntity(messageBoardName, x, y);
		board.setType(Element.ELEMENT_MESSAGEBOARD_TYPE);
		board.getAttributes().put(Element.MESSAGEBOARD_MESSAGE_COUNT, 0);
		return board;
	}

}
