package main.java.playground.layout;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import playground.logic.Message;
import playground.logic.MessageGenerator;

@Component
public class UI {
	private MessageGenerator messageGenerator;
	
	private String name;

	public UI() {
	}

	@Autowired
	public UI(MessageGenerator messageGenerator) {
		super();
		this.messageGenerator = messageGenerator;
	}

	@Value("${name.of.user.to.be.greeted:Anonymous}")
	public void setName(String name) {
		this.name = name;
	}
	
//	@Autowired
	public void setMessageGenerator(MessageGenerator messageGenerator) {
		this.messageGenerator = messageGenerator;
	}
	
	@PostConstruct
	public void showMessage () {
//		String name = "Spring IoC Wolrd!";
		
		Message message = this.messageGenerator.createMessage(name);
		
		System.err.println(message);
	}
}








