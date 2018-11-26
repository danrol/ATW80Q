package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RestController
public class EliaController {
	
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

	@RequestMapping(
			method=RequestMethod.GET,
			path="elia_check",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return "Elia";
	}

	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public ElementTO setUser (@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground")String userPlayground)throws ConfirmException  {
		
		login(userPlayground, email);
		
		elementService.addElement(element.toEntity());
		return new ElementTO(this.elementService.getElement(element.getId(),element.getPlayground()));
	}
	
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAtLocation(@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("distance") double distance, @PathVariable("x") int x, @PathVariable("y") int y)throws ConfirmException{
		
		login(userPlayground, email);
		
		if(distance<0)
			throw new RuntimeException("Negative distance (" + distance + ")");
		
		ElementTO[] elementsInRange= elementService.getAllElementsTOInRadius(element,x,y,distance);
		return elementsInRange;
	}
	
	/*
	 * here for tests - do not copy
	 * */
	

	@RequestMapping(method = RequestMethod.GET, path = "/login2", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
		/*
		 * function 3INPUT: NONE OUTPUT: UserTO
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
	
}


