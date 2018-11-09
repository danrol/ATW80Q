package layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import playground.logic.Message;
import users.Student;
import users.Teacher;

@RestController
public class WebUI {
	
	@Autowired
	private Database db;
	
	private String defaultPlayground = "atw80q";
	
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
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/users/login{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void login() {
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/users",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void newUserForm(@PathVariable("email") String email, @PathVariable("userName") String username, 
			@PathVariable("avatar") String avatar, @PathVariable("role") String role) {
		role = role.toLowerCase();
			if (role == "teacher") {
				this.db.addTeacher(new Teacher(username, email, avatar));
			}
			else if(role == "student") {
				this.db.addStudent(new Student(username, email, avatar));
			}
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/users/confirm/{playground}/{email}/{code}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages(@PathVariable("playground") String playground, @PathVariable("email") String email, 
			@PathVariable("code") String code) {
		return "stam"; //TODO understand {code} meaning
	}
	
	
	
	
	
	
	
	
}
