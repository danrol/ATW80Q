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
		
		NewUserForm postUser = new NewUserForm("danMadMan@gmail.com", "random name", "pikachu", "teacher");
		UserTO userTOToCheckWith = new UserTO(postUser);
		System.out.println(userTOToCheckWith.toString());

		UserTO actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users",
				postUser, UserTO.class);
		System.out.println("before actual");
		System.out.println("Actual value: "+actualReturnedValue.toString());
		System.out.println("after actual");
		assertThat(actualReturnedValue.toString())
		.isNotNull()
		.isEqualTo(userTOToCheckWith.toString());
	}
	
	
	@Test
	public void testAddNewElement() throws Exception{
		
//		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}", request);
	}
	
	@Test
	public void testgetElementsByUserPlaygroundEmailAttributeNameValue() throws Exception{
		
	}
	
}
