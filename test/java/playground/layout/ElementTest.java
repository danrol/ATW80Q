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

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE,Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, user.getPlayground(), user.getEmail(), Constants.Location_x, Constants.Location_y);
		ElementTO elemTO = this.restTemplate.postForObject(this.url + Constants.Function_5, new ElementTO(element), ElementTO.class, user.getPlayground(), user.getEmail());
		ElementEntity element2 = elemTO.toEntity();
		assertThat(element2).isEqualToIgnoringGivenFields(element, Constants.ELEMENT_FIELD_id , Constants.ELEMENT_FIELD_superkey);
	}

	// 5.2 Scenario: Saving an existing element
	@Test(expected = RuntimeException.class)
	public void saveAlreadyExistElement() {
		
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		element = elementService.addElementNoLogin(element);
		int dbSize = elementService.getAllElements().length;
		ElementTO elem = this.restTemplate.postForObject(this.url + Constants.Function_5, new ElementTO(element), ElementTO.class, userElementCreator.getPlayground(), userElementCreator.getEmail());
		assertThat(element).isEqualToIgnoringGivenFields(elem.toEntity(), Constants.ELEMENT_FIELD_creationDate);
		assertThat(dbSize).isEqualTo(elementService.getAllElements().length);

	}

	/*******************************************************************************************************************************/
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test starts

	// 6.1 Scenario: Test successfully update element in database
	@Test
	public void successfullyUpdateElement() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(),elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setName("changedName");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO, userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
		ElementEntity actualEntity = elementService.getElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getSuperkey());
		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToIgnoringGivenFields(updatedElementForTestTO.toEntity(), Constants.ELEMENT_FIELD_creationDate);
	}

	// 6.2 Scenario: Test update non-existent element
	@Test(expected = RuntimeException.class)
	public void updateNonExistingElement() {
		
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, userElementCreator.getPlayground(), userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		ElementTO elementForTest = new ElementTO(elementForTestEntity);
		this.restTemplate.put(this.url + Constants.Function_6, elementForTest, userElementCreator.getPlayground(), userElementCreator.getEmail(), elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// 6.3 Scenario : Test update existing element with non existing Creator email
	// in playground
	@Test(expected = RuntimeException.class)
	public void updateElementForNonExistingCreator() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.Other_Email_For_Test, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.Other_Email_For_Test);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.Other_Email_For_Test, Constants.Location_x, Constants.Location_y);
		elementService.addElementNoLogin(updatedElementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO, Constants.PLAYGROUND_NAME, userElementCreator.getEmail(), updatedElementForTestEntity.getCreatorPlayground(), updatedElementForTestEntity.getId());
	}

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test finished
	// ******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test

	// 7.1 Scenario: Get element with incorrect login details
	@Test(expected = RuntimeException.class)
	public void GETElementIncorrectLoginElementExists() {

		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.Location_x, Constants.Location_y);
		this.elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, Constants.Other_Playground, u.getEmail(), element.getCreatorPlayground(), element.getId());
	}

	// 7.2 Scenario: Get element with correct login details, and element not in
	// database
	@Test(expected = RuntimeException.class)
	public void GETElementCorrectLoginElementNotInDatabase() {
		
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.ID_FOR_TESTS);
	}

	// 7.3 Scenario: Get Element with correct login details and element exists
	@Test
	public void GETElementCorrectLoginElementExists() {
		
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, u.getEmail(), Constants.Location_x, Constants.Location_y);
		this.elementService.addElementNoLogin(element);
		ElementTO el = this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class,	Constants.PLAYGROUND_NAME, u.getEmail(), element.getCreatorPlayground(), element.getId());
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	
	//TODO: Test - get elements is radius and return exception: "No elements in radius"
	
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
		ElementEntity elem1 = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME + Constants.Numbers.ONE.ordinal(), Constants.PLAYGROUND_NAME, userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y);
		ElementEntity elem2 = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME+ Constants.Numbers.TWO.ordinal(), Constants.PLAYGROUND_NAME,userElementCreator.getEmail(), Constants.Location_y, Constants.Location_x);
		elem1 = elementService.addElementNoLogin(elem1);
		elem2 = elementService.addElementNoLogin(elem2);
		ElementTO[] arrForTest = new ElementTO[] {new ElementTO(elem1), new ElementTO(elem2)};
		ElementTO[] result = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class, userElementCreator.getEmail(),Constants.PLAYGROUND_NAME);
		assertThat(result).isNotNull();
		assertThat(result[0]).isEqualToIgnoringGivenFields(arrForTest[0], Constants.ELEMENT_FIELD_creationDate);
		assertThat(result[1]).isEqualToIgnoringGivenFields(arrForTest[1], Constants.ELEMENT_FIELD_creationDate);
	}

	// 8.2 Scenario: Test get all elements from empty database
	@Test
	public void GETAllFromEmptyDatabase() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elemArr = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class, userElementCreator.getEmail(), Constants.PLAYGROUND_NAME);
		assertThat(elemArr).isEqualTo(new ElementTO[0]);
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
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, user.getEmail(), Constants.Location_x, Constants.Location_y);
		elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, user.getEmail(), Constants.Location_x, Constants.Location_y, Constants.Negaive_Distance);
	}

	// 9.2 Scenario: Distance is Zero
	@Test
	public void distanceIsGreaterThanZero() {
		
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		for (int i = 0; i < Constants.Distance; i++) {
			ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME + i, Constants.PLAYGROUND_NAME, user.getEmail(), i, i);
			elementService.addElementNoLogin(element);
			element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME + i+i, Constants.PLAYGROUND_NAME, user.getEmail(), 0, i);
			elementService.addElementNoLogin(element);
		}
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, user.getEmail(), Constants.Location_x, Constants.Location_y, Constants.Distance);
		for (ElementTO element : elements) {
			double x1 = element.getLocation().getX();
			double y1 = element.getLocation().getY();
			double actualDistance = this.distanceBetween(x1, y1, Constants.Location_x, Constants.Location_y);
			assertThat(actualDistance).isLessThan(Constants.Distance);
		}
	}

	// 9.3 Scenario: Distance is greater than Zero
	@Test
	public void GETElementsWithZeroDistance() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, user.getEmail(), Constants.Location_x, Constants.Location_y);
		elementService.addElementNoLogin(element);
		element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, user.getEmail(), Constants.Location_x, Constants.Location_y + 2);
		elementService.addElementNoLogin(element);
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, Constants.Location_x, Constants.Location_y, Constants.Zero_Distance);
		assertThat(elements.length).isEqualTo(Constants.Numbers.ONE.ordinal());
		double actualDistance = distanceBetween(elements[0].getLocation().getX(), elements[0].getLocation().getY(), Constants.Location_x, Constants.Location_y);
		assertThat(actualDistance).isEqualTo(Constants.Zero_Distance);
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

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO elementTO = new ElementTO(new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, 1, 0));
		ElementTO elementForTest = elementTO;
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Constants.attributeName + Constants.Numbers.ONE.ordinal(), Constants.attrValue + Constants.Numbers.ONE.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.TWO.ordinal(), Constants.attrValue + Constants.Numbers.TWO.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.THREE.ordinal(), Constants.attrValue + Constants.Numbers.THREE.ordinal());
		elementForTest.setAttributes(testMap);
		elementService.addElement(Constants.PLAYGROUND_NAME, userElementCreator.getEmail(),elementForTest.toEntity());
		ElementTO[] forNow = this.restTemplate.getForObject(url + Constants.Function_10, ElementTO[].class,	elementForTest.getPlayground(), userElementCreator.getEmail(), Constants.attributeName + Constants.Numbers.THREE.ordinal(), testMap.get(Constants.attributeName + Constants.Numbers.THREE.ordinal()));
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToIgnoringGivenFields(elementForTest, Constants.ELEMENT_FIELD_creationDate, Constants.ELEMENT_FIELD_id);
	}

	// 10.2 Scenario: Test no Elements in ElementService with searched
	// {attributeName} returns empty array of ElementTO
	@Test
	public void attributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, userElementCreator.getEmail(), Constants.Location_x, Constants.Location_y)) };
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Constants.attributeName + Constants.Numbers.ONE.ordinal(), Constants.attrValue + Constants.Numbers.ONE.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.TWO.ordinal(), Constants.attrValue + Constants.Numbers.TWO.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.THREE.ordinal(), Constants.attrValue + Constants.Numbers.THREE.ordinal());
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10,ElementTO[].class, Constants.PLAYGROUND_NAME, userElementCreator.getEmail(), Constants.noSuchAttribute ,testMap.get(Constants.attributeName + Constants.Numbers.THREE.ordinal()));
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// 10.3 Scenario: Test no Elements in ElementService with searched
	// {attributeValue} returns empty array of ElementTO
	@Test
	public void valueInAttributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, Constants.Location_x, Constants.Location_y)) };
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Constants.attributeName + Constants.Numbers.ONE.ordinal(), Constants.attrValue + Constants.Numbers.ONE.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.TWO.ordinal(), Constants.attrValue + Constants.Numbers.TWO.ordinal());
		testMap.put(Constants.attributeName + Constants.Numbers.THREE.ordinal(), Constants.attrValue + Constants.Numbers.THREE.ordinal());
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10,ElementTO[].class,Constants.PLAYGROUND_NAME, userElementCreator.getEmail(), Constants.attributeName + Constants.Numbers.THREE.ordinal(), Constants.wrongAttributeValue);
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
