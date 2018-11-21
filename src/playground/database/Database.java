package playground.database;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import playground.Constants;
import playground.activities.ActivityTO;
import playground.activities.AnswerQuestionTO;
import playground.elements.ElementTO;
import playground.logic.Message;
import playground.logic.UserTO;

@Component
public class Database implements  ATW_Database {



	private static ArrayList<UserTO> users = new ArrayList<UserTO>();
	private static ArrayList<ElementTO> elements = new ArrayList<ElementTO>();
	private static ArrayList<ActivityTO> activities = new ArrayList<ActivityTO>();
	private static ArrayList<Message> messages = new ArrayList<Message>();

	public Database() {
		UserTO user1 = new UserTO("username1", "username1@gmail.com", "avatar1", "moderator", Constants.PLAYGROUND_NAME, "");
		UserTO user2 = new UserTO("username2", "username2@gmail.com", "avatar2", "player", Constants.PLAYGROUND_NAME, "abc");
		user2.setPlayground(Constants.PLAYGROUND_NAME);
		UserTO user3 = new UserTO("username2", "username3@gmail.com", "avatar3", "player", Constants.PLAYGROUND_NAME, "blabla");
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
		return Constants.GAME_RULES;
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

	public ArrayList<ElementTO> getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String Email) {
		ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementTO element : elements) {
			if(checkIfElementsMadeBySpecificUserInSpecificPlayground(element, element.getCreatorPlayground(), element.getCreatorPlayground()))
				result.add(element);
		}
		return result;
	}
	
	public Boolean checkIfElementsMadeBySpecificUserInSpecificPlayground(ElementTO element, 
			String creatorPlayground, String creatorEmail) {
		if(element.getCreatorPlayground() == creatorPlayground && element.getCreatorEmail() == creatorEmail)
			return true;
		else
			return false;
	}
	public ElementTO[] getElementsWithValueInAttribute(String creatorPlayground, 
			String creatorEmail, String attributeName, String value) {
		// TODO Auto-generated method stub
		ArrayList<ElementTO> tempElementsList = new ArrayList<>();
		for (ElementTO element: elements)
		{
			if (checkIfElementsMadeBySpecificUserInSpecificPlayground(element, element.getCreatorPlayground(), 
					element.getCreatorPlayground()) && element.attributeExists(attributeName, value))
				tempElementsList.add(element);
		}
		return tempElementsList.toArray(
				new ElementTO[tempElementsList.size()]);
	}



	public void updateElementInDatabaseFromExternalElement(ElementTO element, String id, String playground) {
		elements.remove(getElement(id, playground));
		elements.add(element);
	}
	
	
	public ElementTO getElement(String id, String playground) {
		for(ElementTO e: elements)
		{
			if(e.getId().equals(id) && e.getPlayground().equals(playground))
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
		//boundaries
		double north=y+distance;
		double south=y-distance;
		double east=x-distance;
		double west=x+distance;
		//find in a circle
		ArrayList<ElementTO> array=new ArrayList<>();
		for(ElementTO el:this.elements) {
			double xin=el.getLocation().getX()-element.getLocation().getX();
			double yin=el.getLocation().getY()-element.getLocation().getY();
			
			if(Math.sqrt(xin*xin+yin*yin)<=distance) {
				array.add(el);
			}
		}
		//find in double x and double y 
//		for(ElementTO el:this.elements) {
//			if(el.getLocation().getX()>=east&&el.getLocation().getX()<=west&&
//					el.getLocation().getY()>=south&&el.getLocation().getY()<=north) {
//				array.add(el);
//			}
//			
//		}
		return (ElementTO[]) array.toArray();
	}
	
	public void cleanDatabase()
	{
		elements.clear();
		users.clear();
		activities.clear();
		messages.clear();
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
