package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
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

import playground.constants.Activity;
import playground.constants.Constants;
import playground.constants.Element;
import playground.constants.Playground;
import playground.constants.User;
import playground.dal.ElementDao;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTest {
	private RestTemplate restTemplate;

	private ElementService elementService;
	private ElementDao elementsDB;

	@Autowired
	public void setElementService(ElementService elementService, ElementDao elementsDB) {
		this.elementsDB = elementsDB;
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

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);

		ElementTO elemTO = this.restTemplate.postForObject(this.url + Constants.Function_5, new ElementTO(element),
				ElementTO.class, user.getPlayground(), user.getEmail());
		ElementEntity element2 = elemTO.toEntity();
		assertThat(element2).isEqualToIgnoringGivenFields(element, Element.ELEMENT_FIELD_id,
				Element.ELEMENT_FIELD_superkey, Element.ELEMENT_FIELD_creatorPlayground,
				Element.ELEMENT_FIELD_creatorEmail);
	}

	// 5.2 Scenario: Add message board activity as Manager
	@Test
	public void AddMessageBoardAsManager() {
		String messageBoardName = "messageBoardName";
		double x = 5;
		double y = 6;
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity board_entity = createMessageBoard(messageBoardName, x, y);
		ElementTO ele = new ElementTO(board_entity);

		ElementTO messageBoardTO = this.restTemplate.postForObject(this.url + Constants.Function_5, ele,
				ElementTO.class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

		ElementEntity rv_messageboard = messageBoardTO.toEntity();
		assertThat(rv_messageboard.getName()).isEqualTo(messageBoardName);
		assertThat(elementsDB.existsById(rv_messageboard.getSuperkey()));
		ElementEntity db_msg = elementService.getElementNoLogin(rv_messageboard.getSuperkey());
		assertThat(db_msg).isEqualTo(rv_messageboard);

	}

	// 5.3 Scenario: Add message board activity as Player
	@Test(expected = RuntimeException.class)
	public void AddMessageBoardAsPlayer() {
		String messageBoardName = "messageBoardName";
		double x = 5;
		double y = 6;
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity board_entity = createMessageBoard(messageBoardName, x, y);
		ElementTO ele = new ElementTO(board_entity);

		ele = this.restTemplate.postForObject(this.url + Constants.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

	}

	//5.4 Scenario: Adding question to Playground as Manager.
	@Test
	public void AddingQuestionToPlaygroundAsManager() {
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity q = createQuestionElement("question title", "the question is here", "this is the answer", 6, 4,
				7);

		ElementTO ele = new ElementTO(q);
		ele = this.restTemplate.postForObject(this.url + Constants.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
		ElementEntity rv_question = ele.toEntity();
		assertThat(q).isEqualToIgnoringGivenFields(rv_question, Element.ELEMENT_FIELD_superkey,
				Element.ELEMENT_FIELD_id, Element.ELEMENT_FIELD_creatorEmail);

	}

	//5.5 Scenario: Adding question to Playground as Player.
	@Test(expected = RuntimeException.class)
	public void AddingQuestionToPlaygroundAsPlayer() {
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity q = createQuestionElement("question title", "the question is here???", "this is the answer", 6, 4,
				7);
		ElementTO ele = new ElementTO(q);
		ele = this.restTemplate.postForObject(this.url + Constants.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
	}

	// 5.6 Scenario: Adding to Playground a question with a missing attribute.
	@Test(expected = RuntimeException.class)
	public void AddingToPlaygroundAQuestionWithAMissingAttribute() {
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity question = new ElementEntity("question title", 2, 3);
		question.setType(Element.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Element.ELEMENT_QUESTION_KEY, "the question is here");
		question.getAttributes().put(Element.ELEMENT_ANSWER_KEY, "this is the answer");

		ElementTO ele = new ElementTO(question);
		ele = this.restTemplate.postForObject(this.url + Constants.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

	}

	/*******************************************************************************************************************************/
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test starts

	// 6.1 Scenario: Test successfully update element in database
	@Test
	public void successfullyUpdateElement() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setName("changedName");

		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO,
				userElementCreator.getPlayground(), userElementCreator.getEmail(),
				elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
		ElementEntity actualEntity = elementService.getElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity.getSuperkey());

		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToIgnoringGivenFields(updatedElementForTestTO.toEntity(),
				Element.ELEMENT_FIELD_creationDate);
	}

	// 6.2 Scenario: Test update non-existent element
	@Test(expected = RuntimeException.class)
	public void updateNonExistingElement() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		ElementTO elementForTest = new ElementTO(elementForTestEntity);
		this.restTemplate.put(this.url + Constants.Function_6, elementForTest, userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity.getCreatorPlayground(),
				elementForTestEntity.getId());
	}

	// 6.3 Scenario : Test update existing element with non existing Creator email
	// in playground
	@Test(expected = RuntimeException.class)
	public void updateElementForNonExistingCreator() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.Other_Email_For_Test,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, User.Other_Email_For_Test);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME,
				Element.LOCATION_X1, Element.LOCATION_Y1);
		updatedElementForTestEntity = elementService.addElementNoLogin(updatedElementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO, Playground.PLAYGROUND_NAME,
				userElementCreator.getEmail(), updatedElementForTestEntity.getCreatorPlayground(),
				updatedElementForTestEntity.getId());
	}

	// 6.4 Scenario : Test update element ID
	@Test(expected = RuntimeException.class)
	public void updateElementID() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setId(Constants.ID_FOR_TESTS + "0");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO,
				userElementCreator.getPlayground(), userElementCreator.getEmail(),
				elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// 6.5 Scenario : Test update element creator playground
	@Test(expected = RuntimeException.class)
	public void updateElementCreatorplayground() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setId(Constants.CREATOR_PLAYGROUND_FOR_TESTS + "1");
		this.restTemplate.put(this.url + Constants.Function_6, updatedElementForTestTO,
				userElementCreator.getPlayground(), userElementCreator.getEmail(),
				elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with
	// PUT test finished
	// ******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test

	// 7.1 Scenario: Get element with incorrect login details
	@Test(expected = RuntimeException.class)
	public void GETElementIncorrectLoginElementExists() {

		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		this.elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, Playground.Other_Playground,
				u.getEmail(), element.getCreatorPlayground(), element.getId());
	}

	// 7.2 Scenario: Get element with correct login details, and element not in
	// database
	@Test(expected = RuntimeException.class)
	public void GETElementCorrectLoginElementNotInDatabase() {

		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class, u.getEmail(),
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.ID_FOR_TESTS);
	}

	// 7.3 Scenario: Get Element with correct login details and element exists
	@Test
	public void GETElementCorrectLoginElementExists() {

		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, Constants.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		this.elementService.addElementNoLogin(element);
		ElementTO el = this.restTemplate.getForObject(this.url + Constants.Function_7, ElementTO.class,
				Playground.PLAYGROUND_NAME, u.getEmail(), element.getCreatorPlayground(), element.getId());
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	// TODO: Test - get elements is radius and return exception: "No elements in
	// radius"

	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test
	// finished

	// ******************************************************************************************//
	// url #8 /playground/elements/{userPlayground }/{email}/all test started

	// 8.1 Scenario: Test get all elements from database
	@Test
	public void GETAllFromDatabaseWithoutPagination() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementEntity elem1 = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + "1",
				Element.LOCATION_X1, Element.LOCATION_Y1);
		ElementEntity elem2 = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + "2",
				Element.LOCATION_Y1, Element.LOCATION_X1);

		elem1 = elementService.addElementNoLogin(elem1);
		elem2 = elementService.addElementNoLogin(elem2);
		ElementTO[] arrForTest = new ElementTO[] { new ElementTO(elem1), new ElementTO(elem2) };

		ElementTO[] valuesFromController = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class,
				userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);

		ElementTO[] valuesFromDatabase = getElementTOArray(elementService.getAllElements());
		assertThat(valuesFromController).isNotNull();

		// Check values returned from controller
		assertThat(valuesFromController[0]).isEqualToIgnoringGivenFields(arrForTest[0],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromController[1]).isEqualToIgnoringGivenFields(arrForTest[1],
				Element.ELEMENT_FIELD_creationDate);

		// Check values in database
		assertThat(valuesFromDatabase).isNotNull();
		assertThat(valuesFromDatabase[0]).isEqualToIgnoringGivenFields(arrForTest[0],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromDatabase[1]).isEqualToIgnoringGivenFields(arrForTest[1],
				Element.ELEMENT_FIELD_creationDate);

	}

	// 8.2 Scenario: Test get all elements from empty database
	@Test
	public void GETAllFromEmptyDatabase() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elemArr = restTemplate.getForObject(this.url + Constants.Function_8, ElementTO[].class,
				userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);

		// Check values returned from controller
		assertThat(elemArr).isEqualTo(new ElementTO[0]);

		// Check values in database
		assertThat(elementService.getAllElements()).isEqualTo(new ElementTO[0]);
	}

	// 8.3 Scenario: Test get elements with pagination from database
	@Test
	public void GETALLFromDatabaseWithPagination() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		for (int n = 1; n <= 11; n++) {
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(),
					new ElementEntity(String.valueOf(n) + Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
							Element.LOCATION_Y1));
		}

		ElementTO[] valuesFromController = restTemplate.getForObject(
				this.url + Constants.Function_8
						+ createPaginationStringAppendixForUrl(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER),
				ElementTO[].class, userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);
		Pageable pageable = PageRequest.of(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER);
		ElementTO[] valuesFromDatabase = getElementTOArray(
				elementService.lstToArray(elementService.getElements(pageable)));

		assertThat(valuesFromController).isNotNull();
		assertThat(valuesFromController[0]).isEqualToIgnoringGivenFields(valuesFromDatabase[0],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromController[1]).isEqualToIgnoringGivenFields(valuesFromDatabase[1],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromController[2]).isEqualToIgnoringGivenFields(valuesFromDatabase[2],
				Element.ELEMENT_FIELD_creationDate);

	}

	// url #8 /playground/elements/{userPlayground }/{email}/all test finished
	// ******************************************************************************************//
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
	// test started

	// 9.1 Scenario: Negative Distance
	@Test(expected = RuntimeException.class)
	public void GETElementsWithNegativeDistance() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class, Playground.PLAYGROUND_NAME,
				user.getEmail(), Element.LOCATION_X1, Element.LOCATION_Y1, Element.Negative_Distance);
	}

	// 9.2 Scenario: Distance is Zero
	@Test
	public void distanceIsGreaterThanZero() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		for (int i = 0; i < 20; i++) {
			ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + i, i, i);
			elementService.addElementNoLogin(element);
			element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + i + i, 0, i);
			elementService.addElementNoLogin(element);
		}
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,
				Playground.PLAYGROUND_NAME, user.getEmail(), Element.LOCATION_X1, Element.LOCATION_Y1,
				Element.Distance);
		for (ElementTO element : elements) {
			double x1 = element.getLocation().getX();
			double y1 = element.getLocation().getY();
			double actualDistance = this.distanceBetween(x1, y1, Element.LOCATION_X1, Element.LOCATION_Y1);
			assertThat(actualDistance).isLessThan(Element.Distance);
		}
	}

	// 9.3 Scenario: Distance is greater than Zero
	@Test
	public void GETElementsWithZeroDistance() {

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1,
				Element.LOCATION_Y1);
		elementService.addElementNoLogin(element);
		element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1, Element.LOCATION_Y1 + 2);
		elementService.addElementNoLogin(element);
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Constants.Function_9, ElementTO[].class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, Element.LOCATION_X1, Element.LOCATION_Y1,
				Constants.Zero_Distance);
		assertThat(elements.length).isEqualTo("1");
		double actualDistance = distanceBetween(elements[0].getLocation().getX(), elements[0].getLocation().getY(),
				Element.LOCATION_X1, Element.LOCATION_Y1);
		assertThat(actualDistance).isEqualTo(Constants.Zero_Distance);
	}

	// 9.4 Scenario: Distance is greater than Zero with pagination
	@Test
	public void GETElementsWithZeroDistanceWithPagination() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] arrForTest = new ElementTO[Constants.SIZE_NUMBER];
		ElementEntity elementToAdd;

		for (int n = 1; n <= 6; n++) {
			elementToAdd = new ElementEntity(String.valueOf(n) + Element.DEFAULT_ELEMENT_NAME, 0, n);
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementToAdd);
		}
		ElementTO[] result = restTemplate.getForObject(
				this.url + Constants.Function_9
						+ createPaginationStringAppendixForUrl(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER),
				ElementTO[].class, Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, Element.LOCATION_X2,
				Element.LOCATION_Y2, Element.ANOTHER_DISTANCE);
		Pageable pageable = PageRequest.of(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER);
		arrForTest = getElementTOArray(
				elementService.getAllElementsInRadius(userElementCreator.getPlayground(), userElementCreator.getEmail(),
						Element.LOCATION_X2, Element.LOCATION_Y2, Element.ANOTHER_DISTANCE, pageable));

		assertThat(result).isNotNull();
		assertThat(result.length).isEqualTo(arrForTest.length);
		assertThat(result[0]).isEqualToIgnoringGivenFields(arrForTest[0], Element.ELEMENT_FIELD_creationDate);
		assertThat(result[1]).isEqualToIgnoringGivenFields(arrForTest[1], Element.ELEMENT_FIELD_creationDate);
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

		// TODO check why fails
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO elementTO = new ElementTO(new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 1, 0));
		ElementTO elementForTest = elementTO;
