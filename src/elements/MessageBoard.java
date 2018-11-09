package elements;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageBoard {
	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2"));
	
	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}
	
	public String viewMessagesBoard() {
		return messagesLst.toString();
	}
}
