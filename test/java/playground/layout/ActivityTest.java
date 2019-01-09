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

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.Constants;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

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
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		user=userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.DEFAULT_ACTIVITY_TYPE);
		ActivityTO act = new ActivityTO(ent);
		ActivityTO ob = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);

		assertThat(act).isEqualToIgnoringGivenFields(ob, "id", "playerPlayground", "playerEmail");
	}

	// 11.2 Scenario: Sending Message activity as player
	@Test
	public void SendMessageActivityToExistingBoardAsPlayer() {
		ElementEntity messageBoard = createMessageBoard("Messageboard", 3, 6);
		messageBoard = elementService.addElementNoLogin(messageBoard);
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		user=userService.addUser(user);

		ActivityEntity ent = this.createMessage(messageBoard.getSuperkey(), "message");
		ActivityTO act = new ActivityTO(ent);
		ActivityTO message = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				user.getPlayground(), user.getEmail());
		assertThat(act).isEqualToIgnoringGivenFields(message, "id", "playerPlayground", "playerEmail");
	}

	// 11.3 Scenario: Sending Message activity to non existing message board
	@Test(expected = RuntimeException.class)
	public void SendMessageActivityToNonExistingBoard() {
		ElementEntity messageBoard = createMessageBoard("Messageboard", 3, 6);
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		user=userService.addUser(user);
		ActivityEntity ent = this.createMessage(messageBoard.getSuperkey(), "message");
		ActivityTO act = new ActivityTO(ent);
		act = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				user.getPlayground(), user.getEmail());
	}

	@Test
	public void ReadExistingQuestionActivityAsPlayer() {
		UserEntity mod = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		mod.verifyUser();
		mod=userService.addUser(mod);
		ElementEntity question = this.createQuestionElement(Constants.QUESTION_TITLE_TEST, Constants.QUESTION_BODY_TEST,
				Constants.QUESTION_CORRECT_ANSWER_TEST, Constants.QUESTION_POINT_VALUE_TEST, Constants.LOCATION_X1,
				Constants.LOCATION_Y1);
		question=elementService.addElementNoLogin(question);
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Constants.QUESTION_READ_ACTIVITY);
		activity.setElementId(question.getSuperkey());
		ActivityTO act = new ActivityTO(activity);
		System.err.println(act);
		ElementTO rv_questionTO = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ElementTO.class,
				mod.getPlayground(), mod.getEmail());
		ElementEntity rv_question = rv_questionTO.toEntity();
		assertThat(rv_question.getSuperkey()).isEqualTo(question.getSuperkey());
	}

	@Test
	public void ReadingAQuestionThatNotExistInDatabase() {

	}

	@Test
	public void AnsweringAQuestionWithCorrectAnswer() {

	}

	@Test
	public void AnsweringAQuestionWithIncorrectAnswer() {

	}

	@Test
	public void GettingScoreFromDatabase() {

	}

	@Test
	public void GetGameRulesActivity() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);

		user.verifyUser();
		user=userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.GET_GAME_RULES_ACTIVITY);
		ActivityTO act = new ActivityTO(ent);

		String rules = this.restTemplate.postForObject(this.url + Constants.Function_11, act, String.class,
				user.getPlayground(), user.getEmail());

		assertThat(rules).isEqualTo(Constants.GAME_RULES);
	}

	// url #11 /playground/activities/{userPlayground}/{email} finished
	// ******************************************************************************************//

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

	public ActivityEntity createMessage(String messageboard_key, String message) {
		ActivityEntity entity = new ActivityEntity();
		entity.setType(Constants.MESSAGE_ACTIVITY);
		entity.getAttribute().put(Constants.ACTIVITY_MESSAGE_KEY, message);
		entity.setElementId(messageboard_key);
		return entity;
	}

}