//		ElementTO elementTO2 = new ElementTO(new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, Constants.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, 1, 0));
//		ElementTO elementForTest2 = elementTO2;

		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Activity.attributeName, Activity.attrValue);
		testMap.put(Activity.attributeName + "2",
				Activity.attrValue + "2");
		testMap.put(Activity.attributeName + "3",
				Activity.attrValue + "3");
		elementForTest.setAttributes(testMap);
//		elementForTest2.setAttributes(testMap);

		elementService.addElement(Playground.PLAYGROUND_NAME, userElementCreator.getEmail(), elementForTest.toEntity());
//		elementService.addElement(Constants.PLAYGROUND_NAME, userElementCreator.getEmail(),elementForTest2.toEntity());

//		ElementTO[] result = this.restTemplate.getForObject(url + Constants.Function_10, ElementTO[].class,	Constants.PLAYGROUND_NAME, 
//				userElementCreator.getEmail(), Activity.attributeName, Activity.attrValue);

//		assertThat(result).isNotNull();
//		assertThat(result[0]).isEqualToIgnoringGivenFields(elementForTest, Constants.ELEMENT_FIELD_creationDate, Constants.ELEMENT_FIELD_id);
	}

	// 10.2 Scenario: Test no Elements in ElementService with searched
	// {attributeName} returns empty array of ElementTO
	@Test
	public void attributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(
				new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1, Element.LOCATION_Y1)) };
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Activity.attributeName + "1",
				Activity.attrValue + "1");
		testMap.put(Activity.attributeName + "2",
				Activity.attrValue + "2");
		testMap.put(Activity.attributeName + "3",
				Activity.attrValue + "3");
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10, ElementTO[].class,
				Playground.PLAYGROUND_NAME, userElementCreator.getEmail(), Activity.noSuchAttribute,
				testMap.get(Activity.attributeName + "3"));
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// 10.3 Scenario: Test no Elements in ElementService with searched
	// {attributeValue} returns empty array of ElementTO
	@Test
	public void valueInAttributeNotExist() {

		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(
				new ElementEntity(Element.DEFAULT_ELEMENT_NAME, Element.LOCATION_X1, Element.LOCATION_Y1)) };
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Activity.attributeName + "1",
				Activity.attrValue + "1");
		testMap.put(Activity.attributeName + "2",
				Activity.attrValue + "2");
		testMap.put(Activity.attributeName + "3",
				Activity.attrValue + "3");
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Constants.Function_10, ElementTO[].class,
				Playground.PLAYGROUND_NAME, userElementCreator.getEmail(),
				Activity.attributeName + "3", Activity.wrongAttributeValue);
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// 10.4 Scenario: Test Successfully Get Elements By Attribute Name Value with
	// pagination
	@Test
	public void successfullyGetElementsByAttributeNameValueWithPagination() {
		UserEntity userElementCreator = new UserEntity(Constants.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] arrForTest;
		ElementEntity elementToAdd;

		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put(Activity.attributeName + "1", Activity.attrValue);
		testMap.put(Activity.attributeName + "2", Activity.attrValue);
		testMap.put(Activity.attributeName, Activity.attrValue);

		for (int n = 1; n <= 11; n++) {
			elementToAdd = new ElementEntity(String.valueOf(n) + Element.DEFAULT_ELEMENT_NAME,
					Element.LOCATION_X1 + n, Element.LOCATION_Y1);

			if (3 <= n && n <= 9) {
				elementToAdd.setAttributes(testMap);
			}
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementToAdd);
		}

		ElementTO[] result = restTemplate.getForObject(
				this.url + Constants.Function_10
						+ createPaginationStringAppendixForUrl(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER),
				ElementTO[].class, Playground.PLAYGROUND_NAME, userElementCreator.getEmail(), Activity.attributeName,
				Activity.attrValue);

		Pageable pageable = PageRequest.of(Constants.PAGE_NUMBER, Constants.SIZE_NUMBER);
		arrForTest = getElementTOArray(
				elementService.getElementsWithValueInAttribute(userElementCreator.getPlayground(),
						userElementCreator.getEmail(), Activity.attributeName, Activity.attrValue, pageable));

		assertThat(result).isNotNull();
		assertThat(result[0]).isEqualToIgnoringGivenFields(arrForTest[0], Element.ELEMENT_FIELD_creationDate);
		assertThat(result[1]).isEqualToIgnoringGivenFields(arrForTest[1], Element.ELEMENT_FIELD_creationDate);
		assertThat(result[2]).isEqualToIgnoringGivenFields(arrForTest[2], Element.ELEMENT_FIELD_creationDate);
	}
	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"
	// test finished

	// ******************************************************************************************//
	// Test helper methods

	public double distanceBetween(double x1, double y1, double x2, double y2) {
		double xin = x1 - x2;
		double yin = y1 - y2;
		return Math.sqrt(xin * xin + yin * yin);

	}

	public String createPaginationStringAppendixForUrl(int pageNum, int sizeNum) {
		return "?page=" + String.valueOf(pageNum) + "&size=" + String.valueOf(sizeNum);
	}

	public ElementTO[] getElementTOArray(ElementEntity[] lst) {
		ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementEntity e : lst)
			result.add(new ElementTO(e));
		return result.toArray(new ElementTO[lst.length]);
	}

	public ElementEntity createQuestionElement(String questionTitle, String questionBody, String answer, int points,
			double x, double y) {
		ElementEntity question = new ElementEntity(questionTitle, x, y);
		question.setType(Element.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Element.ELEMENT_QUESTION_KEY, questionBody);
		question.getAttributes().put(Element.ELEMENT_ANSWER_KEY, answer);
		question.getAttributes().put(Element.ELEMENT_POINT_KEY, points);
		return question;
	}

	public ElementEntity createMessageBoard(String messageBoardName, double x, double y) {
		ElementEntity board = new ElementEntity(messageBoardName, x, y);
		board.setType(Element.ELEMENT_MESSAGEBOARD_TYPE);
		board.getAttributes().put(Element.MESSAGEBOARD_MESSAGE_COUNT, 0);
		return board;
	}

}
