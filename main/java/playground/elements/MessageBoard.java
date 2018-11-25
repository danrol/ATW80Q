package playground.elements;

import java.util.ArrayList;
import java.util.Arrays;

import playground.layout.ElementTO;

public class MessageBoard extends ElementTO {
	public MessageBoard(String id, String playground, String creatorPlayground, String creatorEmail) {
		super(id, playground, creatorPlayground, creatorEmail);
	}

	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2", "message3"));

	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}

	public String viewMessagesBoard() {
		return messagesLst.toString();
	}
}
