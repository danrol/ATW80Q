package playground.database;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Component;

import playground.Constants;
import playground.activities.ActivityTO;
import playground.activities.AnswerQuestionTO;
import playground.elements.ElementTO;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.Message;
import playground.logic.UserEntity;
import playground.logic.UserTO;

@Component
public class Database implements  ATW_Database {



	private static ArrayList<UserEntity> users = new ArrayList<UserEntity>();
	private static ArrayList<ElementEntity> elements = new ArrayList<ElementEntity>();
	private static ArrayList<ActivityTO> activities = new ArrayList<ActivityTO>();
	private static ArrayList<Message> messages = new ArrayList<Message>();

	public Database() {
	}

//	public static void setElements(ArrayList<ElementTO> elements) {
//		Database.elements = elements;
//	}

	
	public static ArrayList<ActivityTO> getActivities() {
		return activities;
	}

	public static void setActivities(ArrayList<ActivityTO> activities) {
		Database.activities = activities;
	}


	

	
	
	
	public void addActivity(ActivityTO activity)
	{
		Database.activities.add(activity);
	}
	public ActivityTO getActivity(String id, String playground)
	{
		for(ActivityTO act:activities)
		{
			if(act.getPlayground().equals(playground) && act.getId().equals(id))
				return act;
		}
		return null;
	}
	public void removeActivity(String id, String playground)
	{
		for(ActivityTO act:activities)
		{
			if(act.getPlayground().equals(playground) && act.getId().equals(id))
				activities.remove(act);
		}
		
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

	
	



	public static ArrayList<Message> getMessages() {
		return messages;
	}

	public static void setMessages(ArrayList<Message> messages) {
		Database.messages = messages;
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
