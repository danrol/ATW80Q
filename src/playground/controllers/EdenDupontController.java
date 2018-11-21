package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.activities.ActivityTO;
import playground.controllers.EdenSharoniController;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.ConfirmException;
import playground.logic.LoginException;
import playground.logic.UserTO;


@RestController
public class EdenDupontController {
	
	@Autowired
	Database db;
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/confirm/{playground}/{email}/{code}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTO verifyUser(@PathVariable("playground") String playground, @PathVariable("email") String email, 
			@PathVariable("code") String code) throws ConfirmException
		{
		/* function 2
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		UserTO user = this.db.getUser(email);
		if(user !=null) {
			if(user.getPlayground().equals(playground))
			{
				String VerificationCode = user.getVerificationCode();
				if (VerificationCode.equals(code))
					{
					user.verifyUser();
					}
				else
					{
						throw new ConfirmException("Invalid verification code");
					}
			}
				else
			{
					throw new ConfirmException("User: " + user.getEmail() +" does not belong to the specified playground ("+playground+")");
			}
		}
			else
			{
				throw new ConfirmException("Email is not registered.");
			}
		return user;
		}
	


	
	//throws exception if element is not found
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,@PathVariable("id") String id) 
		{
		/* function 7
		 * INPUT: NONE
		 * OUTPUT: ElementTO
		 */
		ElementTO element = null;
		login(userPlayground,email);
		//if login succeeded, get element
		element = db.getElement(id, playground);
		if(element == null)
			throw new RuntimeException("Could not find specified element (id=" + id +") in " + playground);
		return element;
		}

	
	//*****************************************
	//do not copy this - originally from EdenSharoni
	//*****************************************
	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/login/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
		/*
		 * function 3INPUT: NONE OUTPUT: UserTO
		 */
		UserTO u = this.db.getUser(email);
		if (u != null) {
			if (u.isVerified()) {
				return u;
			} else {
				throw new LoginException("User is not verified");
			}
		} else {
			throw new LoginException("Email is not registered.");
		}
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/activities/{userPlayground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Object getActivity(@RequestBody ActivityTO activity, @PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground) 
		{
		/* function 11
		 * INPUT: ActivityTO
		 * OUTPUT: Object
		 */
		//TODO add activity to RequestBody
		String s = new String("Hello, " + Constants.DEFAULT_USERNAME + "\n received in POST an activity with mail : " + email + " userPlayground: " + userPlayground + "\n activity:\n" + activity);
		return s; 
		}
	
}
