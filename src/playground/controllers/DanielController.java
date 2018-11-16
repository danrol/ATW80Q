package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Playground_constants;
import playground.activities.ActivityTO;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.NewUserForm;
import playground.logic.UserTO;

@RestController
public class DanielController implements Playground_constants{
	
	private Database database;
	
	@Autowired
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/view_messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return this.database.getMessageBoard().viewMessagesBoard();
	}
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "playground/users",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserTO registerNewUser(NewUserForm newUserForm) {
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
		UserTO newUserTO = new UserTO(newUserForm.getUsername(), newUserForm.getEmail(), 
				newUserForm.getAvatar(), newUserForm.getRole(), PLAYGROUND_NAME);
		database.addUser(newUserTO);
		return newUserTO;
	} //TODO understand where from registerNewUser receives NewUserForm
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addNewElement(@RequestBody ElementTO element,@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,
			@PathVariable("id") String id) {
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */
		ElementTO newElementTO = new ElementTO( id, playground,	userPlayground, email);
		database.addElement(newElementTO);
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/activities/{userPlayground}/{email}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object saveActivityAsObject(ActivityTO activity, 
			@PathVariable("userPlayground") String userPlayground, @PathVariable ("email") String email) {
		/* function 11
		 * INPUT: ActivityTO
		 * OUTPUT: Object
		 */
		return new Object();
	}
	
	
	
	
	
	
	
	
	
	
}
