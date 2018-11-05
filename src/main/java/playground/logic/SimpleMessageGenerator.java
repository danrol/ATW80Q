package main.java.playground.logic;

import org.springframework.stereotype.Component;

//@Component
public class SimpleMessageGenerator implements MessageGenerator {

	@Override
	public Message createMessage(String name) {
		return new Message("hello");
	}

}
