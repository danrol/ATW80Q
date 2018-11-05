package main.java.playground.logic;

import org.springframework.stereotype.Component;

@Component
public class EnhancedMessageGenerator implements MessageGenerator{

	
	@Override
	public Message createMessage(String name) {
		if (name != null && !"null".equals(name)) {
			return new Message ("Hello " + name);
		}else {
			throw new RuntimeException("no available name");
		}
	}

}
