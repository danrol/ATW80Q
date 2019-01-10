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
import playground.constants.Playground;
import playground.constants.User;
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
	
	//1.1 Scenario: Test register with wrong email (no �@� and web address afterwards) 
	@Test(expected = RuntimeException.class)
	public void registerNewUserWithWrongEmail(){

		NewUserForm postUserForm = new NewUserForm("WrongEmail", User.DEFAULT_USERNAME, User.AVATAR_FOR_TESTS, User.PLAYER_ROLE);
		this.restTemplate.postForObject(this.url + Playground.Function_1 , postUserForm, UserTO.class);
	}

	//1.2 Scenario: Test successful register 
	@Test
	public void successfullyRegisterNewUser() {
		
		NewUserForm postUserForm = new NewUserForm(User.EMAIL_FOR_TESTS, User.DEFAULT_USERNAME, User.AVATAR_FOR_TESTS, User.PLAYER_ROLE);
		UserTO testValue = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(), postUserForm.getAvatar(), postUserForm.getRole(), Playground.PLAYGROUND_NAME));
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url + Playground.Function_1, postUserForm,	UserTO.class);		
		assertThat(actualReturnedValue).isNotNull().isEqualToComparingFieldByField(testValue);
	}
	
	//1.3 Scenario: Test register user with the same email as the one that another user already have in the database
	@Test(expected = RuntimeException.class)
	public void registerUserThatAlreadyExists() {

		NewUserForm postUserForm = new NewUserForm(User.EMAIL_FOR_TESTS, User.DEFAULT_USERNAME, User.AVATAR_FOR_TESTS, User.PLAYER_ROLE);
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(), postUserForm.getAvatar(), postUserForm.getRole(), Playground.PLAYGROUND_NAME));
		userService.addUser(userToAdd.toEntity());
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url + Playground.Function_1, postUserForm, UserTO.class);
		assertThat(actualReturnedValue).isNull();
	}

	// url #1 playground/users tests finished

	// ******************************************************************************************//
	// url #2 /playground/users/confirm/{playground}/{email}/{code} test starts
	
	//2.1 Scenario: Email not registered.
	@Test(expected = RuntimeException.class)
	public void confirmUserEmailNotInDatabase() {

		this.restTemplate.getForObject(this.url + Playground.Function_2, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, User.DEFAULT_VERIFICATION_CODE);
	}
	
	//2.2 Scenario : Successful confirmation
	@Test
	public void confirmUserWithCorrectCode() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + Playground.Function_2, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, u.getVerificationCode());
		assertThat(user).isNotNull();

	}
	
	//2.3 Scenario: Playground doesn�t match user 
	@Test(expected = RuntimeException.class)
	public void confirmUserNotInPlayground() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Playground.Function_2, UserTO.class, Playground.Other_Playground, User.EMAIL_FOR_TESTS, u.getVerificationCode());
	}
	
	//2.4 Scenario: Email is registered but verification code is wrong
	@Test(expected = RuntimeException.class)
	public void confirmUserWithIncorrectVerificationCode() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		String code = u.getVerificationCode();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Playground.Function_2, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, code + "x");
	}

	// url #2 /playground/users/confirm/{playground}/{email}/{code} test finished

	// ******************************************************************************************//
	// url #3 /playground/users/login/{playground}/{email} tests started
	//3.1 Scenario: Successful Login
	@Test
	public void loginUserWithCorrectEmail() {
		
		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		UserTO user = this.restTemplate.getForObject(this.url + Playground.Function_3, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
		assertThat(user).isNotNull();
	}
	
	//3.2 Scenario: Email not in Database
	@Test(expected = RuntimeException.class)
	public void loginUserEmailNotInDatabase() {
		
		this.restTemplate.getForObject(this.url + Playground.Function_3, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
	}
	
	//3.3 Scenario: User doesn�t belong in playground
	@Test(expected = RuntimeException.class)
	public void loginUserNotInPlayground() {
		
		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.Other_Playground);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Playground.Function_3, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
	}
	
	//3.4 Scenario: User not verified
	@Test(expected=RuntimeException.class)
	public void loginUserWhenUserNotVerified() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		assertThat(u.isVerified()).isFalse();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Playground.Function_3, UserTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
	}

	// url #3/playground/users/login/{playground}/{email} test finished
	// ******************************************************************************************//
	// url #4 /playground/users/{playground}/{email} test starts
	
	//4.1 Scenario: Manager changes his user
	@Test
	public void UserUpdateHisInfo() {

		UserEntity managerUser = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		managerUser.verifyUser();
		this.userService.addUser(managerUser);
		managerUser.setAvatar("name");
		this.restTemplate.put(this.url + Playground.Function_4, new UserTO(managerUser), managerUser.getPlayground(), managerUser.getEmail());
		
		UserEntity t = userService.getUser(managerUser.getPlayground(), managerUser.getEmail());

		assertThat(managerUser).isEqualTo(t);
		}
	
	//4.2 Scenario: manager changes other manager user
	@Test(expected = RuntimeException.class)
	public void UserUpdateAnotherUser() {

		UserEntity managerUser = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		managerUser.verifyUser();
		this.userService.addUser(managerUser);
		
		UserEntity otherUser = new UserEntity(User.DEFAULT_USERNAME, "other"+User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		otherUser.verifyUser();
		otherUser = userService.addUser(otherUser);
		
		this.restTemplate.put(this.url + Playground.Function_4, new UserTO(otherUser), managerUser.getPlayground(), managerUser.getEmail());		
	}
		
	
	// url #4 /playground/users/{playground}/{email} test finished
	// ******************************************************************************************//
	
}
