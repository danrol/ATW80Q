package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import playground.*;
import playground.exceptions.ConfirmException;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestEdenDupontController {
	
	private RestTemplate restTemplate;
	
	@Autowired
	ElementService elementService;
	@Autowired
	UserService userService;
	
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

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
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
		UserTO user;
		try {
			user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","");

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
	public void testConfirmUserEmailNotInDatabase() {
		/*
		 * 				Given Server is up 
				AND  
				I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is not on the database
				Then I get a Wrong email message
		 */
		 UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1234");

			
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
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTestPlayground@gmail.com","1234");	
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
		
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
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
		u.verifyUser();
		this.userService.addUser(u);
		
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(4,3));
		this.elementService.addElement(element);
		
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}



	
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
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	
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
}
