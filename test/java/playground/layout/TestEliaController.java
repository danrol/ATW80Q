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
import playground.*;
import playground.database.Database;
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.MessageService;
import playground.logic.UserEntity;
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
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("12")));
		double distance=-1;
		assertThat(ElementService.getAllElementsTOInRadius(element,element.getLocation().getX(),element.getLocation().getY(),distance));
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("12")));
		double distance=0;
		assertThat(db.getAllElementsInRadius(element, distance));
	}
	
	@Test
	public void testPOSTNewElement() {
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(new ElementEntity(name,playground,creatorPlayground,new Location("12")));
		db.addElement(element);
		assertThat(db.getElements().contains(element));
	}
	

}
