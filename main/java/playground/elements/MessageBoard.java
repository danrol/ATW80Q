package playground.elements;

import java.util.ArrayList;
import java.util.Arrays;

import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.Location;

public class MessageBoard extends ElementEntity {
	public MessageBoard(String id, String playground, String email, Location location) {
		super(id, playground, email, location);
	}

	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2", "message3"));

	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}

	public String viewMessagesBoard() {
		return messagesLst.toString();
	}
}
