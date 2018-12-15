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
import com.fasterxml.jackson.core.JsonProcessingException;
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
	@Test(expected = RuntimeException.class)
	public void RegisterNewUserWithWrongEmail() {
		
		NewUserForm postUserForm = new NewUserForm("WrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS,
				Constants.PLAYER_ROLE);
		new UserEntity(postUserForm.getUsername(), postUserForm.getUsername(), postUserForm.getAvatar(),
				postUserForm.getRole(), Constants.PLAYGROUND_NAME);
	}

	@Test
	public void SuccessfullyRegisterNewUser() {
		NewUserForm postUserForm = new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		
		UserTO testValue = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(),
				postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url + "/playground/users", postUserForm,
				UserTO.class);assertThat(actualReturnedValue).isNotNull().isEqualToComparingFieldByField(testValue);
	}

	@Test(expected = RuntimeException.class)
	public void RegisterUserThatAlreadyExists() {

		NewUserForm postUserForm = new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
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
	@Test(expected = RuntimeException.class)
	public void ConfirmUserEmailNotInDatabase() {

		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com", "1234");
	}

	@Test
	public void ConfirmUserWithCorrectCode() {

		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com", u.getVerificationCode());

		assertThat(user).isNotNull();

	}

	@Test(expected = RuntimeException.class)
	public void ConfirmUserNotInPlayground() {

		UserEntity u = new UserEntity("userTest", "userTestPlayground@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
				"OtherPlayground");
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTestPlayground@gmail.com", u.getVerificationCode());
	}

	@Test(expected = RuntimeException.class)
	public void ConfirmUserWithIncorrectVerificationCode() {

		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		String code = u.getVerificationCode();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com", code + "x");
	}

	// url #2 /playground/users/confirm/{playground}/{email}/{code} test finished

	// ******************************************************************************************//
	// url #3 /playground/users/login/{playground}/{email} tests started

	@Test
	public void LoginUserWithCorrectEmail() {
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");
		assertThat(user).isNotNull();
	}

	@Test(expected = RuntimeException.class)
	public void LoginUserEmailNotInDatabase() {
		UserEntity u = new UserEntity("userTest", "userTest2@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

	@Test(expected = RuntimeException.class)
	public void LoginUserNotInPlayground() {
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
				"OtherPlayground");
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

	@Test(expected = RuntimeException.class)
	public void LoginUserWhenUserNotverified() {

		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

	// url #3/playground/users/login/{playground}/{email} test finished
	// ******************************************************************************************//
	// url #4 /playground/users/{playground}/{email} test starts

	@Test
	public void ChangeUserWhenRoleIsModeratorAndChangeHisUser() {

		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,",
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		moderatorUser.verifyUser();
		userService.addUser(moderatorUser);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser,
				Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void ChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {

		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,",
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

	@Test
	public void ChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {

		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,",
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

	@Test
	public void ChangeUserWhenRoleIsPlayerAndChangeHisUser() {

		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		PlayerUser.verifyUser();
		userService.addUser(PlayerUser);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", PlayerUser,
				Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void ChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsPlayer() {

		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
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

	@Test(expected = RuntimeException.class)
	public void ChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsModerator() {

		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
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
