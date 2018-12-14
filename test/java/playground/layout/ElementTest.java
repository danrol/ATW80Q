package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Date;
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
import playground.logic.Location;
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

	// ******************************************************************************************//
	// url #5 /playground/elements/{userPlayground }/{email}

	// 5.1 Scenario: Test Save element in database
	@Test
	public void TestSaveElementInDatabase() {

		UserEntity user = new UserEntity("user", "mail@mail.com", "ava", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME);
		userService.addUser(user);
		ElementEntity element = new ElementEntity("id1", "elementName", "thisPlayground", "mail@mail.com", 5, 6);
		ElementTO elemTO = this.restTemplate.postForObject(this.url + "/playground/elements/{playground}/{email}",
				new ElementTO(element), ElementTO.class, Constants.PLAYGROUND_NAME, "mail@mail.com");

		System.err.println(elemTO + "\nPrinted returned ElementTO\n\n");
		ElementEntity element2 = elemTO.toEntity();
		System.err.println(element + "\nPrinted returned converted\n\n");

		assertThat(element2).isEqualTo(element);
	}

	@Test
	public void testPOSTNewElementWithNoCreatorIsAdded() {
//
//		String playground = "playground", creatorPlayground = "creator", id = "";
//		ElementEntity element = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 1, 2);
//
//		elementService.addElementNoLogin(element);
//		assertThat(!elementService.getElements().contains(element));
	//TODO
	}

	@Test
	public void testPOSTElementsThatAllRedyInDatabase() {

//		String playground = "playground", creatorPlayground = "creator", name = "nameOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity("1", name, playground, creatorPlayground, 3, 1);
//		arrElements[1] = new ElementEntity("2", name, playground, creatorPlayground, 3, 3);
//		arrElements[2] = new ElementEntity("3", name, playground, creatorPlayground, 3, 3);
//
//		elementService.addElementNoLogins(arrElements, playground);
//		assertThat(elementService.isElementInDatabase(arrElements[0]));
//		assertThat(elementService.isElementInDatabase(arrElements[1]));
//TODO
	}
	// url #5 /playground/elements/{userPlayground }/{email} finished

	// ******************************************************************************************//
	@Test
	public void testPOSTNewElementsAreAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground
		 * }/{email}/all When: User is verified AND i post new elements. Then: all
		 * elements are saved in the database.
		 */
