package playground.layout;

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
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ActivityTest {
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
	//******************************************************************************************//

	}
	
	


	