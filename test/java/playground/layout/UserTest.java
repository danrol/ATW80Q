package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import playground.Constants;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserTest {
private RestTemplate restTemplate;
	
	private ElementService elementService;
	private UserService userService;
	
	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@LocalServerPort
	private int port;
	private String url;
	
//	@Autowired
//	private Database database;


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
	@Test(expected=RuntimeException.class)
	public void testRegisterNewUserWithWrongEmail() throws JsonProcessingException{
		//Test #1.1
		
		NewUserForm postUserForm = new NewUserForm("WrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		new UserTO(new UserEntity(postUserForm));		
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{

		//Test #1.2
		NewUserForm postUserForm = new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO testValue = new UserTO(new UserEntity(postUserForm));
		
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, UserTO.class);
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualToComparingFieldByField(testValue);
	}
	
	@Test(expected=RuntimeException.class)
	public void testRegisterUserThatAlreadyExists() {
		//Test #1.3
		NewUserForm postUserForm =  new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm));
		userService.addUser(userToAdd.toEntity());
		UserTO actualReturnedValue = this.restTemplate.postForObject(
				this.url+"/playground/users", postUserForm, UserTO.class);
		assertThat(actualReturnedValue).isNull();
	}
	

	
	// url #1 playground/users tests finished
	
	//******************************************************************************************//
	// url #2 /playground/users/confirm/{playground}/{email}/{code} test starts
	@Test(expected=RuntimeException.class)
	public void testConfirmUserEmailNotInDatabase() {
		/*
		 * 				Given Server is up 
				AND  
				I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is not on the database
				Then I get a Wrong email message
		 */
		 this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1234");

			
	}
	
	@Test
	public void testConfirmUserWithNullCode() {
		/*
		 * 
		Given Server is up 
		AND 
		 I GET /playground/users/confirm/{playground}/{email}/
		When email is on the database AND code is ""
		Then I get a 404 exception
		 * */
		String[] s = {"0","0"};
		
		try {
			 this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","");

		}
		catch(RuntimeException e)
		{
			s=e.toString().split(" ", 3);
		}
		System.err.println(s[1]);
		assertThat(s[1]).isEqualTo("404");
		
	}
	
	@Test
	public void testConfirmUserWithCorrectCode() {
		/*
		 * 
				Given Server is up 
				AND 
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database and code is correct and user belongs to playground
				Then I get a verified user message
				
		 * */
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1234");
		//verify that unverified user is now verified
		assertThat(user).isNotNull();
		assertThat(user.isVerified()).isTrue();
		
	}
	
	@Test(expected=RuntimeException.class)
	public void ConfirmUserNotInPlayground() {
		/*
		 * 		Given Server is up 
				AND  
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database and code is correct and user does not belong to playground
				Then I get a user is not on playground message
		 * */
		UserEntity u = new UserEntity("userTest","userTestPlayground@gmail.com","Test.jpg", Constants.MODERATOR_ROLE ,"OtherPlayground", "1234");
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTestPlayground@gmail.com","1234");	
	}
	
	
	
	@Test (expected=RuntimeException.class)
	public void testConfirmUserWithIncorrectVerificationCode() {
		/*		Given Server is up 
				AND 
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database AND code is wrong
				Then I get a Wrong verification code error message
				*/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1");
		assertThat(user.getVerified_user()).isEqualTo(Constants.USER_NOT_VERIFIED);
	}
	
	// url #2 /playground/users/confirm/{playground}/{email}/{code} test finished
	
	//******************************************************************************************//
	// url #3 /playground/users/login/{playground}/{email} tests started
	
	@Test(expected = RuntimeException.class)
	public void testLoginUserWithNullEmail() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/
		 * When: User is verified AND is in database AND email is empty
		 * Then: I get login exception.
		 */
		UserEntity user = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		// given database contains user { "user": "userTest"}
		this.userService.addUser(user);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, " ");
	}

	@Test
	public void testLoginUserWithCorrectEmail() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: user is in playground database and is verified
		 * Then: User gets Logged in
		 */
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url +"/playground/users/login/{playground}/{email}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,	Constants.PLAYGROUND_NAME, "userTest@gmail.com");
		// verify that unverified user is now verified
		assertThat(user).isNotNull();
		assertThat(user.isVerified()).isTrue();
	}
	

	@Test(expected = RuntimeException.class)
	public void testLoginUserEmailNotInDatabase() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is not on the database
		 * Then: I get login exception.
		 */
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

	@Test(expected = RuntimeException.class)
	public void LoginUserNotInPlayground() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is on the database and verified and user does not belong to playground
		 * Then: I get a user is not on playground message
		 */
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE, "OtherPlayground");
		// given database contains user { "user": "userTest"}
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

	@Test(expected = RuntimeException.class)
	public void testLoginUserWhenUserNotVerification() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is on the database AND not verified
		 * Then: I get login exception.
		 */
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url +
		// "/playground/users/login/{playground}/{email}"
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,	Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}
	
	// url #3/playground/users/login/{playground}/{email} test finished
	//******************************************************************************************//
	// url #4 /playground/users/{playground}/{email} test starts
	@Test
	public void testChangeUserWhenRoleIsPlayerAndChangeHisUser() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am Player AND want to update my user
		 * Then: changes are accepted
		 */
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", PlayerUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsPlayer() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am Player AND want to update other user AND other user is player
		 * Then: I get changesUser exception
		 */
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}
	
	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsModerator() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am Player AND want to update other user AND other user is moderator
		 * Then: I get changesUser exception
		 */
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,	Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}
	
	// url #4 /playground/users/{playground}/{email} test finished
	//******************************************************************************************//
	}
	
	

