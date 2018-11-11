package layout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import elements.ElementTO;

import playground.Playground_constants;
import playground.logic.Location;
import playground.logic.NewUserForm;
import database.Database;
import users.UserTo;


@RestController
public class WebUI implements Playground_constants {
	
	@Autowired
	private Database db;
	private String defaultPlaygroundName;
	private String username;

	@Value("${playground.name:no-playground}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		this.defaultPlaygroundName = defaultPlaygroundName;
	}
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		this.username = defaultUsername;
	}
	
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
	public UserTo addNewUser(@RequestBody NewUserForm form) 
		{
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
				UserTo u = new UserTo(form.getEmail(),form.getUsername(), form.getAvatar(), form.getRole(), defaultPlaygroundName);
				this.db.addUser(u);
				return u;
		}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/confirm/{playground}/{email}/{code}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String getUserInformation(@PathVariable("playground") String playground, @PathVariable("email") String email, 
			@PathVariable("code") int code) 
		{
		/* function 2
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		String string = "Hello, " + username + " ";
		UserTo u = this.db.getUsers().get(email);
		if(u !=null) {
			if(u.getPlayground().equals(playground))
			{
				int VerificationCode = this.db.getUsers().get(email).getVerificationCode();
				if (VerificationCode == code)
					string.concat("Verified user");
				else
					string.concat("Wrong verification code");
			}
				else
			{
					return "User does not belong to the specified playground";
			}
		}
			else
				return "Wrong email";
		return string;
		}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTo login(@PathVariable("playground") String playground, 
			@PathVariable("email") String email) {
		/* function 3
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		
		//TODO add try catch
		UserTo u  = this.db.getUsers().get(email);
		if(u != null)
		{
			u.setStatus(ONLINE);
			this.username = u.getUsername();
			return u;
		}
		return null;
		
	}
	

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/users/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void changePlayground(@RequestBody UserTo user, @PathVariable("email") String email,@PathVariable("playground") String playground) 
		{
		/* function 4
		 * INPUT: UserTO
		 * OUTPUT: NONE
		 */
		if(user.getEmail().equals(email))
			user.setPlayground(playground);
		
		}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String function5(@RequestBody ElementTO element, @PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground) 
		{
		/* function 5
		 * INPUT: ElementTO
		 * OUTPUT: ElementTO
		 */
		//TODO change return to ElementTO
		
		return "received in POST: \n" + element + "\n email: " + email + "userPlayground: " + userPlayground + " this URL will return JSon of ElementTo type";
		}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public String function6(@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,@PathVariable("id") int id) 
		{
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */
		//TODO change return type to void
		return "received in PUT: \n" + element + "\n email: " + email + "userPlayground: " + userPlayground + "playground: "+ playground + " id: "+ id +" this URL will return nothing";
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
	public ElementTO[] returnAllElementsByEmailAndCreatorPlayground(@PathVariable("email") String email,
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
	public String distance(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,
			@PathVariable("x") int x_point,@PathVariable("y") int y_point,@PathVariable("distance") double distance ) 
		{
		/* function 9
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		Location l = new Location(x_point,y_point);
		String s = "Distance of points x=" + x_point + " y=" + y_point + " from " + distance + " is : " + Math.abs(l.length()-distance) + " units";
		return s;
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