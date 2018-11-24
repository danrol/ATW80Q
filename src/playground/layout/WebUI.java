package playground.layout;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.database.Database;
import playground.exceptions.ConfirmException;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.NewUserForm;
import playground.logic.UserService;


@RestController
public class WebUI {
	
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;
	/*
	 * 
	 * Please see DanielController, EdenDupontController, EdenSharoniController, EliaController for API 
	 */
	
	/*
	 * 
	 * 
	 * Project API 
	 * 
	 * 
	 * */
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/view_rules",
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public String getGameRules() {
		return Constants.GAME_RULES;
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/users/add_message",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addMessage(@RequestBody String newMessage) {
		
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/view_messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return "string";
	}
	
}