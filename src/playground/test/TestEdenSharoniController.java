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
import playground.logic.UserTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestEdenSharoniController {

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
	public void testRegisterNewUser() {

	}

	@Test(expected = RuntimeException.class)
	public void testLoginUserWithNullEmail() {
		/*
		 * Given: GET /playground/users/login/{playground}/
		 * When:  User is verified AND is in database AND email is " " 
		 * Then: I get mail messege.
		 */
		UserTO user = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		// given database contains user { "user": "userTest"}
		this.db.addUser(user);
		user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class, Constants.PLAYGROUND_NAME, " ");
	}

	@Test
	public void testLoginUserWithCorrectEmail() {
		/*
		 * Given: GET /playground/users/login/{playground}/{email}
		 * When: user is in database and is verified
		 * Then: User gets Logged in
		 */
		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		// When I invoke GET this.url +"/playground/users/login/{playground}/{email}"
		u = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");
		// verify that unverified user is now verified
		assertThat(u).isNotNull();
		assertThat(u.isVerified()).isTrue();
	}

	@Test(expected = RuntimeException.class)
	public void testLoginUserEmailNotInDatabase() {
		/*
		 * Given: GET /playground/users/login/{playground}/{email}
		 * When: email is not on the database
		 * Then: I get email message
		 */
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

	@Test(expected = RuntimeException.class)
	public void LoginUserNotInPlayground() {
		/*
		 * Given: GET /playground/users/login/{playground}/{email}
		 * When: email is on the database and verified and user does not belong to playground
		 * Then: I get a user is not on playground message
		 */
		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
				"OtherPlayground");
		// given database contains user { "user": "userTest"}
		u.verifyUser();
		this.db.addUser(u);
		u = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

	@Test(expected = RuntimeException.class)
	public void testLoginUserWhenUserNotVerification() {
		/*
		 * Given: GET /playground/users/login/{playground}/{email}
		 * When: email is on the database AND not verificate
		 * Then: I get a not verified email message
		 */
		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		// given database contains user { "user": "userTest"}
		this.db.addUser(u);
		// When I invoke GET this.url +
		// "/playground/users/login/{playground}/{email}"
		u = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, "userTest@gmail.com");
	}

}
