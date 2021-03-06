package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
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

		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);

		ElementEntity element2 = this.restTemplate.postForObject(this.url + Playground.Function_5,
				new ElementTO(element), ElementTO.class, user.getPlayground(), user.getEmail()).toEntity();
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
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity board_entity = createMessageBoard(messageBoardName, x, y);
		ElementTO ele = new ElementTO(board_entity);

		ElementTO messageBoardTO = this.restTemplate.postForObject(this.url + Playground.Function_5, ele,
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
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity board_entity = createMessageBoard(messageBoardName, x, y);
		ElementTO ele = new ElementTO(board_entity);

		ele = this.restTemplate.postForObject(this.url + Playground.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

	}

	// 5.4 Scenario: Adding question to Playground as Manager.
	@Test
	public void AddingQuestionToPlaygroundAsManager() {
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity q = createQuestionElement("question title", "the question is here", "this is the answer", 6, 4,
				7);

		ElementTO ele = new ElementTO(q);
		ele = this.restTemplate.postForObject(this.url + Playground.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
		ElementEntity rv_question = ele.toEntity();
		assertThat(q).isEqualToIgnoringGivenFields(rv_question, Element.ELEMENT_FIELD_superkey,
				Element.ELEMENT_FIELD_id, Element.ELEMENT_FIELD_creatorEmail);

	}

	// 5.5 Scenario: Adding question to Playground as Player.
	@Test(expected = RuntimeException.class)
	public void AddingQuestionToPlaygroundAsPlayer() {
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity q = createQuestionElement("question title", "the question is here???", "this is the answer", 6, 4,
				7);
		ElementTO ele = new ElementTO(q);
		ele = this.restTemplate.postForObject(this.url + Playground.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);
	}

	// 5.6 Scenario: Adding to Playground a question with a missing attribute.
	@Test(expected = RuntimeException.class)
	public void AddingToPlaygroundAQuestionWithAMissingAttribute() {
		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);

		ElementEntity question = new ElementEntity("question title", 2, 3);
		question.setType(Element.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Element.ELEMENT_QUESTION_KEY, "the question is here");
		question.getAttributes().put(Element.ELEMENT_ANSWER_KEY, "this is the answer");

		ElementTO ele = new ElementTO(question);
		ele = this.restTemplate.postForObject(this.url + Playground.Function_5, ele, ElementTO.class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS);

	}

	/*******************************************************************************************************************************/
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with

	// 6.1 Scenario: Test successfully update element in database
	@Test
	public void successfullyUpdateElement() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setName("changedName");

		this.restTemplate.put(this.url + Playground.Function_6, updatedElementForTestTO,
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

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		ElementTO elementForTest = new ElementTO(elementForTestEntity);
		this.restTemplate.put(this.url + Playground.Function_6, elementForTest, userElementCreator.getPlayground(),
				"wrong@email.com", elementForTestEntity.getCreatorPlayground(),
				elementForTestEntity.getId());
	}

	// 6.3 Scenario : Test update existing element with non existing Creator email
	@Test(expected = RuntimeException.class)
	public void updateElementForNonExistingCreator() {
		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.Other_Email_For_Test,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, User.Other_Email_For_Test);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		updatedElementForTestEntity = elementService.addElementNoLogin(updatedElementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		this.restTemplate.put(this.url + Playground.Function_6, updatedElementForTestTO, Playground.PLAYGROUND_NAME,
				userElementCreator.getEmail(), updatedElementForTestEntity.getCreatorPlayground(),
				updatedElementForTestEntity.getId());
	}

	// 6.4 Scenario : Test update element ID
	@Test(expected = RuntimeException.class)
	public void updateElementID() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userElementCreator = userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setId(User.ID_FOR_TESTS + "0");
		this.restTemplate.put(this.url + Playground.Function_6, updatedElementForTestTO,
				userElementCreator.getPlayground(), userElementCreator.getEmail(),
				elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// 6.5 Scenario : Test update element creator playground
	@Test(expected = RuntimeException.class)
	public void updateElementCreatorplayground() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementEntity elementForTestEntity = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		elementForTestEntity = elementService.addElement(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), elementForTestEntity);
		ElementTO updatedElementForTestTO = new ElementTO(elementForTestEntity);
		updatedElementForTestTO.setId(Playground.CREATOR_PLAYGROUND_FOR_TESTS + "1");
		this.restTemplate.put(this.url + Playground.Function_6, updatedElementForTestTO,
				userElementCreator.getPlayground(), userElementCreator.getEmail(),
				elementForTestEntity.getCreatorPlayground(), elementForTestEntity.getId());
	}

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with

	// ******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test

	// 7.1 Scenario: Get element with incorrect login details
	@Test(expected = RuntimeException.class)
	public void GETElementIncorrectLoginElementExists() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		this.elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Playground.Function_7, ElementTO.class, Playground.Other_Playground,
				u.getEmail(), element.getCreatorPlayground(), element.getId());
	}

	// 7.2 Scenario: Get element with correct login details, and element not in

	@Test(expected = RuntimeException.class)
	public void GETElementCorrectLoginElementNotInDatabase() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + Playground.Function_7, ElementTO.class, u.getEmail(),
				Playground.CREATOR_PLAYGROUND_FOR_TESTS, User.ID_FOR_TESTS);
	}

	// 7.3 Scenario: Get Element with correct login details and element exists
	@Test
	public void GETElementCorrectLoginElementExists() {

		UserEntity u = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		this.elementService.addElementNoLogin(element);
		ElementTO el = this.restTemplate.getForObject(this.url + Playground.Function_7, ElementTO.class,
				Playground.PLAYGROUND_NAME, u.getEmail(), element.getCreatorPlayground(), element.getId());
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test

	// ******************************************************************************************//
	// url #8 /playground/elements/{userPlayground }/{email}/all test started

	// 8.1 Scenario: Test get all elements from database
	@Test
	public void GETAllFromDatabaseWithoutPagination() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		ElementEntity elem1 = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + "1", 5, 6);
		ElementEntity elem2 = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + "2", 6, 5);

		elem1 = elementService.addElementNoLogin(elem1);
		elem2 = elementService.addElementNoLogin(elem2);
		ElementTO[] arrForTest = new ElementTO[] { new ElementTO(elem1), new ElementTO(elem2) };

		ElementTO[] valuesFromController = restTemplate.getForObject(this.url + Playground.Function_8,
				ElementTO[].class, userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);

		ElementTO[] valuesFromDatabase = getElementTOArray(elementService.getAllElements());

		assertThat(valuesFromController).isNotNull();
		assertThat(valuesFromController[0]).isEqualToIgnoringGivenFields(arrForTest[0],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromController[1]).isEqualToIgnoringGivenFields(arrForTest[1],
				Element.ELEMENT_FIELD_creationDate);

		assertThat(valuesFromDatabase).isNotNull();
		assertThat(valuesFromDatabase[0]).isEqualToIgnoringGivenFields(arrForTest[0],
				Element.ELEMENT_FIELD_creationDate);
		assertThat(valuesFromDatabase[1]).isEqualToIgnoringGivenFields(arrForTest[1],
				Element.ELEMENT_FIELD_creationDate);

	}

	// 8.2 Scenario: Test get all elements from empty database
	@Test
	public void GETAllFromEmptyDatabase() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elemArr = restTemplate.getForObject(this.url + Playground.Function_8, ElementTO[].class,
				userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);

		assertThat(elemArr).isEqualTo(new ElementTO[0]);

		assertThat(elementService.getAllElements()).isEqualTo(new ElementTO[0]);
	}

	// 8.3 Scenario: Test get elements with pagination from database
	@Test
	public void GETALLFromDatabaseWithPagination() {
		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		for (int n = 1; n <= 11; n++) {
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(),
					new ElementEntity(String.valueOf(n) + Element.DEFAULT_ELEMENT_NAME, 5, 6));
		}

		ElementTO[] valuesFromController = restTemplate.getForObject(
				this.url + Playground.Function_8 + createPaginationStringAppendixForUrl(1, 3), ElementTO[].class,
				userElementCreator.getEmail(), Playground.PLAYGROUND_NAME);
		Pageable pageable = PageRequest.of(1, 3);
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

	// 9.1 Scenario: Negative Distance
	@Test(expected = RuntimeException.class)
	public void GETElementsWithNegativeDistance() {

		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		elementService.addElementNoLogin(element);
		this.restTemplate.getForObject(this.url + Playground.Function_9, ElementTO[].class, Playground.PLAYGROUND_NAME,
				user.getEmail(), 5, 6, -1);
	}

	// 9.2 Scenario: Distance is Zero
	@Test
	public void distanceIsGreaterThanZero() {

		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		for (int i = 0; i < 20; i++) {
			ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + i, i, i);
			elementService.addElementNoLogin(element);
			element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME + i + i, 0, i);
			elementService.addElementNoLogin(element);
		}
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Playground.Function_9, ElementTO[].class,
				Playground.PLAYGROUND_NAME, user.getEmail(), 5, 6, 10);
		for (ElementTO element : elements) {
			double x1 = element.getLocation().getX();
			double y1 = element.getLocation().getY();
			double actualDistance = this.distanceBetween(x1, y1, 5, 6);
			assertThat(actualDistance).isLessThan(10);
		}
	}

	// 9.3 Scenario: Distance is greater than Zero
	@Test
	public void GETElementsWithZeroDistance() {

		UserEntity user = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS, User.AVATAR_FOR_TESTS,
				User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ElementEntity element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6);
		elementService.addElementNoLogin(element);
		element = new ElementEntity(Element.DEFAULT_ELEMENT_NAME, 5, 6 + 2);
		elementService.addElementNoLogin(element);
		ElementTO[] elements = this.restTemplate.getForObject(this.url + Playground.Function_9, ElementTO[].class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, 5, 6, 0);
		assertThat(elements.length).isEqualTo(1);
		double actualDistance = distanceBetween(elements[0].getLocation().getX(), elements[0].getLocation().getY(), 5,
				6);
		assertThat(actualDistance).isEqualTo(0);
	}

	// 9.4 Scenario: Distance is greater than Zero with pagination
	@Test
	public void GETElementsWithZeroDistanceWithPagination() {
		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] arrForTest = new ElementTO[3];
		ElementEntity elementToAdd;

		for (int n = 1; n <= 6; n++) {
			elementToAdd = new ElementEntity(String.valueOf(n) + Element.DEFAULT_ELEMENT_NAME, 0, n);
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementToAdd);
		}

		ElementTO[] result = restTemplate.getForObject(
				this.url + Playground.Function_9 + createPaginationStringAppendixForUrl(1, 3), ElementTO[].class,
				Playground.PLAYGROUND_NAME, User.EMAIL_FOR_TESTS, 0, 8, 6);
		Pageable pageable = PageRequest.of(1, 3);
		arrForTest = getElementTOArray(elementService.getAllElementsInRadius(userElementCreator.getPlayground(),
				userElementCreator.getEmail(), 0, 8, 6, pageable));

		assertThat(result).isNotNull();
		assertThat(result.length).isEqualTo(arrForTest.length);
		assertThat(result[0]).isEqualToIgnoringGivenFields(arrForTest[0], Element.ELEMENT_FIELD_creationDate);
		assertThat(result[1]).isEqualToIgnoringGivenFields(arrForTest[1], Element.ELEMENT_FIELD_creationDate);
	}

	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}

	// ******************************************************************************************//

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"

	// 10.1 Scenario: Test Successfully Get Elements By Name and Type with
	// pagination
	@Test
	public void managerSuccessfullyGetAllElementsByAttributeNameValueWithPagination() {
		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.MANAGER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] arrForTest;
		ElementEntity elementToAdd;

		for (int n = 1; n <= 11; n++) {
			elementToAdd = createQuestionElement(Element.QUESTION_TITLE_TEST + String.valueOf(n),
					Element.QUESTION_BODY_TEST, Element.ELEMENT_ANSWER_KEY, Element.QUESTION_POINT_VALUE_TEST, 5, 6);

			if (3 <= n && n <= 9) {
				elementToAdd.setCreatorEmail(Element.ELEMENT_FIELD_creatorEmail);

			}
			elementService.addElement(userElementCreator.getPlayground(), userElementCreator.getEmail(), elementToAdd);
		}

		ElementTO[] result = restTemplate.getForObject(
				this.url + Playground.Function_10 + createPaginationStringAppendixForUrl(1, 3), ElementTO[].class,
				Playground.PLAYGROUND_NAME, userElementCreator.getEmail(), Element.TYPE_FIELD,
				Element.ELEMENT_QUESTION_TYPE);

		Pageable pageable = PageRequest.of(1, 3);
		arrForTest = getElementTOArray(
				elementService.getElementsByAttributeNameAndAttributeValue(userElementCreator.getPlayground(),
						userElementCreator.getEmail(), Element.TYPE_FIELD, Element.ELEMENT_QUESTION_TYPE, pageable));

		assertThat(result).isNotNull();
		for (int i = 0; i < 3; i++) {
			assertThat(result[i]).isEqualToIgnoringGivenFields(arrForTest[i], Element.ELEMENT_FIELD_creationDate);
		}
	}

	// 10.2 Scenario: Test no Elements in ElementService with searched
	@Test(expected = RuntimeException.class)
	public void attributeNameNotExist() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);

		restTemplate.getForObject(this.url + Playground.Function_10, ElementTO[].class, Playground.PLAYGROUND_NAME,
				userElementCreator.getEmail(), "no_such_name", Element.ELEMENT_QUESTION_TYPE);
	}

	// 10.3 Scenario: Test no Elements in ElementService with searched
	@Test
	public void AttributeValueNotExist() {

		UserEntity userElementCreator = new UserEntity(User.DEFAULT_USERNAME, User.EMAIL_FOR_TESTS,
				User.AVATAR_FOR_TESTS, User.PLAYER_ROLE, Playground.PLAYGROUND_NAME);
		userElementCreator.verifyUser();
		userService.addUser(userElementCreator);
		ElementTO[] elementForTest = { new ElementTO(new ElementEntity(Element.ELEMENT_QUESTION_KEY, 5, 6)) };
		elementService.addElementNoLogin(elementForTest[0].toEntity());
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + Playground.Function_10, ElementTO[].class,
				Playground.PLAYGROUND_NAME, userElementCreator.getEmail(), Element.TYPE_FIELD,
				"no_such_attribute_value");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}

	// url #10
	// "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}"

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
