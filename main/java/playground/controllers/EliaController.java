package playground.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.logic.ElementEntity;
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
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public ElementTO setUser (@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground")String userPlayground)throws ConfirmException  {
		
		//TODO change to login check through userService
		userService.login(userPlayground,email);
		elementService.addElement(element.toEntity());
		return new ElementTO(this.elementService.getElement(element.getId(),element.getPlayground()));
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}/all",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public ElementTO[] setAllUsers (
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
    		@RequestBody ElementTO[] element,
    		@PathVariable("email") String email,
    		@PathVariable("userPlayground")String userPlayground)throws ConfirmException  {
		
		userService.login(userPlayground,email);
		elementService.addElements(element, userPlayground);
		return elementService.getElementsByCreatorPlaygroundAndEmail(userPlayground, email, page, size);
			
		
	}
	
	
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAtLocation(
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestBody ElementTO element,
			@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("distance") double distance, 
			@PathVariable("x") int x, 
			@PathVariable("y") int y)throws ConfirmException{
		
		//TODO change to login check through userService
		userService.login(userPlayground,email);
		
		return elementService.getAllElementsTOInRadius(element,x,y,distance, page, size);
	}
	
	/*
	 * here for tests - do not copy
	 * */
	

	
	
}
