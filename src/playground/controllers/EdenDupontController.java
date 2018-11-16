package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.activities.ActivityTO;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.UserTO;

@RestController
public class EdenDupontController {
	
	@Autowired
	Database db;
	

	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/activities/{userPlayground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Object function11(@RequestBody ActivityTO activity, @PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground) 
		{
		/* function 11
		 * INPUT: ActivityTO
		 * OUTPUT: Object
		 */
		//TODO add activity to RequestBody
		return "Hello, " + username + "\n received in POST an activity with mail : " + email + " userPlayground: " + userPlayground + "\n activity:\n" + activity; 
		}

	
}
