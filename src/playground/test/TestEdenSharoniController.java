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

	@Test
	public void testLoginUserWithNullEmail() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/ When:
		 * email is "" Then: I get a 404 exception
		 */
		String[] s = { "0", "0" };
		UserTO user;
		try {
			user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
					UserTO.class, Constants.PLAYGROUND_NAME, "");

		} catch (RuntimeException e) {
			s = e.toString().split(" ", 3);
		}
		System.err.println(s[1]);
		assertThat(s[1]).isEqualTo("404");

	}

	@Test
	public void testLoginUserWithCorrectEmail() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: user is in database and is verified Then: User gets Logged in
		 */

		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		u.verifyUser();

		// given database contains user { "user": "userTest"}
		this.db.addUser(u);

		// When I invoke GET this.url +"/playground/users/login/{playground}/{email}"
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");
		// verify that unverified user is now verified
		assertThat(user).isNotNull();
		assertThat(user.isVerified()).isTrue();
	}

	@Test(expected = RuntimeException.class)
	public void testLoginUserEmailNotInDatabase() {
		/*
		 * Given: Server is up AND I GET
		 * /playground/users/login/{playground}/{email}/{code} When: email is not on the
		 * database Then: I get a Wrong email message
		 */
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

//	@Test(expected = RuntimeException.class)
//	public void LoginUserNotInPlayground() {
//		/*
//		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
//		 * When: email is on the database and code is correct and user does not belong to
//		 * playground Then: I get a user is not on playground message
//		 */
//		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
//				"OtherPlayground");
//		// given database contains user { "user": "userTest"}
//		this.db.addUser(u);
//		u = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}/{code}",
//				UserTO.class, Constants.PLAYGROUND_NAME, "userTestPlayground@gmail.com", "1234");
//
//	}

//	@Test(expected = RuntimeException.class)
//	public void testConfirmUserWithIncorrectVerificationCode() {
//		/*
//		 * Given Server is up AND I GET
//		 * /playground/users/login/{playground}/{email}/{code} When email is on the
//		 * database AND code is wrong Then I get a Wrong verification code error message
//		 */
//		UserTO u = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
//				Constants.PLAYGROUND_NAME, "1234");
//
//		// given database contains user { "user": "userTest"}
//		this.db.addUser(u);
//
//		// When I invoke GET this.url +
//		// "/playground/users/login/{playground}/{email}/{code}"
//		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/confirm/{playground}/{email}/{code}",
//				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com", "1");
//
//	}

}
