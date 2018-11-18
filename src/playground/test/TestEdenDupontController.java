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
import playground.logic.UserTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestEdenDupontController implements Playground_constants {
	
	private RestTemplate restTemplate;
	
	@Autowired
	Database db;
	
	@LocalServerPort
	private int port;
	private String url;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/";
		System.err.println(this.url);
	}
	
	@Before
	public void setup() {
		
	}

	@After
	public void teardown() {
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	

				/*
				 * 
				Given Server is up 
				AND  
				I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is not on the database
				Then I get a Wrong email message
				
				Given Server is up 
				AND 
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database AND code is wrong
				Then I get a Wrong verification code error message
				
				Given Server is up 
				AND 
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database and code is correct and user belongs to playground
				Then I get a verified user message
				
				Given Server is up 
				AND  
				 I GET /playground/users/confirm/{playground}/{email}/{code}
				When email is on the database and code is correct and user does not belong to playground
				Then I get a user is not on playground message
		
				 * */
	@Test
	public void testConfirmUserNullCode() {
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", MODERATOR_ROLE ,PLAYGROUND_NAME);
		
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/"+ PLAYGROUND_NAME +"/userTest@gmail.com/", UserTO.class, u);
		
		//verify that Message received applies to the following assertion: message.message is exactly "demo"
		assertThat(user).isNotNull();
		assertThat(user.isVerified());
		
	}
	
	@Test
	public void testConfirmUserWithCode() {
		UserTO u = new UserTO("userTest","userTest@gmail.com","Test.jpg,", MODERATOR_ROLE ,PLAYGROUND_NAME, "1234");
		
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		
		// When I invoke GET this.url + "/playground/users/confirm/{playground}/{email}/{code}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/"+ PLAYGROUND_NAME +"/userTest@gmail.com/1234", UserTO.class, u);
		
		//verify that Message received applies to the following assertion: message.message is exactly "demo"
		assertThat(user).isNotNull();
		assertThat(user.isVerified());
		
	}
	
}
