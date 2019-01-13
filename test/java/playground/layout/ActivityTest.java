package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;
import playground.constants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActivityTest {
	private RestTemplate restTemplate;

	private ElementService elementService;
	private UserService userService;
	private ActivityService activityService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@LocalServerPort
	private int port;
	private String url;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port;
		System.err.println(this.url);
	}

	@Before
	public void setup() {
		userService.cleanUserService();
		elementService.cleanElementService();
		activityService.cleanActivityService();
	}

	@After
	public void teardown() {
		userService.cleanUserService();
		elementService.cleanElementService();
		activityService.cleanActivityService();
	}
	// ******************************************************************************************//
	// url #11 /playground/activities/{userPlayground}/{email} started

	// 11.1 Scenario: Sending Echo activity
	@Test
	public void SendEchoActivity() {
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		user = userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Activity.DEFAULT_ACTIVITY_TYPE);
		ActivityTO act = new ActivityTO(ent);
		ActivityTO ob = this.restTemplate.postForObject(this.url + Playground.Function_11, act, ActivityTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

		assertThat(act).isEqualToIgnoringGivenFields(ob, "id", "playerPlayground", "playerEmail");
	}

	// 11.2 Scenario: Sending Message activity as player
	@Test
	public void SendMessageActivityToExistingBoardAsPlayer() {
		ElementEntity messageBoard = createMessageBoard("Messageboard", 3, 6);
		messageBoard = elementService.addElementNoLogin(messageBoard);
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		user = userService.addUser(user);

		ActivityEntity ent = this.createMessage(messageBoard.getSuperkey(), "message");
		ActivityTO act = new ActivityTO(ent);
		ActivityTO message = this.restTemplate.postForObject(this.url + Playground.Function_11, act, ActivityTO.class,
				user.getPlayground(), user.getEmail());
		assertThat(act).isEqualToIgnoringGivenFields(message, "id", "playerPlayground", "playerEmail");
	}

	// 11.3 Scenario: Sending Message activity to non existing message board
	@Test(expected = RuntimeException.class)
	public void SendMessageActivityToNonExistingBoard() {
		ElementEntity messageBoard = createMessageBoard("Messageboard", 3, 6);
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		user = userService.addUser(user);
		ActivityEntity ent = this.createMessage(messageBoard.getSuperkey(), "message");
		ActivityTO act = new ActivityTO(ent);
		act = this.restTemplate.postForObject(this.url + Playground.Function_11, act, ActivityTO.class,
				user.getPlayground(), user.getEmail());
	}

	// 11.4 Scenario: Read existing question activity
	@Test
	public void ReadExistingQuestionActivityAsPlayer() {
		UserEntity mod = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		mod.verifyUser();
		mod = userService.addUser(mod);
		ElementEntity question = this.createQuestionElement(Element.QUESTION_TITLE_TEST, Element.QUESTION_BODY_TEST,
				Element.QUESTION_CORRECT_ANSWER_TEST, Element.QUESTION_POINT_VALUE_TEST, 5, 6);
		question = elementService.addElementNoLogin(question);
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Activity.QUESTION_READ_ACTIVITY);
		activity.setElementId(question.getSuperkey());
		ActivityTO act = new ActivityTO(activity);
		ElementTO rv_questionTO = this.restTemplate.postForObject(this.url + Playground.Function_11, act,
				ElementTO.class, mod.getPlayground(), mod.getEmail());
		ElementEntity rv_question = rv_questionTO.toEntity();
		assertThat(rv_question.getSuperkey()).isEqualTo(question.getSuperkey());
	}

	// 11.5 Scenario: Reading a question that not exist in database.
	@Test(expected = RuntimeException.class)
	public void ReadingAQuestionThatDoesNotExistInDatabase() {
		UserEntity mod = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		mod.verifyUser();
		mod = userService.addUser(mod);
		ElementEntity question = this.createQuestionElement(Element.QUESTION_TITLE_TEST, Element.QUESTION_BODY_TEST,
				Element.QUESTION_CORRECT_ANSWER_TEST, Element.QUESTION_POINT_VALUE_TEST, 5, 6);
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Activity.QUESTION_READ_ACTIVITY);
		activity.setElementId(question.getSuperkey());
		ActivityTO act = new ActivityTO(activity);
		ElementTO rv_questionTO = this.restTemplate.postForObject(this.url + Playground.Function_11, act,
				ElementTO.class, mod.getPlayground(), mod.getEmail());
		ElementEntity rv_question = rv_questionTO.toEntity();
		assertThat(rv_question.getSuperkey()).isEqualTo(question.getSuperkey());
	}

	// 11.6 Scenario: Answering a question with correct answer.
	@Test
	public void AnsweringQuestionWithCorrectAnswer() {
		UserEntity mod = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		mod.verifyUser();
		mod = userService.addUser(mod);
		ElementEntity question = this.createQuestionElement(Element.QUESTION_TITLE_TEST, Element.QUESTION_BODY_TEST,
				Element.QUESTION_CORRECT_ANSWER_TEST, Element.QUESTION_POINT_VALUE_TEST, 5, 6);
		question = elementService.addElementNoLogin(question);
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Activity.QUESTION_ANSWER_ACTIVITY);
		activity.getAttribute().put(Activity.ACTIVITY_USER_ANSWER_KEY,
				question.getAttributes().get(Element.ELEMENT_ANSWER_KEY));

		activity.setElementId(question.getSuperkey());
		ActivityTO act = new ActivityTO(activity);

		long points_before = mod.getPoints();
		boolean SystemResponse = this.restTemplate.postForObject(this.url + Playground.Function_11, act, boolean.class,
				mod.getPlayground(), mod.getEmail());
		long points_after = userService.getUser(mod.getSuperkey()).getPoints();
		assertThat(SystemResponse).isEqualTo(Activity.CORRECT_ANSWER);
		int question_val = (int) question.getAttributes().get(Element.ELEMENT_POINT_KEY);
		assertThat(points_after).isEqualTo(points_before + new Long(question_val));
	}

	// 11.7 Scenario: Answering a question with incorrect answer.
	@Test
	public void AnsweringAQuestionWithIncorrectAnswer() {
		UserEntity mod = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		mod.verifyUser();
		mod = userService.addUser(mod);
		ElementEntity question = this.createQuestionElement(Element.QUESTION_TITLE_TEST, Element.QUESTION_BODY_TEST,
				Element.QUESTION_CORRECT_ANSWER_TEST, Element.QUESTION_POINT_VALUE_TEST, 5, 6);
		question = elementService.addElementNoLogin(question);
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Activity.QUESTION_ANSWER_ACTIVITY);
		activity.getAttribute().put(Activity.ACTIVITY_USER_ANSWER_KEY,
				question.getAttributes().get(Element.ELEMENT_ANSWER_KEY) + "x");

		activity.setElementId(question.getSuperkey());
		ActivityTO act = new ActivityTO(activity);

		long points_before = mod.getPoints();
		boolean SystemResponse = this.restTemplate.postForObject(this.url + Playground.Function_11, act, boolean.class,
				mod.getPlayground(), mod.getEmail());
		long points_after = userService.getUser(mod.getSuperkey()).getPoints();
		assertThat(SystemResponse).isEqualTo(Activity.WRONG_ANSWER);
		assertThat(points_after).isEqualTo(points_before);
	}

	// 11.8 Scenario: getting score from database from highest to lowest with
	// pagination.
	@Test
	public void getHighScoresDescending() {
		UserEntity user;
		int numberOfUsers = 9;
		UserTO[] usersForTest = new UserTO[5 - 1];
		long points = 110;
		ActivityEntity actEnt = new ActivityEntity();
		actEnt.setType(Activity.GET_SCORES_ACTIVITY);

		for (int i = 0; i < numberOfUsers; i++) {
			user = new UserEntity(User.DEFAULT_USERNAME, i + User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
					User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
			user.verifyUser();
			user.setPoints(points - i * 10);
			userService.addUser(user);
			if (i >= 5)
				usersForTest[i - 5] = new UserTO(user);
		}

		UserTO[] result = this.restTemplate.postForObject(
				this.url + Playground.Function_11 + createPaginationStringAppendixForUrl(1, 5), actEnt, UserTO[].class,
				Playground.PLAYGROUND_NAME, "0" + User.EMAIL_FOR_TESTS);

		assertThat(result).isNotNull();

		for (int i = 0; i < 5 - 1; i++)
			assertThat(result[i]).isEqualToComparingFieldByField(usersForTest[i]);
	}

	// 11.9 Getting game rules
	@Test
	public void getGameRulesActivity() {

		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);

		user.verifyUser();
		user = userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Activity.GET_GAME_RULES_ACTIVITY);
		ActivityTO act = new ActivityTO(ent);

		String rules = this.restTemplate.postForObject(this.url + Playground.Function_11, act, String.class,
				user.getPlayground(), user.getEmail());

		assertThat(rules).isEqualTo(Activity.GAME_RULES);
	}

	// url #11 /playground/activities/{userPlayground}/{email} finished
	// ******************************************************************************************//

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

	public ActivityEntity createMessage(String messageboard_key, String message) {
		ActivityEntity entity = new ActivityEntity();
		entity.setType(Activity.MESSAGE_ACTIVITY);
		entity.getAttribute().put(Activity.ACTIVITY_MESSAGE_KEY, message);
		entity.setElementId(messageboard_key);
		return entity;
	}

	public String createPaginationStringAppendixForUrl(int pageNum, int sizeNum) {
		return "?page=" + String.valueOf(pageNum) + "&size=" + String.valueOf(sizeNum);
	}

}
