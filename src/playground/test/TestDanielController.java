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
	
	@Test
	public void testRegisterNewUser() throws Exception{
//		Given Server is up
//		When I POST /playground/users
//		  With headers:
//			  Accept:application/json
//			  content-type: application/json
//		  With Body:
//		  {"email" : "danMadMan@gmail.com", "username":"random name", "avatar":"pikachu", "role":"teacher"}
//		Then the response body contains new UserTO with the "email" : "danMadMan@gmail.com", "avatar:"pikachu", "role":"teacher""
//			And database contains new UserTO with the same fields as in the body
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
	
	
	public void createVerifiedUserFromEmail(String email) {
		UserTO u = new UserTO("bestNameEvar", email, "pikachuAvatar", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		u.setVerified_user(Constants.USER_VERIFIED);
		this.database.addUser(u);
	}
	
	@Test
	public void testUpdateElement() throws Exception{
//		Given Server is up
//		And database contains element with fields {userPlayground}, {playground}, {id}
//		And database contains verified user with {email}
//		When I PUT /playground/elements/{userPlayground}/{email}/{playground}/{id}
//			With headers:
//				Accept:application/json
//				content-type: application/json
//		Then element with matching creatorPlayground, creatorEmail, playground, id will be updated with new element defined in JSON body 
//		
		String userPlayground = "MainPlayground";
		String email = "nudnik@mail.ru";
		String playground = "atw80";
		String id = "123";
		
		ElementTO elementForTest = new ElementTO(id, playground, userPlayground, email);
		createVerifiedUserFromEmail(email);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, userPlayground, email, playground, id);
		
		//TODO change to Entity
		ElementTO actualEntity = this.database.getElement(id, playground);
//		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToComparingFieldByField(elementForTest);
		}
	
	@Test
	public void testGetElementsByUserPlaygroundEmailAttributeNameValue(){
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
		System.out.println("Test search, element for test"+elementForTest.toString());
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest.setAttributes(testMap);
		this.database.addElement(elementForTest);
		System.out.println("Check that element added" + this.database.getElements().toString());
		
//		ResponseEntity<ElementTO[]> response = restTemplate.exchange(
//				  this.url + "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
//				  HttpMethod.GET,
//				  null,
//				  new ParameterizedTypeReference<ElementTO[]>(){});
//		ElementTO[] actualValue = response.getBody();
		System.out.println("before response");
		ResponseEntity<ElementTO[]> responseEntity = restTemplate.getForEntity(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class);
		ElementTO[] elements = responseEntity.getBody();
//		ElementArray response = this.restTemplate.getForObject(this.url + 
//				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementArray.class, 
//				userPlayground, email, "attribute1", "attr1Value");
		System.out.println("print"+responseEntity);
//		ElementTO[] elementsArr = response.getElements();
		
		System.out.println("Print elements from http"+elements);
		
		System.out.println("Test searh, actualValue"+elementForTest.toString());
		
		assertThat(elements).isNotNull();
//		assertThat(actualValue).isEqualToComparingFieldByField(elementForTest);
		
	}
	
//	 class ElementArray{
//		private ElementTO[] elements;
//		
//		public ElementArray() {
//			elements = new ElementTO[1];
//		}
//
//		public ElementTO[] getElements() {
//			return elements;
//		}
//
//		public void setElements(ElementTO[] elements) {
//			this.elements = elements;
//		}
		
//	}
}
