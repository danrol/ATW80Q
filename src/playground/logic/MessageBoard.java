package playground.logic;

import java.util.ArrayList;
import java.util.Arrays;

import elements.Element;


public class MessageBoard extends Element {
	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2"));
	
	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}
	
	public String viewMessagesBoard() {
		return messagesLst.toString();
	}
}
