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
	
	
	@Test
	public void testUpdateElement() throws Exception{
//		Given Server is up
//		And database contains element with fields {userPlayground}, {email}, {playground}, {id}
//		When I PUT /playground/elements/{userPlayground}/{email}/{playground}/{id}
//			With headers:
//				Accept:application/json
//				content-type: application/json
//			With Body:
//			{"name": "stam", "id" : "123", "playground": "atw80", "type": "random type", 
//		"creatorPlayground":"Main_Playground", "creatorEmail":"naknik@taim.com", "location":{"x":1, "y":1},"creationDate":"11/12/1984", "expirationDate: "12/10/1990"} 
//		Then element with matching creatorPlayground, creatorEmail, playground, id will be updated with new element defined in JSON body 
//		
		System.out.println("Start update element test:");
		String userPlayground = "MainPlayground";
		String email = "nudnik@mail.ru";
		String playground = "atw80";
		String id = "123";
		
		ElementTO elementForTest = new ElementTO(id, playground, userPlayground, email);
//		String jsonStringToElement = "{\"name\": \"stam\", \"id\" : \"123\", \"playground\": \""+playground+"\", \"type\": \"random type\" "
//				+ ",\"creatorPlayground\":\""+userPlayground+"\", \"creatorEmail\":\""+email+ "\" ,\"id\":\""+id+"\"}";
//		ElementTO elementForTest = this.jsonMapper.readValue(jsonStringToElement, ElementTO.class);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, userPlayground, email, playground, id);
		
		System.out.println("Element for test: " + elementForTest);
		System.out.println("Element from database: "+this.database.getElement(id, playground));
		//TODO change to Entity
		ElementTO actualEntity = this.database.getElement(id, playground);
		assertThat(actualEntity).isNotNull();
		assertThat(actualEntity).isEqualToComparingFieldByField(elementForTest);
		}
	
	@Test
	public void testgetElementsByUserPlaygroundEmailAttributeNameValue(){
//		Given Server is up
//		And database contains element with fields {userPlayground}, {email}, {attributeName}, {value}
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
		
		ElementTO actualValue = this.restTemplate.getForObject(this.url + "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", 
				ElementTO.class, userPlayground, email, playground, id);
		
		System.out.println("Test searh, actualValue"+elementForTest.toString());
		
		assertThat(actualValue).isNotNull();
		assertThat(actualValue).isEqualToComparingFieldByField(elementForTest);
		
	}
	
}
