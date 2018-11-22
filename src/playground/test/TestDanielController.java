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
		// Jackson init
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
		String emailForTest = "wrongEmail";
		String nameForTest = "random name";
		String avatarForTest = "pikachu";
		String roleForTest = "teacher";
		NewUserForm postUserForm = new NewUserForm(emailForTest, nameForTest, avatarForTest, roleForTest);
			
		String testValue = jsonMapper.writeValueAsString(new UserTO(postUserForm));
	}
	
	@Test
	public void testUserAlreadyExists() {
		String emailForTest = "danMadMan@gmail.com";
		String nameForTest = "random name";
		String avatarForTest = "pikachu";
		String roleForTest = "teacher";
		
		NewUserForm postUserForm = new NewUserForm(emailForTest, nameForTest, avatarForTest, roleForTest);
		UserTO userToAdd = new UserTO(postUserForm);
		this.database.addUser(userToAdd);
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, String.class);
		assertThat(actualReturnedValue).isNull();
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{
		String emailForTest = "danMadMan@gmail.com";
		String nameForTest = "random name";
		String avatarForTest = "pikachu";
		String roleForTest = "teacher";
		
		
		NewUserForm postUserForm = new NewUserForm(emailForTest, nameForTest, avatarForTest, roleForTest);
		String testValue = jsonMapper.writeValueAsString(new UserTO(postUserForm));
		
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, String.class);
		System.out.println(actualReturnedValue);
		//TODO fix problem with mapping
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualTo(testValue);
		
		
		//TODO add gherkin database check
		assertThat(jsonMapper.writeValueAsString(this.database.getUser(emailForTest))).isEqualTo(testValue);	
		//TODO check if its right to use JSON for tests this way
	}
	
	
	public void createVerifiedUserFromEmailAndPlayground(String email, String creatorPlayground) {
		UserTO u = new UserTO("bestNameEvar", email, "pikachuAvatar", Constants.MODERATOR_ROLE, creatorPlayground);
		u.setVerified_user(Constants.USER_VERIFIED);
		this.database.addUser(u);
	}
	
	@Test(expected = RuntimeException.class)
	public void testWrongElementPassedForUpdate() {
		String userPlayground = "MainPlayground";
		String email = "nudnik@mail.ru";
		String playground = "atw80";
		String id = "123";
		
		ElementTO elementForTest = new ElementTO(id, playground, userPlayground, email);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, userPlayground, email, playground, id);

	}
	@Test
	public void testSuccessfullyUpdateElementWith() throws Exception{
		String userPlayground = "MainPlayground";
		String email = "nudnik@mail.ru";
		String playground = "atw80";
		String id = "123";
		
		createVerifiedUserFromEmailAndPlayground(email, userPlayground);
		ElementTO updatedElementForTest = new ElementTO(id, playground, userPlayground, email);
		
		this.database.addElement(new ElementTO(id, playground, userPlayground, email));
		
		HashMap<String, Object> attributes = new HashMap<>();
		attributes.put("attribute1","attr1Value");
		attributes.put("attribute2","attr2Value");
		attributes.put("attr3","attr3Val");
		updatedElementForTest.setAttributes(attributes);
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTest, userPlayground, email, playground, id);
		
		//TODO change to Entity
		
		ElementTO actualEntity = this.database.getElement(id, playground);
//		assertThat(actualEntity).isNotNull();
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
	
	
	@Test
	public void testSuccessfullyGetElementsByUserPlaygroundEmailAttributeNameValue(){
//		Given Server is up
//		And database contains element with fields {userPlayground}, {email}, {attributeName}, {value}
//		And database contains verified user with {email}
//		When I GET /playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}
//			With headers:
//				Accept:application/json
//				content-type: application/json
//			With parametrs:
//			userPlayground = "sht", email = "agile@bumbamail.ru", attributeName = "attribute", value = "random_value"
//		Then element with matching creatorPlayground, creatorEmail, playground, id will be updated with new element defined in JSON body 
//		
		String userPlayground = "MainPlayground";
		String email = "nudnik@mail.ru";
		String playground = "atw80";
		String id = "123";
		
		ElementTO elementForTest = new ElementTO(id, playground, userPlayground, email);
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest.setAttributes(testMap);
		this.database.addElement(elementForTest);
		System.out.println("Check that element added" + this.database.getElements().toString());
		
		
		
//		System.out.println("Print elements from http"+actualValue);
		
//		System.out.println("Test searh, actualValue"+elementForTest.toString());
		
		ElementTO forNow = this.restTemplate.getForObject(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO.class, userPlayground, email,
				"attr3", "attr3Val");
	    
		System.out.println("forNow"+forNow.toString());
//		assertThat(actualValue).isNotNull();
//		assertThat(actualValue).isEqualToComparingFieldByField(elementForTest);

	}
	
	
}
