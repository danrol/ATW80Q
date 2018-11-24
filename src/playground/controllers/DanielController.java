package playground.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.activities.ActivityTO;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Message;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;
import playground.logic.UserTO;

@RestController
public class DanielController {
	
//	private Database database;
	private ElementService elementService;
	private UserService userService;
	
	
//	@Autowired
//	public void setDatabase(Database database) {
//		this.database = database;
//	}
	
	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/view_messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Message> viewMessages() {
		return Database.getMessages();
	}
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/playground/users",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserTO registerNewUser(@RequestBody NewUserForm newUserForm) {
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
		UserTO newUserTO = new UserTO(new UserEntity(newUserForm));
		if (userService.getUser(newUserForm.getEmail()) != null)
			return null;
		else {
		userService.addUser(newUserTO.toEntity());
		return newUserTO;
		}
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element, @PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground, @PathVariable("playground") String playground,
			@PathVariable("id") String id) {
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */
		
			System.out.println("Entered update");
			elementService.updateElementInDatabaseFromExternalElement(element.toEntity(), id, playground);
			System.out.println("updatePerformed");
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsByUserPlaygroundEmailAttributeNameValue
	(@RequestBody ActivityTO activity,@PathVariable("userPlayground") String userPlayground, 
			@PathVariable ("email") String email, @PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value) {
		/* function 10
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		System.out.println();
			return elementService.getElementsWithValueInAttribute(userPlayground, email, attributeName, value);
	}
	
	
	
	
	
	
	
	
	
	
}
