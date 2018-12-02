package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import playground.Constants;
import playground.exceptions.ChangeUserException;
import playground.exceptions.LoginException;
import playground.layout.UserTO;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;


@RestController
public class EdenSharoniController {

	private ElementService elementService;
	private UserService userService;
	
	
	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/login/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
		/*
		 * function 3
		 * INPUT: NONE OUTPUT: UserTO
		 */
		UserEntity u = this.userService.getUser(email);
		if (u != null) {
			if (u.getPlayground().equals(playground)) {
				if (u.isVerified()) {
					return new UserTO(u);
				} else {
					throw new LoginException("User is not verified.");
				}
			} else {
				throw new LoginException("User does not belong to the specified playground.");
			}

		} else {
			throw new LoginException("Email is not registered.");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/playground/users/{playground}/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody UserEntity user, @PathVariable("email") String email,
			@PathVariable("playground") String playground) {
		/*
		 * function 4 INPUT: UserTO OUTPUT: NONE
		 */
		login(playground, email);
		if (userService.getUser(email).getRole().equals(Constants.MODERATOR_ROLE)) {
			if(user.getEmail().equals(email)) {
				userService.updateUser(user);
			}
			else if (!user.getRole().equals(Constants.MODERATOR_ROLE)) {
				userService.updateUser(user);
			} else {
				throw new ChangeUserException("Moderator cannot change other moderator user");
			}
		} else if (userService.getUser(email).getRole().equals(Constants.PLAYER_ROLE)) {
			if (email.equals(user.getEmail())) {
				userService.updateUser(user);
			} else {
				throw new ChangeUserException("PLAYER_ROLE cannot change other users information");
			}
		} else {
			throw new ChangeUserException("invalid role " + userService.getUser(email).getRole());
		}
	}

}
