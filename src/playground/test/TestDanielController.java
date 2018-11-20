package playground.test;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

import playground.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.*;
import playground.logic.NewUserForm;
import playground.logic.UserTO;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestDanielController {
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	private String url;

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
	public void testRegisterNewUserCreatesAndReturnsNewUserTOFromNewUserForm() throws Exception{
//		Given Server is up
//		When I POST /playground/users
//		  with headers:
//			  Accept:application/json
//			  content-type: application/json
//		  With Body
//		  {"email" : "danMadMan@gmail.com", "username":"random name", "avatar":"pikachu", "role":"teacher"}
//		Then The response body contains new UserTO with the "email" : "danMadMan@gmail.com", "avatar:"pikachu", "role":"teacher""
		String emailForTest = "danMadMan@gmail.com";
		String nameForTest = "random name";
		String avatarForTest = "pikachu";
		String roleForTest = "teacher";
		
		
		NewUserForm postUser = new NewUserForm(emailForTest, nameForTest, avatarForTest, roleForTest);
		String jsonFromNewUser = this.jsonMapper.writeValueAsString(new UserTO(postUser));
		
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users",
				postUser, String.class);
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualTo(jsonFromNewUser);
	}
	
	//ToDO
	@Test
	public void testAddNewElement() throws Exception{
		
//		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}", request);
	}
	
	@Test
	public void testgetElementsByUserPlaygroundEmailAttributeNameValue() throws Exception{
		
	}
	
}
