package layout;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import elements.Element;

import application.Playground_constants;
import database.Database;
import playground.logic.Message;
import users.User;


@RestController
public class WebUI implements Playground_constants {
	
	@Autowired
	private Database db;
	
	private String defaultPlayground = "atw80q";
	

	/*
	 * 
	 * 
	 * General API 
	 * 
	 * 
	 * */
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/users",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void newUserForm(String email, String username, String avatar, @PathVariable("role") String role, String playground) 
		{
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
				this.db.addUser(new User(username, email, avatar, role, playground));
		}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/confirm/{playground}/{email}/{code}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages1(@PathVariable("playground") String playground, @PathVariable("email") String email, 
			@PathVariable("code") String code) 
		{
		/* function 2
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		

		if(this.db.getUsers().containsKey(email)) {
			if(this.db.getUsers().get(email).getVerificationCode() == code)
				return "verification passed";
			else
				return "wrong verification code";
		}
			else
				return "wrong email";
		}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void login(@PathVariable("playground") String playground, 
			@PathVariable("email") String email) {
		/* function 3
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		
		//TODO add try catch
		this.db.getUsers().get(email).setStatus(ONLINE);
		
	}
	

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/users/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void changePlayground(@PathVariable("email") String email,@PathVariable("playground") String playground) 
		{
		/* function 4
		 * INPUT: UserTO
		 * OUTPUT: NONE
		 */
		
		this.db.getUsers().get(email).setPlayground(playground);
		//return this.db.getLessons();
		}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void function5(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground) 
		{
		/* function 5
		 * INPUT: ElementTO
		 * OUTPUT: ElementTO
		 */
		
		//this.db
		}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void function6(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,@PathVariable("id") int id) 
		{
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */
		}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void function7(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,@PathVariable("id") int id) 
		{
		/* function 7
		 * INPUT: NONE
		 * OUTPUT: ElementTO
		 */
		}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Element[] returnAllElementsByEmailAndCreatorPlayground(@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground) 
		{
		// returns all element with the same playground and email as in url
		/* function 8
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		
		return this.db.getAllElementsByEmailAndCreatorPlayground(userPlayground, email);
		
		}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void function9(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,
			@PathVariable("x") int x_point,@PathVariable("y") int y_point,@PathVariable("distance") double distance ) 
		{
		/* function 9
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void function10(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,
			@PathVariable("attributeName") String attributeName,@PathVariable("value") String value) 
		{
		/* function 10
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/activities/{userPlayground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void function11(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground) 
		{
		/* function 11
		 * INPUT: ActivityTO
		 * OUTPUT: Object
		 */
		}
	
	/*
	 * 
	 * 
	 * Project API 
	 * 
	 * 
	 * */
	@RequestMapping(
			method=RequestMethod.GET,
			path="/view_rules",
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public String getGameRules() {
		return this.db.getGameRules();
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/add_message",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addMessage(@RequestBody String newMessage) {
		this.db.getMessageBoard().writeMessage(newMessage);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/view_messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return this.db.getMessageBoard().viewMessagesBoard();
	}
	
}
