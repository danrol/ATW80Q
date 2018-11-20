package playground.test;

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
import playground.*;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.ConfirmException;
import playground.logic.UserTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestEdenDupontController {
	
	private RestTemplate restTemplate;
	
	@Autowired
	Database db;
	
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
		db.cleanDatabase();
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
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		
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
		UserTO u = new UserTO("userTest","userTestPlayground@gmail.com","Test.jpg", Constants.MODERATOR_ROLE ,"OtherPlayground", "1234");
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		u = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTestPlayground@gmail.com","1234");

			
	}
	
	
	
	@Test (expected=RuntimeException.class)
	public void testConfirmUserWithIncorrectVerificationCode() {
		/*		Given Server is up 
				AND 
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database AND code is wrong
				Then I get a Wrong verification code error message
				*/
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}", UserTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com","1");
	
	}
	/*





	Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
	When user login details are correct and element is not in database
	Then I get an exception
*/
	@Test
	public void testGetElementCorrectLoginElementExists() {
		/*
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are correct and element exists
		Then I get the element
		*/
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		ElementTO element = new ElementTO("elementIdTest",Constants.PLAYGROUND_NAME,Constants.PLAYGROUND_NAME,"elementTest@gmail.com");
		this.db.addUser(u);
		this.db.addElement(element);
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTest@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
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
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		ElementTO element = new ElementTO("elementIdTest",Constants.PLAYGROUND_NAME,Constants.PLAYGROUND_NAME,"elementTest@gmail.com");
		this.db.addUser(u);
		this.db.addElement(element);
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
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		ElementTO element = new ElementTO("elementIdTest",Constants.PLAYGROUND_NAME,Constants.PLAYGROUND_NAME,"elementTest@gmail.com");
		this.db.addUser(u);
		this.db.addElement(element);
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
		
	}
	
	
}
