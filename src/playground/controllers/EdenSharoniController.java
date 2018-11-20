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
public class EdenSharoniController {
	@Autowired
	Database db;

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
}
