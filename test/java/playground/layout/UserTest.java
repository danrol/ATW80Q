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
import playground.Constants;
import playground.logic.ElementService;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest {
	private RestTemplate restTemplate;

	private ElementService elementService;
	private UserService userService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
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

	}

	@After
	public void teardown() {
		userService.cleanUserService();
		elementService.cleanElementService();
	}

//******************************************************************************************//
	// url #1 /playground/users tests start
	
	//1.1 Scenario: Test register with wrong email (no “@” and web address afterwards) 

	@Test(expected = RuntimeException.class)
	public void registerNewUserWithWrongEmail() {//TODO: tests throw RuntimeException at the NewUserForm and fail restTemplate

		NewUserForm postUserForm = new NewUserForm("WrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS,
				Constants.PLAYER_ROLE);
		this.restTemplate.postForObject(this.url + "/playground/users", postUserForm,
				UserTO.class);
	}

	//1.2 Scenario: Test successful register 
	@Test
	public void successfullyRegisterNewUser() {

		NewUserForm postUserForm = new NewUserForm(Constants.EMAIL_FOR_TESTS, "Curiosity", Constants.AVATAR_FOR_TESTS, "PLAYER");
		UserTO testValue = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(),
				postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url + "/playground/users", postUserForm,
				UserTO.class);		
		assertThat(actualReturnedValue).isNotNull().isEqualToComparingFieldByField(testValue);
	}
	//1.3 Scenario: Test register user with the same email as the one that another user already have in the database
	@Test(expected = RuntimeException.class)
	public void registerUserThatAlreadyExists() {

		NewUserForm postUserForm = new NewUserForm(Constants.EMAIL_FOR_TESTS, "Curiosity", Constants.AVATAR_FOR_TESTS, "PLAYER");
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(),
				postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));
		userService.addUser(userToAdd.toEntity());
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url + "/playground/users", postUserForm,
				UserTO.class);
		assertThat(actualReturnedValue).isNull();
	}

	// url #1 playground/users tests finished

	// ******************************************************************************************//
	// url #2 /playground/users/confirm/{playground}/{email}/{code} test starts
	
	//2.1 Scenario: Email not registered.
	@Test(expected = RuntimeException.class)
	public void confirmUserEmailNotInDatabase() {

		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, "1234");
	}
	//2.2 Scenario : Successful confirmation
	@Test
	public void confirmUserWithCorrectCode() {

		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}",
				UserTO.class, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, u.getVerificationCode());

		assertThat(user).isNotNull();

	}
	//2.3 Scenario: Playground doesn’t match user 
	@Test(expected = RuntimeException.class)
	public void confirmUserNotInPlayground() {

		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS,Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				"OtherPlayground", Constants.EMAIL_FOR_TESTS, u.getVerificationCode());
	}
	//2.4 Scenario: Email is registered but verification code is wrong
	@Test(expected = RuntimeException.class)
	public void confirmUserWithIncorrectVerificationCode() {

		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		String code = u.getVerificationCode();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, code + "x");
	}

	// url #2 /playground/users/confirm/{playground}/{email}/{code} test finished

	// ******************************************************************************************//
	// url #3 /playground/users/login/{playground}/{email} tests started
	//3.1 Scenario: Successful Login
	//TODO: check if all test 3 works after fixing the "Login Required"
	@Test
	public void loginUserWithCorrectEmail() {
		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);
		assertThat(user).isNotNull();
	}
	//3.2 Scenario: Email not in Database
	@Test(expected = RuntimeException.class)//TODO: email is not like in Gherkin example
	public void loginUserEmailNotInDatabase() {
		UserEntity u = new UserEntity("userTest", "nudnik2@mail.ru", Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);

	}
	//3.3 Scenario: User doesn’t belong in playground
	@Test(expected = RuntimeException.class)
	public void loginUserNotInPlayground() {
		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				"OtherPlayground");
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);
	}
	//3.4 Scenario: User not verified
	@Test
	public void loginUserWhenUserNotVerified() {

		UserEntity u = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		assertThat(u.isVerified()).isFalse();
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);
		assertThat(user.toEntity().isVerified()).isFalse();
		
	}

	// url #3/playground/users/login/{playground}/{email} test finished
	// ******************************************************************************************//
	// url #4 /playground/users/{playground}/{email} test starts
	
	//4.1 Scenario: Moderator changes his user
	@Test
	public void changeUserWhenRoleIsModeratorAndChangeHisUser() {

		UserEntity moderatorUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		moderatorUser.verifyUser();
		userService.addUser(moderatorUser);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser,
				Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	
	//4.2 Scenario: Moderator changes other moderator user
	@Test(expected = RuntimeException.class)
	public void changeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {

		UserEntity moderatorUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		moderatorUser.verifyUser();
		userService.addUser(moderatorUser);

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,",
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		OtherUser.verifyUser();
		userService.addUser(OtherUser);

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				moderatorUser.getEmail());
	}
	
	//4.3 Scenario: Moderator changes other player user
	@Test(expected = RuntimeException.class)
	public void changeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {

		UserEntity moderatorUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, "Test.jpg,",
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		moderatorUser.verifyUser();
		userService.addUser(moderatorUser);

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		OtherUser.verifyUser();
		userService.addUser(OtherUser);

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				moderatorUser.getEmail());
	}

	//4.4 Scenario: Player changes his user
	@Test
	public void changeUserWhenRoleIsPlayerAndChangeHisUser() {

		UserEntity PlayerUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		PlayerUser.verifyUser();
		userService.addUser(PlayerUser);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", PlayerUser,
				Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}
	
	//4.5 Scenario: Player changes other player user
	@Test(expected = RuntimeException.class)
	public void changeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsPlayer() {

		UserEntity PlayerUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		PlayerUser.verifyUser();
		userService.addUser(PlayerUser);

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		OtherUser.verifyUser();
		userService.addUser(OtherUser);

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				PlayerUser.getEmail());
	}
	
	//4.6 Scenario: Player changes other moderator user
	@Test(expected = RuntimeException.class)
	public void changeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsModerator() {

		UserEntity PlayerUser = new UserEntity("userTest", Constants.EMAIL_FOR_TESTS, "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		PlayerUser.verifyUser();
		userService.addUser(PlayerUser);

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,",
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		OtherUser.verifyUser();
		userService.addUser(OtherUser);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				PlayerUser.getEmail());
	}

	// url #4 /playground/users/{playground}/{email} test finished
	// ******************************************************************************************//
}
