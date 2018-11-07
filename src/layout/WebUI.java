package layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import elements.GameRules;
import elements.MessageBoard;
import playground.logic.Message;

@RestController
public class WebUI {
	
	@Autowired
	private GameRules gameRules;
	@Autowired
	private MessageBoard msgBoard;
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="view_rules",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String getGameRules() {
		return this.gameRules.viewRules();
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="add_message",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addMessage(@RequestBody String newMessage) {
		msgBoard.writeMessage(newMessage);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="view_all_messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return this.msgBoard.viewMessagesBoardAsString();
	}
	
	
	
}
