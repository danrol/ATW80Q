package main.java.playground.layout;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.logic.Message;
import playground.logic.MessageGenerator;

@RestController
public class WebUI {
	private String defaultUserName;
	private MessageGenerator messageGenerator;
	
	@Autowired
	public void setMessageGenerator(MessageGenerator messageGenerator) {
		this.messageGenerator = messageGenerator;
	}
	
	@Value("${name.of.user.to.be.greeted:Anonymous}")
	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/showMessage",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Message getDefaultMessage () {
		return this.messageGenerator.createMessage(this.defaultUserName);
	}
	
//	@RequestMapping(
//			method=RequestMethod.GET,
//			path="/showMessageWithNullName",
//			produces=MediaType.APPLICATION_JSON_VALUE)
	public Message getMessageWithNullName () {
		return this.messageGenerator.createMessage(null);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/messages",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Message[] getAllMessages () {
		List<Message> messages = Arrays.asList(
				new Message("first"),
				new Message("second"),
				new Message("last")
				);
		
		return messages.toArray(new Message[0]);		
	}

	@RequestMapping(
			method=RequestMethod.GET,
			path="/messages/{name}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Message getCustomMessage (@PathVariable("name") String name) {
		return this.messageGenerator.createMessage(name);
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/messages",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Message storeMessage (@RequestBody Message newMessage) {
		return newMessage;
	}

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/messages/{name}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateMessage (
			@PathVariable("name") String name,
			@RequestBody Message newMessage) throws Exception {
		validateNull(name);
	}
	
	
	@RequestMapping(
			method=RequestMethod.DELETE,
			path="/messages/{name}")
	public void deleteMessage (@PathVariable("name") String name) throws Exception {
		validateNull(name);
	}
	
	private void validateNull(String name) throws Exception {
		if ("null".equals(name) || name == null) {
			throw new Exception("message not found");
		}
	}
}









