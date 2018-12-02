package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;

import playground.logic.ElementService;

import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RestController
public class DanielController {
	
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
			method = RequestMethod.POST,
			path = "/playground/users",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserTO registerNewUser(@RequestBody NewUserForm newUserForm) {
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
		
		userService.addUser(new UserEntity(newUserForm));
		return new UserTO(new UserEntity(newUserForm));
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
//			userService.login(userPlayground, email);
			elementService.updateElementInDatabaseFromExternalElement(element.toEntity(), userPlayground, 
					playground, id);
			System.out.println("updatePerformed");
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsByAttributeNameValue(
			@RequestBody ActivityTO activity, 
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@PathVariable("userPlayground") String userPlayground, 
			@PathVariable ("email") String email, 
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value) {
		/* function 10
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		System.out.println();
//		userService.login(userPlayground, email);
		return elementService.getElementsWithValueInAttribute(userPlayground, email, attributeName, value, page, size);
				
	}
	
	
	
	
	
	
	
	
	
	
}
