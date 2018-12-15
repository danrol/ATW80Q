package playground.layout;
import static org.assertj.core.api.Assertions.assertThat;
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

import playground.Constants;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JPATest {
	
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

	@Test
	public void addElementToJPA() {
		
		
		String playground="playground",creatorPlayground="creator",id="idOfElement";
		ElementEntity element = new ElementEntity(id,Constants.ELEMENT_NAME, playground,creatorPlayground,1,2);
		
		elementService.addElementNoLogin(element);
		assertThat(elementService.isElementInDatabase(element));
		assertThat(elementService.getElementNoLogin(ElementEntity.setSuperkey(element.getId(), element.getCreatorPlayground()))).isEqualTo(element);
	}
}
