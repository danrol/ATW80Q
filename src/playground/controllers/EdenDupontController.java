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
import playground.database.Database;
import playground.elements.ElementTO;
import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;
import playground.logic.UserTO;


@RestController
public class EdenDupontController {
	
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;
	

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
		UserEntity user = this.userService.getUser(email);
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
		return user.toTO();
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
		ElementEntity element = null;
		login(userPlayground,email);
		//if login succeeded, get element
		element = elementService.getElement(id, playground);
		if(element == null)
			throw new RuntimeException("Could not find specified element (id=" + id +") in " + playground);
		return element.toTO();
		}

	
	//*****************************************
	//do not copy this - originally from EdenSharoni
	//*****************************************
	@RequestMapping(method = RequestMethod.GET, path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public void login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
		/*
		 * function 3INPUT: NONE OUTPUT: UserTO
		 */

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
		String s = new String("FHello, " + Constants.DEFAULT_USERNAME + "\n received in POST an activity with mail : " + email + " userPlayground: " + userPlayground + "\n activity:\n" + activity);
		return s; 
		}
}
