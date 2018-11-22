package playground.test;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import playground.Constants;
import playground.database.Database;
import playground.elements.ElementTO;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import playground.logic.NewUserForm;
import playground.logic.UserTO;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestDanielController {
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	private String url;
	
	@Autowired
	private Database database;

	private ObjectMapper jsonMapper;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port;
		System.err.println(this.url);
		this.jsonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		
	}

	@After
	public void teardown() {
		database.cleanDatabase();
	}
	
	@Test(expected=RuntimeException.class)
	public void testRegisterNewUserWithWrongEmail() throws JsonProcessingException{
		NewUserForm postUserForm =  new NewUserForm("wrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		jsonMapper.writeValueAsString(new UserTO(postUserForm));
	}
	
	@Test
	public void testUserAlreadyExists() {
		NewUserForm postUserForm =  new NewUserForm(Constants.EMAIL_FOR_TESTS, Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		UserTO userToAdd = new UserTO(postUserForm);
		this.database.addUser(userToAdd);
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, String.class);
		assertThat(actualReturnedValue).isNull();
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{
		NewUserForm postUserForm = new NewUserForm(Constants.EMAIL_FOR_TESTS, Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		String testValue = jsonMapper.writeValueAsString(new UserTO(postUserForm));
		
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, String.class);
		//TODO fix problem with mapping
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualTo(testValue);
		
		assertThat(jsonMapper.writeValueAsString(this.database.getUser(Constants.EMAIL_FOR_TESTS))).isEqualTo(testValue);	
		//TODO check if its right to use JSON for tests this way
	}
	
	
	public void createVerifiedUserFromEmailAndPlayground(String email, String creatorPlayground) {
		UserTO u = new UserTO(Constants.DEFAULT_USERNAME, email, Constants.AVATAR_FOR_TESTS, Constants.MODERATOR_ROLE, creatorPlayground);
		u.setVerified_user(Constants.USER_VERIFIED);
		this.database.addUser(u);
		System.out.println(u.toString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testWrongElementPassedForUpdate() {

		ElementTO elementForTest = new ElementTO(
				Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, 
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.ID_FOR_TESTS);

	}
	@Test
	public void testSuccessfullyUpdateElement() throws Exception{
	
		createVerifiedUserFromEmailAndPlayground(Constants.EMAIL_FOR_TESTS, Constants.CREATOR_PLAYGROUND_FOR_TESTS);
		ElementTO updatedElementForTest = new ElementTO(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS);
		
		this.database.addElement(new ElementTO(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS));
		
		HashMap<String, Object> attributes = new HashMap<>();
		attributes.put("attribute1","attr1Value");
		attributes.put("attribute2","attr2Value");
		attributes.put("attr3","attr3Val");
		updatedElementForTest.setAttributes(attributes);
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTest, Constants.CREATOR_PLAYGROUND_FOR_TESTS, 
				Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.ID_FOR_TESTS);
		//TODO change to Entity
		
		System.out.println("Arrived");
		ElementTO actualEntity = this.database.getElement(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME);
		
		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToComparingFieldByField(updatedElementForTest);
		assertThat(actualEntity.getAttributes()).isEqualTo(updatedElementForTest.getAttributes());
		}
	
	@Test(expected=RuntimeException.class)
	public void testAttributeNotExist() {
		ResponseEntity<ElementTO[]> responseEntity = restTemplate.getForEntity(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class);
		ElementTO[] elements = responseEntity.getBody();
		assertThat(elements).isNull();
	}
	
	public String[] getGenericDataForTests() {
		return new String []{"MainPlayground", "nudnik@mail.ru","atw80", "123"};
	}
	
	@Test
	public void testSuccessfullyGetElementsByUserPlaygroundEmailAttributeNameValue(){
		ElementTO elementForTest = new ElementTO(Constants.ID_FOR_TESTS, 
				Constants.PLAYGROUND_NAME, Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS);
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest.setAttributes(testMap);
		this.database.addElement(elementForTest);
		System.out.println("Check that element added" + this.database.getElements().toString());
		
		ElementTO forNow = this.restTemplate.getForObject(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO.class, 
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS, "attr3", "attr3Val");
	    
		System.out.println("forNow"+forNow.toString());
//		assertThat(actualValue).isNotNull();
//		assertThat(actualValue).isEqualToComparingFieldByField(elementForTest);

	}
	
	
}
