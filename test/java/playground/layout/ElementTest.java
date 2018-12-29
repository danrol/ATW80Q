package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import playground.Constants;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTest {
	private RestTemplate restTemplate;

	private ElementService elementService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

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

	// ******************************************************************************************//
	// url #5 /playground/elements/{userPlayground }/{email}

	// 5.1 Scenario: Test Save element in database
	@Test
	public void saveElementInDatabase() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE,Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, user.getPlayground(), user.getEmail(), Constants.Location_x, Constants.Location_y);
		ElementTO elemTO = this.restTemplate.postForObject(this.url + Constants.Function_5, new ElementTO(element), ElementTO.class, user.getPlayground(), user.getEmail());
		ElementEntity element2 = elemTO.toEntity();
		assertThat(element2).isEqualToIgnoringGivenFields(element, Constants.Ignore_id , Constants.Ignore_superkey);
	}

	// 5.2 Scenario: Saving an existing element
	@Test(expected = RuntimeException.class)
	public void saveAlreadyExistElement() {
		
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		element = elementService.addElementNoLogin(element);
		int dbSize = elementService.getAllElements().length;
		ElementTO elem = this.restTemplate.postForObject(this.url + Constants.Function_5, new ElementTO(element), ElementTO.class, userElementCreator.getPlayground(), userElementCreator.getEmail());
		assertThat(element).isEqualToIgnoringGivenFields(elem.toEntity(), Constants.Ignore_creationDate);
		assertThat(dbSize).isEqualTo(elementService.getAllElements().length);

	}

	/*******************************************************************************************************************************/
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test starts

	// 6.1 Scenario: Test successfully update element in database
	@Test
	public void successfullyUpdateElement() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Constants.ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(),elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setName("changedName");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO, userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
		ElementEntity actualEntity = elementService.getElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getSuperkey());
		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToIgnoringGivenFields(updatedElementForTestTO.toEntity(), Constants.Ignore_creationDate);
	}

	// 6.2 Scenario: Test update non-existent element
	@Test(expected = RuntimeException.class)
	public void updateNonExistingElement() {
		
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Constants.ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		ElementTO elementForTest = new ElementTO(elementForTestEntity);
		this.restTemplate.put(this.url + Constants.Function_6, elementForTest, userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// 6.3 Scenario : Test update existing element with non existing Creator email
	// in playground
	@Test(expected = RuntimeException.class)
	public void updateElementForNonExistingCreator() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.Other_Email_For_Test, Constants.Location_x, Constants.Location_y);
		elementService.addElementNoLogin(updatedElementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, updatedElementForTestEntity.getCreatorPlayground(), updatedElementForTestEntity.getId());
	}

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test finished
	// ******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test

	// 7.1 Scenario: Get element with incorrect login details
	@Test(expected = RuntimeException.class)
	public void GETElementIncorrectLoginElementExists() {

		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.Location_x, Constants.Location_y);
		this.elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, Constants.Other_Playground, u.getEmail(), element.getCreatorPlayground(), element.getId());
	}

	// 7.2 Scenario: Get element with correct login details, and element not in
	// database
	@Test(expected = RuntimeException.class)
	public void GETElementCorrectLoginElementNotInDatabase() {
		
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.ID_FOR_TESTS);
	}

	// 7.3 Scenario: Get Element with correct login details and element exists
	@Test
	public void GETElementCorrectLoginElementExists() {
		
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.Location_x, Constants.Location_y);
		this.elementService.addElementNoLogin(element);
		ElementTO el = this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class,	Constants.PLAYGROUND_NAME, u.getEmail(), element.getCreatorPlayground(), element.getId());
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test
	// finished

	// ******************************************************************************************//
	// url #8 /playground/elements/{userPlayground }/{email}/all test started

	// 8.1 Scenario: Test get all elements from database
	@Test
	public void GETAllFromDatabase() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elem1 = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 2);
		ElementEntity elem2 = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 2, 1);
		elem1 = elementService.addElementNoLogin(elem1);
		elem2 = elementService.addElementNoLogin(elem2);
		ElementTO[] arrForTest = new ElementTO[] { new ElementTO(elem1), new ElementTO(elem2) };
		ElementTO[] result = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class, Constants.EMAIL_FOR_TESTS,Constants.PLAYGROUND_NAME);
		assertThat(result).isNotNull();
		assertThat(result[0]).isEqualToComparingFieldByField(arrForTest[0]);
		assertThat(result[1]).isEqualToComparingFieldByField(arrForTest[1]);
	}

	// 8.2 Scenario: Test get all elements from empty database
	@Test
	public void GETAllFromEmptyDatabase() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elemArr = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class, Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME);
		assertThat(elemArr).isEqualTo(new ElementTO[0]);
	}

	// 8.3 Scenario: Not registered user fails to get all elements in the database
	@Test(expected = RuntimeException.class)
	public void GETAllFromDatabaseWithNoUserVerified() {

		ElementEntity t1 = new ElementEntity(Constants.ELEMENT_NAME+"1", Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 2);
		ElementEntity t2 = new ElementEntity(Constants.ELEMENT_NAME+"2", Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 2, 1);
		elementService.addElement(Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, t1);
		elementService.addElement(Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, t2);
		userService.addUser(new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME));
		restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class, "userPlayground", Constants.EMAIL_FOR_TESTS);
	}

	// url #8 /playground/elements/{userPlayground }/{email}/all test finished
	// ******************************************************************************************//
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
	// test started

	// 9.1 Scenario: Negative Distance
	@Test(expected = RuntimeException.class)
	public void GETElementsWithNegativeDistance() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		int distance = -1, x = 5, y = 5;
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, x, y);
		elementService.addElementNoLogin(element);
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, x, y, distance);
	}

	// 9.2 Scenario: Distance is Zero
	@Test
	public void distanceIsGreaterThanZero() {
		
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		for (int i = 0; i < 10; i++) {
			ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME + i, Constants.PLAYGROUND_NAME,	Constants.EMAIL_FOR_TESTS, i, i);
			elementService.addElementNoLogin(element);
			element = new ElementEntity("name" + i, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 0, i);
			elementService.addElementNoLogin(element);
		}
		int distance = 10, x = 5, y = 5;
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, x, y, distance);
		for (ElementTO element : elements) {
			double x1 = element.getLocation().getX();
			double y1 = element.getLocation().getY();
			double actualDistance = this.distanceBetween(x1, y1, x, y);
			assertThat(actualDistance).isLessThan(distance);
		}
	}

	// 9.3 Scenario: Distance is greater than Zero
	@Test
	public void GETElementsWithZeroDistance() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 2);
		elementService.addElementNoLogin(element);
		element = new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 3);
		elementService.addElementNoLogin(element);
		int distance = 0, x = 1, y = 2;
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, x, y, distance);
		assertThat(elements.length).isEqualTo(1);
		double actualDistance = distanceBetween(elements[0].getLocation().getX(), elements[0].getLocation().getY(), x,y);
		assertThat(actualDistance).isEqualTo(0);
	}

	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
	// test finished

	// ******************************************************************************************//

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"
	// test starts

	// 10.1 Scenario: Successfully Get Elements By Attribute Name Value

	@Test
	public void successfullyGetElementsByAttributeNameValue() {

		String creatorPlayground = "creatorPlayground";
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO elementTO = new ElementTO(new ElementEntity(Constants.ELEMENT_NAME, "playground", Constants.EMAIL_FOR_TESTS, 1, 0));
		elementTO.setCreatorPlayground(creatorPlayground);
		ElementTO elementForTest = elementTO;
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("attr3", "attr3Val");
		elementForTest.setAttributes(testMap);
		elementService.addElement(creatorPlayground, Constants.EMAIL_FOR_TESTS,elementForTest.toEntity());
		ElementTO[] forNow = this.restTemplate.getForObject(url + Constants.Function_10, ElementTO[].class,	creatorPlayground, Constants.EMAIL_FOR_TESTS, "attr3", "attr3Val");
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToIgnoringGivenFields(elementForTest, "creationDate", "id");
	}

	// 10.2 Scenario: Test no Elements in ElementService with searched
	// {attributeName} returns empty array of ElementTO
	@Test
	public void attributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(new ElementEntity(Constants.ELEMENT_NAME, "userPlayground", Constants.EMAIL_FOR_TESTS, 1, 0)) };
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("attr3", "attr3Val");
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10,ElementTO[].class, "creatorPlayground", Constants.EMAIL_FOR_TESTS, "noSuchAttribute", "attr3Val");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// 10.3 Scenario: Test no Elements in ElementService with searched
	// {attributeValue} returns empty array of ElementTO
	@Test
	public void valueInAttributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, "player", "creatorPlayground");
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(new ElementEntity(Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 0)) };
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("randomAttribute", "attr3Val");
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10,ElementTO[].class,Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, "randomAttribute", "wrongValue");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"
	// test finished

	// ******************************************************************************************//
	public double distanceBetween(double x1, double y1, double x2, double y2) {
		double xin = x1 - x2;
		double yin = y1 - y2;
		return Math.sqrt(xin * xin + yin * yin);

	}
}
