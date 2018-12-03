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
public class WebUITest {
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
	
	// url #1 /playground/users tests start
	@Test(expected=RuntimeException.class)
	public void testRegisterNewUserWithWrongEmail() throws JsonProcessingException{
/*		
		Given Server is up
		When I POST /playground/users
		With headers: Accept:application/json, content-type: application/json
		With "email" : "WrongEmail” in NewUserForm body
		Then RuntimeException appears
*/
		
		NewUserForm postUserForm = new NewUserForm("WrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		new UserTO(new UserEntity(postUserForm));		
	}
	
	@Test(expected=RuntimeException.class)
	public void testRegisterUserThatAlreadyExists() {
/*		
		Given Server is up  
		When I POST /playground/users 
		AND user exists in the database
		Then a null user is returned in JSON
*/
		NewUserForm postUserForm =  new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm));
		userService.addUser(userToAdd.toEntity());
		UserTO actualReturnedValue = this.restTemplate.postForObject(
				this.url+"/playground/users", postUserForm, UserTO.class);
		assertThat(actualReturnedValue).isNull();
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{
/*		
		Given Server is up
		When I POST /playground/users
		With headers:	  Accept:application/json,  content-type: application/json
		With Body:
		{"email" : "nudnik@mail.ru", "username":"Curiosity", "avatar":"ava", "role":"PLAYER"}
		Then the response body contains new UserTO with the "email" : "nudnik@mail.ru", “username”: “Curiosity”, "avatar:"ava", "role":"PLAYER""
		AND 
		database contains new UserTO with the same fields as in the body
*/

		NewUserForm postUserForm = new NewUserForm("nudnik@mail.ru", "Curiosity", "ava", "PLAYER");
		UserTO testValue = new UserTO(new UserEntity(postUserForm));
		
		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, UserTO.class);
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualToComparingFieldByField(testValue);
	}
	
	// url #1 playground/users tests finished
	
	
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
	
	// url #5 /playground/elements/{userPlayground }/{email}  starts 
	@Test
	public void testPOSTNewElementIsAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		elementService.addElement(element);
		ArrayList <ElementEntity> arr= elementService.getElements();
		
		assertThat(arr.contains(element)).isTrue();
	}
	
	@Test
	public void testPOSTNewElementWithNoCreatorIsAdded() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element with empty creatorPlayground.
		 * Then: a new element is saved in the serviceElement.
		 */
		String playground="playground",creatorPlayground=" ",name="nameOfElement:(english hei 7)";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		
		elementService.addElement(element);
		assertThat(elementService.getElements().contains(element)).isTrue();
	}
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeHisUser() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update my user
		 * Then: changes are accepted
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update other user AND other user is moderator
		 * Then: I get changeUser exception
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,	Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update other user AND other user is player
		 * Then: changes are accepted
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	// url #5 /playground/elements/{userPlayground }/{email}  finished

	
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with PUT test starts
	@Test(expected = RuntimeException.class)
	public void testWrongElementPassedForUpdate() {
		
		ElementEntity elementEntityForTest = 
				new ElementEntity(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS,new Location(0,1));
		ElementTO elementForTest = new ElementTO(elementEntityForTest);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, 
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.ID_FOR_TESTS);
	}
	
	@Test
	public void testSuccessfullyUpdateElement() throws Exception{
/*		
		Given Server is up
		AND database contains element with fields user playground "User playgrouNd", playground = "User playgrouNd", id = “123”, creator email = “email@email.com” 
		AND database contains verified user with "email@email.com"
		When I PUT /playground/elements/User playgrouNd/email@email.com/"User playgrouNd"/”123”
			With headers: Accept:application/json, content-type: application/json
			And element in body contains fields user playground "User playgrouNd", playground = "for test", id = “123”, creator email = “email@email.com” 
		Then original element’s playground will be updated with “for test” 
*/
		
		UserEntity userElementCreator = new UserEntity("username", "email@email.com", "ava", 
				"PLAYER", "User playgrouNd");
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = 
				new ElementEntity("123", "User playgrouNd", "email@email.com",new Location(0,1));
		elementService.addElement(updatedElementForTestEntity);
		
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("for test");
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTestTO, "User playgrouNd", 
				"email@email.com", "User playgrouNd",  "123");
		
		ElementEntity actualEntity = elementService.getElement("123", "for test");

		
		assertThat(actualEntity).isNotNull();

		assertThat(actualEntity).isEqualToComparingFieldByField(updatedElementForTestTO.toEntity());
		}
	
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with PUT test finished
	
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test started
	
	@Test(expected=RuntimeException.class)
	public void testGetElementCorrectLoginElementNotInDatabase() {
	/*
	 * 
	Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
	When user login details are correct and element is not in database
	Then I get an exception
		
	*/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	
	@Test
	public void testGetElementCorrectLoginElementExists() {
		/*
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are correct and element exists
		Then I get the element
		*/
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME,Constants.EMAIL_FOR_TESTS,Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME);
		u.verifyUser();
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(1,7));
		this.userService.addUser(u);
		this.elementService.addElement(element);
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,Constants.EMAIL_FOR_TESTS,Constants.PLAYGROUND_NAME,"elementIdTest");
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	@Test(expected=RuntimeException.class)
	public void testGetElementIncorrectLoginElementExists() {
		/*
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are incorrect and element exists
		Then I get an exception
		*/
		
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(5,7));
		this.elementService.addElement(element);
		
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	

	@Test(expected=RuntimeException.class)
	public void testGetElementIncorrectLoginElementNotInDatabase() {
	/*
	 * 
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are incorrect and element is not in database
		Then I get an exception
		
	*/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.setVerified_user(Constants.USER_NOT_VERIFIED);
		this.userService.addUser(u);
		
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(4,3));
		this.elementService.addElement(element);
		
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test finished
	

	// url #8 /playground/elements/{userPlayground }/{email}/all test started
	@Test
	public void testPOSTNewElementsAreAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementTO[] arrElements=new ElementTO[3];
		arrElements[0]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,1")));
		arrElements[1]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,2")));
		arrElements[2]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,3")));
		
		
		elementService.addElements(arrElements, playground);
		
		
		
	}
	
	@Test
	public void testPOSTNewElementsWithSameFieldsAreNotAddedDuplicatedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementTO[] arrElements=new ElementTO[3];
		arrElements[0]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,1")));
		arrElements[1]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,1")));
		arrElements[2]=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("3,3")));
		
		
		elementService.addElements(arrElements, playground);
		//todo func in dummyElementService that check if element in database
		
	}
	// url #8 /playground/elements/{userPlayground }/{email}/all test finished

	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance} test started
	@Test
	public void testIfWeGETElementsFromDatabaseWithRightRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is above 0.
		 * Then: I get  ElementTO[] back.
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementTO element1=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		ElementTO element2=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("2,1")));
		elementService.addElement(element1.toEntity());
		double distance=7;
		assertThat(elementService.getAllElementsTOInRadius(element2,element2.getLocation().getX(),element2.getLocation().getY(),distance, 0, 10)).isNotNull();
	}
	
	@Test(expected=RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is negative.
		 * Then: I get NULL ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=-1;
		elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10);
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is 0.
		 * Then: I get NULL ElementTO[].
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=0;
		assertThat(elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10)).isNull();
	}
	
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance} test finished



	// url #10 "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" test starts
	@Test
	public void testAttributeNotExist() {
		/*		
		Given: the server is up
		AND element service doesn't contains element with attributeName = “noSuchAttribute”
		With headers:	  Accept:application/json,  content-type: application/json
		When: I GET /playground/elements/creatorPlayground/nudnik@mail.ru/search/attr3/attr3Val
		Then: server returns  emtpty ElementTO[] array with element with creatorEmail = "nudnik@mail.ru",  value in attribute attributeName = “attr3”:attributeValue=”attr3Val”
*/
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", 
				"player", "creatorPlayground");
		
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity("123", 
				"userPlayground", "nudnik@mail.ru", new Location(1,0)))};
		
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				"creatorPlayground", "nudnik@mail.ru", "noSuchAttribute", "attr3Val");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}
	
	
	@Test
	public void testSuccessfullyGetElementsByAttributeNameValue(){
/*		
		Given: the server is up
		AND element service contains contains element with attributeName = “attr3”:attributeValue=”attr3Val”
		With headers:	  Accept:application/json,  content-type: application/json
		When: I GET /playground/elements/creatorPlayground/nudnik@mail.ru/search/attr3/attr3Val
		Then: server returns  ElementTO[] array with element with creatorEmail = "nudnik@mail.ru",  value in attribute attributeName = “attr3”:attributeValue=”attr3Val”
*/
		
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", 
				"player", "creatorPlayground");
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity("123", 
				"userPlayground", "nudnik@mail.ru", new Location(1,0)))};
		
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest[0].setAttributes(testMap);
		elementService.addElement(elementForTest[0].toEntity());
		
		ElementTO[] forNow = this.restTemplate.getForObject(url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class, 
				"creatorPlayground", "nudnik@mail.ru", "attr3", "attr3Val");
		
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToComparingFieldByField(elementForTest[0]);
	}
	// url #10 "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" test finished

	


	
	
	

	
	// url #11 /playground/activities/{userPlayground}/{email} started
	@Test
	public void testSendValidActivityToServer() {
	/*
	 * 
		Given the server is up and I POST /playground/activities/{userPlayground}/{email}
		When user login details are correct and activity is valid
		Then an Object is returned
	*/
		
		ActivityTO act = new ActivityTO();
		ActivityTO ob = this.restTemplate.postForObject(this.url + "/playground/activities/{userPlayground}/{email}", act, ActivityTO.class,Constants.PLAYGROUND_NAME,"Test@gmail.com");
		System.err.println(ob);
	}
	// url #11 /playground/activities/{userPlayground}/{email} finished

	}
	
	


	


	/*
	 * DanielController:
	 * 
	 * 1. Register new user
	 * "/playground/users" POST
	 * 
	 * 6. Update element
	 * "/playground/elements/{userPlayground}/{email}/{playground}/{id}" PUT
	 * 
	 * 10. Get element containing attribute with specific value
	 * "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" GET
	 * 
	 * 
	 * EdenDupontController
	 * 
	 * 2. Confirm User
	 * "/playground/users/confirm/{playground}/{email}/{code}" GET
	 * 
	 * 7. Get element
	 * "/playground/elements/{userPlayground}/{email}/{playground}/{id}" GET
	 * 
	 * 11. Request Server
	 * "/playground/activities/{userPlayground}/{email}" POST
	 * 
	 * EdenSharoniController
	 * 
	 * 
	 * */
