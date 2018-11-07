package elements;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import activities.Location;
import database.Database;

public class MessageBoard {
	
	private Location lctn;
	private ArrayList<String> messagesLst = new ArrayList<>(Arrays.asList("message1", "message2", "message3"));
	
	public void writeMessage(String message) {
		this.messagesLst.add(message);
	}
	
	public String viewMessagesBoardAsString() {
		if(this.messagesLst.isEmpty()) {
			return "no messages for now";
		}
		else {
			String res = String.join("\n", this.messagesLst);
			return res;
		}
	}
}
