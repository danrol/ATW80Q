package playground.database;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import playground.Playground_constants;
import playground.activities.AnswerQuestionTO;
import playground.elements.ElementTO;
import playground.elements.MessageBoard;
import playground.logic.UserTO;

@Component
public class Database implements Playground_constants, ATW {

	// TODO add databases from Session and Lesson

	private static HashMap<String, UserTO> users = new HashMap<String, UserTO>();
	private static MessageBoard messageBoard = new MessageBoard();

	public Database() {
		UserTO user1 = new UserTO("username1", "username1@gmail.com", "avatar1", "Teacher", PLAYGROUND_NAME, "");
		UserTO user2 = new UserTO("username2", "username2@gmail.com", "avatar2", "Student", PLAYGROUND_NAME, "abc");
		UserTO user3 = new UserTO("username2", "username3@gmail.com", "avatar3", "TeAchEr", PLAYGROUND_NAME, "blabla");
		this.addUser(user1);
		this.addUser(user2);
		this.addUser(user3);
	}

	public void addUser(UserTO user) {
		Database.users.put(user.getEmail(), user);
	}

	public String getGameRules() {
		return GAME_RULES;
	}

	public MessageBoard getMessageBoard() {
		return messageBoard;
	}

	public HashMap<String, UserTO> getUsers() {
		return users;
	}

	@Override
	public AnswerQuestionTO editQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteQuestion() {
		// TODO Auto-generated method stub
		
	}

	public ElementTO[] getElementsWithValueInAttribute(String attributeName, String value) {
		// TODO Auto-generated method stub
		return null;
	}


//	public ElementTO[] getAllElementsByEmailAndCreatorPlayground(String userPlayground, String email) {
//
//		ArrayList<ElementTO> elementsList = new ArrayList<>();
//		for (Lesson el : Database.lessons.values()) {
//
//			if (el.getCreatorPlayground().equals(userPlayground) && el.getCreatorEmail().equals(email))
//				elementsList.add(el);
//
//			for (GameSession ses : el.getSessions().values()) {
//				for (Question question : ses.getQuestions().values())
//					if (question.getCreatorPlayground().equals(userPlayground)
//							&& question.getCreatorEmail().equals(email))
//						elementsList.add(question);
//			}
//
//		}
//		// TODO add message board check
//		return (ElementTO[]) elementsList.toArray();
//	}
//
//	public ElementTO[] getElementsWithValueInAttribute(String attributeName, String value) {
//
//		ArrayList<ElementTO> elementsList = new ArrayList<>();
//		for (Lesson el : Database.lessons.values()) {
//
//			if (el.attributeExists(attributeName, value))
//				elementsList.add(el);
//
//			for (GameSession ses : el.getSessions().values()) {
//				for (Question question : ses.getQuestions().values())
//					if (question.attributeExists(attributeName, value))
//						elementsList.add(question);
//			}
//
//			
//		}
//		return elementsList.toArray(new ElementTO[elementsList.size()]);
//	}
}