//
//		String playground = "playground", creatorPlayground = "creator", name = "nameOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity("1", name, playground, creatorPlayground, 3, 1);
//		arrElements[1] = new ElementEntity("2", name, playground, creatorPlayground, 3, 2);
//		arrElements[2] = new ElementEntity("3", name, playground, creatorPlayground, 3, 3);
//
//		elementService.addElementNoLogins(arrElements, playground);
//		assertThat(
//				elementService.isElementInDatabase(arrElements[0]) && elementService.isElementInDatabase(arrElements[1])
//						&& elementService.isElementInDatabase(arrElements[2]));
//TODO
	}

	@Test
	public void testGETNewElementsFromDatabase() {

//
//		String playground = "playground", creatorPlayground = "creator", name = "nameOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity("1", name, playground, creatorPlayground, 3, 1);
//		arrElements[1] = new ElementEntity("2", name, playground, creatorPlayground, 3, 2);
//		arrElements[2] = new ElementEntity("3", name, playground, creatorPlayground, 3, 3);
//
//		elementService.addElementNoLogins(arrElements, playground);
//		assertThat(
//				elementService.isElementInDatabase(arrElements[0]) && elementService.isElementInDatabase(arrElements[1])
//						&& elementService.isElementInDatabase(arrElements[2]));
//TODO
	}

	@Test
	public void testPOSTNewElementsWithSameFieldsAreNotAddedDuplicatedToDatabase() {


//		String playground = "playground", creatorPlayground = "creator", name = "nameOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity("1", name, playground, creatorPlayground, 3, 1);
//		arrElements[1] = new ElementEntity("2", name, playground, creatorPlayground, 3, 1);
//		arrElements[2] = new ElementEntity("3", name, playground, creatorPlayground, 3, 3);
//
//		elementService.addElementNoLogins(arrElements, playground);
//		assertThat(elementService.isElementInDatabase(arrElements[0])
//				&& (!elementService.isElementInDatabase(arrElements[1]))
//				&& elementService.isElementInDatabase(arrElements[2]));
//TODO
	}
	/*******************************************************************************************************************************/
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test starts

	@Test
	public void testSuccessfullyUpdateElement() throws Exception {

		// 6.1
		UserEntity userElementCreator = new UserEntity("username", "email@email.com", "ava", "PLAYER", "playground");
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementEntity elementForTestEntity = new ElementEntity("123", "name", "playground", "email@email.com", 0, 1);

		userService.printUserDB();
		elementService.addElementNoLogin(elementForTestEntity);
		elementService.printElementDB();

		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setName("changedName");

		this.restTemplate.put(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				updatedElementForTestTO, "playground", "email@email.com", "playground", "123");

		ElementEntity actualEntity = elementService.getElementNoLogin(elementForTestEntity.getSuperkey());

		assertThat(actualEntity).isNotNull();

		assertThat(actualEntity).isEqualToComparingFieldByField(updatedElementForTestTO.toEntity());
	}

	@Test(expected = RuntimeException.class)
	public void testUpdateNonExistingElement() {

		// 6.2
		ElementEntity elementEntityForTest = new ElementEntity("123", "name", "playground", "email@email.com", 0, 1);
		ElementTO elementForTest = new ElementTO(elementEntityForTest);

		this.restTemplate.put(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				elementForTest, "userPlayground", "email@email.com", "wrongPlayground", "123");
	}

	@Test(expected = RuntimeException.class)
	public void testUpdateElementForNonExistingCreator() throws Exception {

		// 6.3
		ElementEntity updatedElementForTestEntity = new ElementEntity("123","name","userPlayground", "wrong@email.com",0,1);
		
		elementService.addElementNoLogin(updatedElementForTestEntity);

		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");

		this.restTemplate.put(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				updatedElementForTestTO, "userPlayground", "test@test.com", "playgr", "123");

	}

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test finished
	// ******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test
	// started

	@Test(expected = RuntimeException.class)
	public void testGetElementCorrectLoginElementNotInDatabase() {
		/*
		 * 
		 * Given the server is up and I GET
		 * /playground/elements/{userPlayground}/{email}/{playground}/{id} When user
		 * login details are correct and element is not in database Then I get an
		 * exception
		 * 
		 */
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, Constants.PLAYGROUND_NAME, "userTestWrong@gmail.com", Constants.PLAYGROUND_NAME,
				"elementIdTest");

	}

	@Test
	public void testGetElementCorrectLoginElementExists() {
		/*
		 * Given the server is up and I GET
		 * /playground/elements/{userPlayground}/{email}/{playground}/{id} When user
		 * login details are correct and element exists Then I get the element
		 */
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.verifyUser();
		ElementEntity element = new ElementEntity("elementIdTest", Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME,
				"elementTest@gmail.com", 1, 7);
		this.userService.addUser(u);
		this.elementService.addElementNoLogin(element);
		ElementTO el = this.restTemplate.getForObject(
				this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, "elementIdTest");
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	@Test(expected = RuntimeException.class)
	public void testGetElementIncorrectLoginElementExists() {
		/*
		 * Given the server is up and I GET
		 * /playground/elements/{userPlayground}/{email}/{playground}/{id} When user
		 * login details are incorrect and element exists Then I get an exception
		 */

		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity("elementIdTest", Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME,
				"elementTest@gmail.com", 5, 7);
		this.elementService.addElementNoLogin(element);

		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, Constants.PLAYGROUND_NAME, "userTestWrong@gmail.com", Constants.PLAYGROUND_NAME,
				"elementIdTest");

	}

	@Test(expected = RuntimeException.class)
	public void testGetElementIncorrectLoginElementNotInDatabase() {
		/*
		 * 
		 * Given the server is up and I GET
		 * /playground/elements/{userPlayground}/{email}/{playground}/{id} When user
		 * login details are incorrect and element is not in database Then I get an
		 * exception
		 * 
		 */
		UserEntity u = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,
				Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity("elementIdTest", Constants.ELEMENT_NAME, Constants.PLAYGROUND_NAME,
				"elementTest@gmail.com", 4, 3);
		this.elementService.addElementNoLogin(element);

		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, Constants.PLAYGROUND_NAME, "userTestWrong@gmail.com", Constants.PLAYGROUND_NAME,
				"elementIdTest");

	}

	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test
	// finished

	// ******************************************************************************************//
	// url #8 /playground/elements/{userPlayground }/{email}/all test started
	@Test
	public void testGETAllFromDatabase() {

//		boolean flag = true;
//		String playground = "playground", creatorPlayground = "creator", id = "nameOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 1, 2);
//		arrElements[1] = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 2, 3);
//
//		elementService.addElementNoLogins(arrElements, playground);
//		for (int i = 0; i < arrElements.length; i++) {
//			if (!elementService.getElements().contains(arrElements[i])) {
//				flag = false;
//				break;
//			}
//		}
//		assertThat(flag == true);
		//TODO
	}

	@Test
	public void testGETAllFromEmptyDatabase() {

		assertThat(elementService.getElements()).isEmpty();

	}

	@Test(expected = RuntimeException.class)
	public void testGETAllFromDatabaseWithNoUserVerified() {

//		boolean flag = true;
//		String playground = "playground", creatorPlayground = "creator", id = "idOfElement";
//		ElementEntity[] arrElements = new ElementEntity[3];
//		arrElements[0] = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 1, 2);
//		arrElements[1] = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 2, 1);
//
//		elementService.addElementsNoLogin(arrElements);
//		elementService.getAllElements();
		//TODO
	
	}

	// url #8 /playground/elements/{userPlayground }/{email}/all test finished
	// ******************************************************************************************//
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
	// test started
	@Test
	public void testIfWeGETElementsFromDatabaseWithRightRadius() {

//		boolean flag = false;
//		String playground = "playground", creatorPlayground = "creator", id = "idOfElement";
//		ElementEntity element1 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 0, 0);
//		ElementEntity element2 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 0, 1);
//		ElementEntity element3 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 3, 7);
//		ElementEntity element4 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 5, 7);
//		ElementEntity element5 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 0, 7);
//		ElementEntity element6 = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 7, 7);
//		elementService.addElementNoLogin(element1);
//		elementService.addElementNoLogin(element2);
//		elementService.addElementNoLogin(element3);
//		elementService.addElementNoLogin(element4);
//		elementService.addElementNoLogin(element5);
//		elementService.addElementNoLogin(element6);
//		double distance = 7;
//		ElementEntity[] arr = elementService.getAllElementsInRadius(element1, element1.getX(), element1.getY(),
//				distance, 0, 10);
//		if (arr.length == 3) {
//			flag = true;
//		}
//		assertThat(flag);
		
		//TODO
	}

	@Test(expected = RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {

//		String playground = "playground", creatorPlayground = "creator", id = "idOfElement";
//		ElementEntity element = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 1, 2);
//		double distance = -1;
//		elementService.getAllElementsInRadius(element, element.getX(), element.getY(), distance, 0, 10);
//TODO
	}

	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {

//		String playground = "playground", creatorPlayground = "creator", id = "idOfElement";
//		ElementEntity element = new ElementEntity(id, Constants.ELEMENT_NAME, playground, creatorPlayground, 1, 2);
//		double distance = 0;
//		assertThat(elementService.getAllElementsInRadius(element, element.getX(), element.getY(), distance, 0, 10))
//				.contains(element);
		
		
		//TODO
	}

	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
	// test finished

	// ******************************************************************************************//

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"
	// test starts

	@Test
	public void testSuccessfullyGetElementsByAttributeNameValue() {

//		10.1
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", "player", "creatorPlayground");
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementTO elementTO = new ElementTO(
				new ElementEntity("123", Constants.ELEMENT_NAME, "playground", "nudnik@mail.ru", 1, 0));
		elementTO.setCreatorPlayground("creatorPlayground");
		ElementTO[] elementForTest = { elementTO };

		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("attr3", "attr3Val");

		elementForTest[0].setAttributes(testMap);
		elementService.addElementNoLogin(elementForTest[0].toEntity());

		ElementTO[] forNow = this.restTemplate.getForObject(
				url + "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				"creatorPlayground", "nudnik@mail.ru", "attr3", "attr3Val");

		elementService.printElementDB();
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToComparingFieldByField(elementForTest[0]);
	}

	@Test
	public void testAttributeNotExist() {

//		10.1
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", "player", "creatorPlayground");

		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementTO[] elementForTest = { new ElementTO(
				new ElementEntity("123", Constants.ELEMENT_NAME, "userPlayground", "nudnik@mail.ru", 1, 0)) };

		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("attr3", "attr3Val");
		elementService.addElementNoLogin(elementForTest[0].toEntity());

		ElementTO[] responseEntity = restTemplate.getForObject(
				this.url + "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
				ElementTO[].class, "creatorPlayground", "nudnik@mail.ru", "noSuchAttribute", "attr3Val");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	@Test
	public void testValueInAttributeNotExist() {

		// 10.2
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", "player", "creatorPlayground");

		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementTO[] elementForTest = { new ElementTO(
				new ElementEntity("123", Constants.ELEMENT_NAME, "userPlayground", "nudnik@mail.ru", 1, 0)) };

		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1", "attr1Value");
		testMap.put("attribute2", "attr2Value");
		testMap.put("randomAttribute", "attr3Val");
		elementService.addElementNoLogin(elementForTest[0].toEntity());

		ElementTO[] responseEntity = restTemplate.getForObject(
				this.url + "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
				ElementTO[].class, "creatorPlayground", "nudnik@mail.ru", "randomAttribute", "wrongValue");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"
	// test finished

	// ******************************************************************************************//

}
