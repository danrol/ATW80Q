package playground.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import playground.Playground_constants;
import playground.activities.ActivityTO;
import playground.activities.AnswerQuestionTO;
import playground.elements.ElementTO;
import playground.elements.MessageBoard;
import playground.logic.Message;
import playground.logic.UserTO;

@Component
public class Database implements Playground_constants, ATW_Database {



	private static ArrayList<UserTO> users = new ArrayList<UserTO>();
	private static ArrayList<ElementTO> elements = new ArrayList<ElementTO>();
	private static ArrayList<ActivityTO> activities = new ArrayList<ActivityTO>();
	private static ArrayList<Message> messages = new ArrayList<Message>();

	public Database() {
		UserTO user1 = new UserTO("username1", "username1@gmail.com", "avatar1", "Teacher", PLAYGROUND_NAME, "");
		UserTO user2 = new UserTO("username2", "username2@gmail.com", "avatar2", "Student", PLAYGROUND_NAME, "abc");
		UserTO user3 = new UserTO("username2", "username3@gmail.com", "avatar3", "TeAchEr", PLAYGROUND_NAME, "blabla");
		this.addUser(user1);
		this.addUser(user2);
		this.addUser(user3);
	}

	public static ArrayList<UserTO> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<UserTO> users) {
		Database.users = users;
	}

	public static ArrayList<ElementTO> getElements() {
		return elements;
	}

	public static void setElements(ArrayList<ElementTO> elements) {
		Database.elements = elements;
	}

	public static ArrayList<ActivityTO> getActivities() {
		return activities;
	}

	public static void setActivities(ArrayList<ActivityTO> activities) {
		Database.activities = activities;
	}


	
	public void addUser(UserTO user) {
		Database.users.add(user);
	}
	
	public void addElement(ElementTO element) {
		getElements().add(element);
	}

	public String getGameRules() {
		return GAME_RULES;
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



	public ElementTO getElement(String id) {
		for(ElementTO e: elements)
		{
			if(e.getId().equals(id))
				return e;
		}
		return null;
	}

	public UserTO getUser(String email) {
		for(UserTO u:users)
		{
			if(u.getEmail().equals(email))
				return u;
		}
		return null;
	}

	public static ArrayList<Message> getMessages() {
		return messages;
	}

	public static void setMessages(ArrayList<Message> messages) {
		Database.messages = messages;
	}
	
	public ElementTO[] getAllElementsTOInRadius(ElementTO element,double distance)
	{
		//element.getLocation();
		double x=0;
		double y=0;
		//bounderies
		double north=y+distance;
		double south=y-distance;
		double east=x-distance;
		double west=x+distance;
		
		ArrayList<ElementTO> array=new ArrayList<>();
		for(ElementTO el:this.elements) {
			if(el.getLocation().getX()>=east&&el.getLocation().getX()<=west&&
					el.getLocation().getY()>=south&&el.getLocation().getY()<=north) {
				array.add(el);
			}
			
		}
		return (ElementTO[]) array.toArray();
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
