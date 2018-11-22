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
		 * Given: Server is up AND I GET /playground/users/login/{playground}/ When:
		 * User is verified AND is in database AND email is empty Then: I get login
		 * exception.
		 */
		UserTO user = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		user.verifyUser();
		// given database contains user { "user": "userTest"}
		this.db.addUser(user);
		user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}", UserTO.class,
				Constants.PLAYGROUND_NAME, " ");
	}

	@Test
	public void testLoginUserWithCorrectEmail() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: user is in playground database and is verified Then: User gets Logged
		 * in
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
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is not on the database Then: I get login exception.
		 */
		UserTO user = this.restTemplate.getForObject(this.url + "/playground/users/login/{playground}/{email}",
				UserTO.class, Constants.PLAYGROUND_NAME, "userTest@gmail.com");

	}

	@Test(expected = RuntimeException.class)
	public void LoginUserNotInPlayground() {
		/*
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is on the database and verified and user does not belong to
		 * playground Then: I get a user is not on playground message
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
		 * Given: Server is up AND I GET /playground/users/login/{playground}/{email}
		 * When: email is on the database AND not verified Then: I get login exception.
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

	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeHisUser() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email} When: I
		 * am moderator AND want to update my user Then: changes are accepted
		 */
		UserTO moderatorUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(moderatorUser);
		moderatorUser.verifyUser();

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser,
				Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email} When:
		 * I am moderator AND want to update other user AND other user is moderator Then: I get changeUser exception
		 */
		UserTO moderatorUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserTO OtherUser = new UserTO("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email} When:
		 * I am moderator AND want to update other user AND other user is player Then: changes are accepted
		 */
		UserTO moderatorUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserTO OtherUser = new UserTO("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsPlayerAndChangeHisUser() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email} When: I
		 * am Player AND want to update my user Then: changes are accepted
		 */
		UserTO PlayerUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(PlayerUser);
		PlayerUser.verifyUser();
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", PlayerUser, Constants.PLAYGROUND_NAME,
				PlayerUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsPlayer() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am Player AND want to update other user AND other user is player
		 * Then: I get changesUser exception
		 */
		UserTO PlayerUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserTO OtherUser = new UserTO("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				PlayerUser.getEmail());
	}
	
	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsPlayerAndChangeOtherUserAndOtherUserIsModerator() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am Player AND want to update other user AND other user is moderator
		 * Then: I get changesUser exception
		 */
		UserTO PlayerUser = new UserTO("userTest", "userTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE,
				Constants.PLAYGROUND_NAME);
		db.addUser(PlayerUser);
		PlayerUser.verifyUser();
		
		UserTO OtherUser = new UserTO("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		
		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME,
				PlayerUser.getEmail());
	}
}
