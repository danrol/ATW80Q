package playground.logic;

import java.util.ArrayList;
import java.util.Arrays;

import playground.elements.ElementTO;

public class MessageBoard extends ElementTO {
	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2", "message3"));

	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}

	public String viewMessagesBoard() {
		return messagesLst.toString();
	}
}
