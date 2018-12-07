package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import playground.Constants;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTest {
	private RestTemplate restTemplate;

	private ElementService elementService;
	private UserService userService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

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
	
	//******************************************************************************************//
	// url #5 /playground/elements/{userPlayground }/{email}  starts 
	@Test
	public void testPOSTNewElementIsAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		elementService.addElement(element);
		ArrayList <ElementEntity> arr= elementService.getElements();
		
		assertThat(arr.contains(element)).isTrue();
	}
	
	@Test
	public void testPOSTNewElementWithNoCreatorIsAdded() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element with empty creatorPlayground.
		 * Then: a new element is saved in the serviceElement.
		 */
		String playground="playground",creatorPlayground=" ",name="nameOfElement:(english hei 7)";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		
		elementService.addElement(element);
		assertThat(elementService.getElements().contains(element)).isTrue();
	}
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeHisUser() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update my user
		 * Then: changes are accepted
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", moderatorUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}

	@Test(expected = RuntimeException.class)
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsModerator() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update other user AND other user is moderator
		 * Then: I get changeUser exception
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE,	Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	
	@Test
	public void testChangeUserWhenRoleIsModeratorAndChangeOtherUserAndOtherUserIsPlayer() {
		/*
		 * Given: Server is up AND I PUT /playground/users/{playground}/{email}
		 * When: I am moderator AND want to update other user AND other user is player
		 * Then: changes are accepted
		 */
		UserEntity moderatorUser = new UserEntity("userTest", "userTest@gmail.com", "Test.jpg,", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		userService.addUser(moderatorUser);
		moderatorUser.verifyUser();

		UserEntity OtherUser = new UserEntity("userTest", "OtherUserTest@gmail.com", "Test.jpg,", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		

		this.restTemplate.put(this.url + "/playground/users/{playground}/{email}", OtherUser, Constants.PLAYGROUND_NAME, moderatorUser.getEmail());
	}
	// url #5 /playground/elements/{userPlayground }/{email}  finished
	//******************************************************************************************//
	@Test
	public void testPOSTNewElementsAreAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND i post new elements.
		 * Then: all elements are saved in the database.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity[] arrElements=new ElementEntity[3];
		arrElements[0]=new ElementEntity(name,playground,creatorPlayground,new Location("3,1"));
		arrElements[1]=new ElementEntity(name,playground,creatorPlayground,new Location("3,2"));
		arrElements[2]=new ElementEntity(name,playground,creatorPlayground,new Location("3,3"));
		
		
		elementService.addElements(arrElements, playground);
		assertThat(elementService.isElementInDatabase(arrElements[0])&&elementService.isElementInDatabase(arrElements[1])&&elementService.isElementInDatabase(arrElements[2]));
		
		
	}
	
	@Test
	public void testGETNewElementsFromDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND i post new elements.
		 * Then: all elements are saved in the database.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity[] arrElements=new ElementEntity[3];
		arrElements[0]=new ElementEntity(name,playground,creatorPlayground,new Location("3,1"));
		arrElements[1]=new ElementEntity(name,playground,creatorPlayground,new Location("3,2"));
		arrElements[2]=new ElementEntity(name,playground,creatorPlayground,new Location("3,3"));
		
		
		elementService.addElements(arrElements, playground);
		assertThat(elementService.isElementInDatabase(arrElements[0])&&elementService.isElementInDatabase(arrElements[1])&&elementService.isElementInDatabase(arrElements[2]));
		
		
	}
	@Test
	public void testPOSTNewElementsWithSameFieldsAreNotAddedDuplicatedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity[] arrElements=new ElementEntity[3];
		arrElements[0]=new ElementEntity(name,playground,creatorPlayground,new Location("3,1"));
		arrElements[1]=new ElementEntity(name,playground,creatorPlayground,new Location("3,1"));
		arrElements[2]=new ElementEntity(name,playground,creatorPlayground,new Location("3,3"));
		
		
		elementService.addElements(arrElements, playground);
		assertThat(elementService.isElementInDatabase(arrElements[0])&&(!elementService.isElementInDatabase(arrElements[1]))&&elementService.isElementInDatabase(arrElements[2]));
		
	}
	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with PUT test starts
	
	@Test
	public void testSuccessfullyUpdateElement() throws Exception{
		
		// 6.1
		UserEntity userElementCreator = new UserEntity("username", "email@email.com", "ava", 
				"PLAYER", "userPlayground");
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		ElementEntity updatedElementForTestEntity = 
				new ElementEntity("123", "userPlayground", "email@email.com",new Location(0,1));
		elementService.addElement(updatedElementForTestEntity);
		
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTestTO, "userPlayground", 
				"email@email.com", "userPlayground",  "123");
	    
		ElementEntity actualEntity = elementService.getElement("123", "forTest");

		
		assertThat(actualEntity).isNotNull();

		assertThat(actualEntity).isEqualToComparingFieldByField(updatedElementForTestTO.toEntity());
		}
	
	@Test(expected = RuntimeException.class)
	public void testUpdateNonExistingElement() {
		
		// 6.2
		ElementEntity elementEntityForTest = 
				new ElementEntity("123", "playground", "email@email.com",new Location(0,1));
		ElementTO elementForTest = new ElementTO(elementEntityForTest);
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, "userPlayground", 
				"email@email.com", "wrongPlayground",  "123");
	}
	
	@Test(expected = RuntimeException.class)
	public void testUpdateElementForNonExistingCreator() throws Exception{
		
		ElementEntity updatedElementForTestEntity = 
				new ElementEntity("123", "userPlayground", "wrong@email",new Location(0,1));
		elementService.addElement(updatedElementForTestEntity);
		
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		updatedElementForTestTO.setPlayground("forTest");
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTestTO, "userPlayground", 
				"test@test.com", "playgr",  "123");
		
		}
	

	// url #6 /playground/elements/{userPlayground}/{email}/{playground}/{id} with PUT test finished
	//******************************************************************************************//
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test started
	
	@Test(expected=RuntimeException.class)
	public void testGetElementCorrectLoginElementNotInDatabase() {
	/*
	 * 
	Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
	When user login details are correct and element is not in database
	Then I get an exception
		
	*/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	
	@Test
	public void testGetElementCorrectLoginElementExists() {
		/*
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are correct and element exists
		Then I get the element
		*/
		UserEntity u = new UserEntity(Constants.DEFAULT_USERNAME,Constants.EMAIL_FOR_TESTS,Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME);
		u.verifyUser();
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(1,7));
		this.userService.addUser(u);
		this.elementService.addElement(element);
		ElementTO el = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,Constants.EMAIL_FOR_TESTS,Constants.PLAYGROUND_NAME,"elementIdTest");
		assertThat(el).isNotNull();
		assertThat(el.getId()).isEqualTo(element.getId());
		assertThat(el.getPlayground()).isEqualTo(element.getPlayground());
	}

	@Test(expected=RuntimeException.class)
	public void testGetElementIncorrectLoginElementExists() {
		/*
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are incorrect and element exists
		Then I get an exception
		*/
		
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.verifyUser();
		this.userService.addUser(u);
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(5,7));
		this.elementService.addElement(element);
		
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	

	@Test(expected=RuntimeException.class)
	public void testGetElementIncorrectLoginElementNotInDatabase() {
	/*
	 * 
		Given the server is up and I GET /playground/elements/{userPlayground}/{email}/{playground}/{id}
		When user login details are incorrect and element is not in database
		Then I get an exception
		
	*/
		UserEntity u = new UserEntity("userTest","userTest@gmail.com","Test.jpg,", Constants.MODERATOR_ROLE ,Constants.PLAYGROUND_NAME, "1234");
		u.setVerified_user(Constants.USER_NOT_VERIFIED);
		this.userService.addUser(u);
		
		ElementEntity element = new ElementEntity("elementIdTest",Constants.PLAYGROUND_NAME,"elementTest@gmail.com", new Location(4,3));
		this.elementService.addElement(element);
		
		this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, Constants.PLAYGROUND_NAME,"userTestWrong@gmail.com",Constants.PLAYGROUND_NAME,"elementIdTest");
		
	}
	
	// url #7 /playground/elements/{userPlayground}/{email}/{playground}/{id} test finished
	
	//******************************************************************************************//
	// url #8 /playground/elements/{userPlayground }/{email}/all test started
	@Test
	public void testGETAllFromDatabase() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND database is not empty.
		 * Then: all elements are returned from the database.
		 */
		boolean flag=true;
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity[] arrElements=new ElementEntity[3];
		arrElements[0]=new ElementEntity(name,playground,creatorPlayground,new Location("3,1"));
		arrElements[1]=new ElementEntity(name,playground,creatorPlayground,new Location("3,2"));
		arrElements[2]=new ElementEntity(name,playground,creatorPlayground,new Location("3,3"));
		
		
		elementService.addElements(arrElements, playground);
		ArrayList elementsreturned=elementService.getElements();
		for(int i=0;i<arrElements.length;i++) {
			if(!elementsreturned.contains(arrElements[i])) {
				flag=false;
				break;
			}
			
		}
		assertThat(flag=true);
		
	}
	
	@Test
	public void testGETAllFromEmptyDatabase() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground }/{email}/all
		 * When: User is verified AND database is empty.
		 * Then: i get empty array of elements.
		 */
		
		
		ArrayList elementsreturned=elementService.getElements();
		assertThat(elementsreturned.isEmpty());
		
	}
	//////
	
	// url #8 /playground/elements/{userPlayground }/{email}/all test finished
	//******************************************************************************************//
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance} test started
	@Test
	public void testIfWeGETElementsFromDatabaseWithRightRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is above 0.
		 * Then: I get  ElementTO[] back.
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity element1=new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		ElementEntity element2=new ElementEntity(name,playground,creatorPlayground,new Location("2,1"));
		elementService.addElement(element1);
		double distance=7;
		assertThat(elementService.getAllElementsInRadius(element2,element2.getLocation().getX(),element2.getLocation().getY(),distance, 0, 10)).isNotNull();
	}
	
	@Test(expected=RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is negative.
		 * Then: I get NULL ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity element=new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		double distance=-1;
		elementService.getAllElementsInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10);
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is 0.
		 * Then: I get NULL ElementTO[].
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)";
		ElementEntity element=new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		double distance=0;
		assertThat(elementService.getAllElementsInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10)).isNull();
	}
	
	// url #9 /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance} test finished

	//******************************************************************************************//

	// url #10 "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" test starts
	
	@Test
	public void testSuccessfullyGetElementsByAttributeNameValue(){
/*		
		Given: the server is up
		AND element service contains contains element with attributeName = �attr3�:attributeValue=�attr3Val�
		With headers:	  Accept:application/json,  content-type: application/json
		When: I GET /playground/elements/creatorPlayground/nudnik@mail.ru/search/attr3/attr3Val
		Then: server returns  ElementTO[] array with element with creatorEmail = "nudnik@mail.ru",  value in attribute attributeName = �attr3�:attributeValue=�attr3Val�
*/
		
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", 
				"player", "creatorPlayground");
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity("123", 
				"userPlayground", "nudnik@mail.ru", new Location(1,0)))};
		
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest[0].setAttributes(testMap);
		elementService.addElement(elementForTest[0].toEntity());
		
		ElementTO[] forNow = this.restTemplate.getForObject(url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class, 
				"creatorPlayground", "nudnik@mail.ru", "attr3", "attr3Val");
		
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToComparingFieldByField(elementForTest[0]);
	}
	
	@Test
	public void testAttributeNotExist() {
		/*		
		Given: the server is up
		AND element service doesn't contains element with attributeName = �noSuchAttribute�
		With headers:	  Accept:application/json,  content-type: application/json
		When: I GET /playground/elements/creatorPlayground/nudnik@mail.ru/search/attr3/attr3Val
		Then: server returns  emtpty ElementTO[] array with element with creatorEmail = "nudnik@mail.ru",  value in attribute attributeName = �attr3�:attributeValue=�attr3Val�
*/
		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", 
				"player", "creatorPlayground");
		
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity("123", 
				"userPlayground", "nudnik@mail.ru", new Location(1,0)))};
		
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				"creatorPlayground", "nudnik@mail.ru", "noSuchAttribute", "attr3Val");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}
	
	@Test
	public void testValueInAttributeNotExist() {

		UserEntity userElementCreator = new UserEntity("name", "nudnik@mail.ru", "ava", 
				"player", "creatorPlayground");
		
		userElementCreator.setVerified_user(Constants.USER_VERIFIED);
		userService.addUser(userElementCreator);
		
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity("123", 
				"userPlayground", "nudnik@mail.ru", new Location(1,0)))};
		
		elementForTest[0].setCreatorPlayground("creatorPlayground");
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("randomAttribute","attr3Val");
		
		ElementTO[] responseEntity = restTemplate.getForObject(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				"creatorPlayground", "nudnik@mail.ru", "randomAttribute", "wrongValue");
		assertThat(responseEntity).isEqualTo(new ElementTO[0]);
	}
	

	// url #10 "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" test finished

	//******************************************************************************************//

}
