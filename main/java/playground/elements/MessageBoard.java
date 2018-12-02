package playground.elements;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.UserService;

public class MessageBoard extends ElementEntity {

	private ElementService elementService;
	private UserService userService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

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
