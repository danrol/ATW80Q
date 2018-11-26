package playground.layout;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WebUITest {
	@LocalServerPort
	private int port;
	
	private String url;
	
	private RestTemplate restTemplate;
	/*
	 * DanielController:
	 * 
	 * 1. Register new user
	 * "/playground/users" POST
	 * 
	 * 6. Update element
	 * "/playground/elements/{userPlayground}/{email}/{playground}/{id}" PUT
	 * 
	 * 10. Get element containing attribute with specific value
	 * "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}" GET
	 * 
	 * 
	 * EdenDupontController
	 * 
	 * 2. Confirm User
	 * "/playground/users/confirm/{playground}/{email}/{code}" GET
	 * 
	 * 7. Get element
	 * "/playground/elements/{userPlayground}/{email}/{playground}/{id}" GET
	 * 
	 * 11. Request Server
	 * "/playground/activities/{userPlayground}/{email}" POST
	 * 
	 * EdenSharoniController
	 * 
	 * 
	 * */
}
