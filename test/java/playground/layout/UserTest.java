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
		/**
		    When: I POST /playground/users
				With headers: Accept:application/json, content-type: application/json
				With "email" : "WrongEmail” in NewUserForm body
			Then: RuntimeException appears
		 **/
		NewUserForm postUserForm = new NewUserForm("WrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		new UserTO(new UserEntity(
				postUserForm.getUsername(), postUserForm.getUsername(), postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));		
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{
		/**
	    	When: I POST /playground/users
				With headers:Accept:application/json,  content-type: application/json
				With Body:{"email" : "nudnik@mail.ru", "username":"Curiosity", "avatar":"ava", "role":"PLAYER"}
			Then: the response body contains new UserTO with the "email" : "nudnik@mail.ru", “username”: “Curiosity”, "avatar:"ava", "role":"PLAYER""
				AND database contains new UserTO with the same fields as in the body
	    **/
		NewUserForm postUserForm = new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO testValue = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(), 
				postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));	
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, UserTO.class);
		System.out.println("users after user added"+this.userService.getUsers().toString());
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualToComparingFieldByField(testValue);
	}
	
	@Test(expected=RuntimeException.class)
	public void testRegisterUserThatAlreadyExists() {
		/**
	    	Given: user with email “nudnik@mail.ru” and with playground=”randomPlayground” exists in userService
			When: I POST /playground/users
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : "nudnik@mail.ru", "username":"Curiosity", "avatar":"ava", "role":"PLAYER",}
			Then: a null user is returned in JSON
	    **/
		NewUserForm postUserForm =  new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm.getUsername(), postUserForm.getEmail(), 
				postUserForm.getAvatar(), postUserForm.getRole(), Constants.PLAYGROUND_NAME));
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
		/**
		 	Given: Email is not on the database
			When:   I GET /playground/users/confirm/playground.rolnik/mail@test.com/1234
					With headers: Accept:application/json Content-Type: application/json
			Then: the response status is <> 2xx
		 **/
		 this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1234");
	}
	
	//DELETED FROM GERKIN
	/**@Test
	public void testConfirmUserWithNullCode() {
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
		
	}**/
	
	@Test
	public void testConfirmUserWithCorrectCode() {
		/**
		 	Given: database contains a User with mail test@test.com in playground.rolnik with a generated verificationCode
			When: 	I GET /playground/users/confirm/playground.rolnik/test@test.com/{code}AND {code} is verificationCode
			Then: the retrieved UserTO email and playground matches test@test.com and playground.rolnik with a null verificationCode
		 **/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1234");
		//verify that unverified user is now verified
		assertThat(user).isNotNull();
		
	}
	
	@Test(expected=RuntimeException.class)
	public void ConfirmUserNotInPlayground() {
		/**
		 	Given: database contains a User with mail test@test.com in playground.rolnik with a generated verificationCode
			When: 	I GET /playground/users/confirm/playground.other/test@test.com/{code} AND {code} is verificationCode
			Then: the response status is <> 2xx
		 **/
		UserEntity u = new UserEntity("userTest","userTestPlayground@gmail.com","Test.jpg", Constants.MODERATOR_ROLE ,"OtherPlayground", "1234");
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTestPlayground@gmail.com","1234");	
	}
	
	
	
	@Test (expected=RuntimeException.class)
	public void testConfirmUserWithIncorrectVerificationCode() {
		/**
			Given: database contains a User with mail test@test.com in playground.rolnik with a generated verificationCode
			When: 	I GET /playground/users/confirm/playground.other/test@test.com/{code} AND {code} is retrieved from the  verificationCode+”X”
			Then: the response status is <> 2xx
		 **/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1");
//		assertThat(user.getVerified_user()).isEqualTo(Constants.USER_NOT_VERIFIED); //TODO remove verified
	}
	
	// url #2 /playground/users/confirm/{playground}/{email}/{code} test finished
	
	//******************************************************************************************//
	// url #3 /playground/users/login/{playground}/{email} tests started
	
	//DELETED FROM GERKIN
	/**@Test(expected = RuntimeException.class)
	public void testLoginUserWithNullEmail() {
		UserEntity user = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		// given database contains user { "user": "userTest"}
		this.userService.addUser(user);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, " ");
	}**/

	@Test
	public void testLoginUserWithCorrectEmail() {
		/**
		 	Given: database contains a User with mail test@test.com in playground.rolnik AND User is verified
			When: I GET /playground/users/login/playground.rolnik/test@test.com
			Then: The retrieved UserTO email matches test@test.com and playground matches playground.rolnik.
		 **/
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url +"/playground/users/login/{playground}/{email}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,	Constants.PLAYGROUND_NAME, "userTest@gmail.com");
		// verify that unverified user is now verified
		assertThat(user).isNotNull();
//		assertThat(user.isVerified()).isTrue();
	}
	

	@Test(expected = RuntimeException.class)
	public void testLoginUserEmailNotInDatabase() {
		/**
		 	Given: Database does not contain User with email test@test.com
			When: I GET /playground/users/login/playground.rolnik/test@test.com
			Then: The response status is <> 2xx
		 **/
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

	@Test(expected = RuntimeException.class)
	public void LoginUserNotInPlayground() {
		/**
		 	Given: User is verified in database with email: test@test.com AND playground is playground.other AND User is verified.
			When: I GET /playground/users/login/playground.other/test@test.com
			Then: The response status is <> 2xx 
		 **/
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE, "OtherPlayground");
		// given database contains user { "user": "userTest"}
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

	/**
	@Test(expected = RuntimeException.class)
	public void testLoginUserWhenUserNotVerification() {
		
		 	Given: User in database with email: test@test.com AND playground is playground.rolnik AND User is not verified 
			When: I GET /playground/users/login/playground.rolnik/test@test.com
			Then: The response status is <> 2xx
		 
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		System.err.println(u.isVerified());
		// given database contains user { "user": "userTest"}
		this.userService.addUser(u);
		// When I invoke GET this.url +
		// "/playground/users/login/{playground}/{email}"
		this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,	Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}
	**/
	// url #3/playground/users/login/{playground}/{email} test finished
	//******************************************************************************************//
	// url #4 /playground/users/{playground}/{email} test starts
	
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeHisUser() {
		/**
		 	Given: USER is moderator AND is verified in the database with email moderatorTest@test.com in playground.rolnik AND USER update his own information
			When: I PUT /playground/users/playground.rolnik/moderatorTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : moderatorTest@test.com", "username":ModeratorName, "avatar":MyAvatar.jpg, "role":MODERATOR, “playground”:playground.rolnik}
			Then: The response body contains moderatorTest@test.com USER with updates.
		 **/
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {
		/**
		 	Given: USER is moderator AND is verified in the database with email 	
				moderatorTest@test.com in playground.rolnik AND USER update other moderator 
				USER that is verified in the database with email otherModeratorTest@test.com in playground.rolnik
			When: I PUT /playground/otherModeratorUsers/playground.rolnik/moderatorTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : otherModeratorTest@test.com", "username":OtherModeratorName, "avatar":MyAvatar.jpg, "role":MODERATOR, “playground”:playground.rolnik}
			Then: the response status is <> 2xx
		 **/
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();
		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,	Constants.PLAYGROUND_NAME);
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {
		/**
		 	Given: USER is moderator AND is verified in the database with email moderatorTest@test.com in playground.rolnik AND USER update other player
				USER that is verified in the database with email otherPlayerTest@test.com in playground.rolnik
			When: I PUT /playground/otherPlayerUsers/playground.rolnik/moderatorTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : otherPlayerTest@test.com", "username":OtherPlayerName, "avatar":MyAvatar.jpg, "role":PLAYER, “playground”:playground.rolnik}
			Then: The response body contains otherPlayerTest@test.com USER with updates.
		 **/
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsPlayerAndChangeHisUser() {
		/**
		  	Given: USER is player AND is verified in the database with email playerTest@test.com in playground.rolnik AND USER update his own information
			When: I PUT /playground/users/playground.rolnik/playerTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : playerTest@test.com", "username":PlayerName, "avatar":MyAvatar.jpg,"role":PLAYER, “playground”:playground.rolnik}
			Then: The response body contains playerTest@test.com USER with updates.
		 **/
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", PlayerUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsPlayer() {
		/**
		  	Given: USER role is player AND is verified in the database with email playerTest@test.com in playground.rolnik AND USER update other player
				USER that is verified in the database with email otherPlayerTest@test.com in playground.rolnik
			When: I PUT /playground/otherPlayerUsers/playground.rolnik/playerTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : otherPlayerTest@test.com", "username":OtherPlayerName, "avatar":MyAvatar.jpg, "role":PLAYER, “playground”:playground.rolnik}
			Then: the response status is <> 2xx
		 **/
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}
	
	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsModerator() {
		/**
		 	Given: USER is player AND is verified in the database with email playerTest@test.com in playground.rolnik AND USER update other player
				USER that is verified in the database with email otherModeratorTest@test.com in playground.rolnik
			When: I PUT /playground/otherPlayerModeratorUsers/playground.rolnik/otherModeratorTest@test.com
				With headers:	  Accept:application/json,  content-type: application/json
				With Body:{"email" : otherModeratorTest@test.com", "username":OtherModeratorName, "avatar":MyAvatar.jpg, "role":MODERATOR, “playground”:playground.rolnik}
			Then: the response status is <> 2xx
		 **/
		UserEntity PlayerUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,	Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, PlayerUser.getEmail());
	}
	
	// url #4 /playground/users/{playground}/{email} test finished
	//******************************************************************************************//
	}
	
	

