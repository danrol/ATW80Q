package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.database.Database;
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
			if(user.getPlayground().equals(Constants.PLAYGROUND_NAME))
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
	
}
