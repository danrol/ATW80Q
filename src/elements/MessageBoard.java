package elements;

import java.util.ArrayList;

public class MessageBoard {
	private ArrayList<String> messagesLst;
	
	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}
	
	public String viewMessagesBoard() {
		return "messages";
	}
}
