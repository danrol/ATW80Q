package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.database.Database;
import playground.elements.ElementTO;
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
			@PathVariable("code") String code) 
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
						throw new RuntimeException("invalid verification code");
					}
			}
				else
			{
					throw new RuntimeException("User does not belong to the specified playground");
			}
		}
			else
			{
				throw new RuntimeException("Email is not registered.");
			}
		return user;
		}
	

	
	//method from Eden's controller 
	//DO NOT COPY!!!!!!!!! here for tests
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
				throw new RuntimeException("User is not verified");
			}
		} else {
			throw new RuntimeException("Email is not registered.");
		}
	}
	//DO NOT COPY!!!!!!!!!
	
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
		//logins user
		login(userPlayground,email);
		//if login succeeded, get element
			element = db.getElement(id, playground);
		if(element == null)
			throw new RuntimeException("Could not find specified element (id=" + id +") in " + playground);
		return element;
		}
}
