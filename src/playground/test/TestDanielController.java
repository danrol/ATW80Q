package playground.test;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

import playground.Constants;
import playground.database.Database;

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
	private Database db;

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
		UserTO userForTest = new UserTO(postUserForm);
		
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users",
				postUserForm, String.class);
		System.out.println(actualReturnedValue);
		//TODO fix problem with mapping
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualTo(jsonMapper.writeValueAsString(userForTest));
		
		
		//TODO add gherkin database check
		assertThat(jsonMapper.writeValueAsString(this.db.getUser(emailForTest))).isEqualTo(jsonMapper.writeValueAsString(userForTest));	
		
	}
	
	//ToDO
	@Test
	public void testAddNewElement(){
//		Given Server is up
//		When I PUT /playground/elements/{userPlayground}/{email}/{playground}/{id}
//			With headers:
//				Accept:application/json
//				content-type: application/json
//			With Body:
//			{"newAttributeName1": "stam", newAttributeName1:"line"} 
//		Then element with matching creatorPlayground, creatorEmail, playground, id will be updated with added attributes {"newAttributeName1": "stam", newAttributeName1:"line"} 
//			
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}", request);
	}
	
	@Test
	public void testgetElementsByUserPlaygroundEmailAttributeNameValue(){
		
	}
	
}
