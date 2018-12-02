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
	
	private ElementService elementService;
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
	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	
	//TODO add non RuntimeException tests
	
	@Test
	public void testIfWeGETElementsFromDatabaseWithRightRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is above 0.
		 * Then: I get  ElementTO[] back.
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element1=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		ElementTO element2=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("2,1")));
		elementService.addElement(element1.toEntity());
		double distance=7;
		assertThat(elementService.getAllElementsTOInRadius(element2,element2.getLocation().getX(),element2.getLocation().getY(),distance, 0, 10)).isNotNull();
	}
	
	@Test(expected=RuntimeException.class)
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is negative.
		 * Then: I get NULL ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=-1;
		elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10);
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		/*
		 * Given: Server is up AND I GET /playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}
		 * When: User is verified AND distance is 0.
		 * Then: I get NULL ElementTO[].
		 */
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("1,2")));
		double distance=0;
		assertThat(elementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance, 0, 10)).isNull();
	}
	
	@Test
	public void testPOSTNewElementIsAddedToDatabase() {
		/*
		 * Given: Server is up AND I POST /playground/elements/{userPlayground }/{email}
		 * When: User is verified AND i post new element.
		 * Then: a new element is saved in the serviceElement.
		 */
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementEntity element =new ElementEntity(name,playground,creatorPlayground,new Location("1,2"));
		elementService.addElement(element);
		ArrayList <ElementEntity> arr= elementService.getElements();
		
		assertThat(arr.contains(element)).isTrue();
	}
	
	@Test
	public void testPOSTNewElementWithNoCreatorIsAdded() {
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
	
	
	

}
