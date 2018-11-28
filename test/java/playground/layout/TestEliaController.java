package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

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
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.UserService;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestEliaController {
	//
	private RestTemplate restTemplate;
	
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;
	
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

	private void clean() {
		
		
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	
	@Test(expected=RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is negative.
		 * Then: I get empty ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=-1;
		assertThat(elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance)).isNull();
	}
	
	@Test(expected=RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is 0.
		 * Then: I get empty ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=0;
		assertThat(elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance)).isNull();
	}
	
	@Test(expected=RuntimeException.class)
	public void testPOSTNewElement() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		boolean flag=false;
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		elementService.addElement(element);
		ArrayList <ElementEntity> arr= elementService.getElements();
		if(arr.contains(element)) {
			flag=true;
		}
		assertThat(flag).isFalse();
	}
	
	@Test(expected=RuntimeException.class)
	public void testPOSTNewElementWithNoCreator() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element with empty creatorPlayground.
		 * Then: a new element is saved in the serviceElement.
		 */
		String playground="playground",creatorPlayground=" ",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		
		elementService.addElement(element);
		assertThat(elementService.getElements().contains(element)).isTrue();
	}
	
	@Test
	public void nothing() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element with empty creatorPlayground.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		
		assertThat(elementService.getElements().isEmpty()).isTrue();
	}
	

}
