package users;

import java.util.ArrayList;
import java.util.HashMap;

import org.assertj.core.util.Arrays;

import com.sun.javafx.collections.MappingChange.Map;

import activities.Session;
import elements.Lesson;
import elements.MessageBoard;

public class Teacher implements User{
	private int id;
	private HashMap<Integer, Lesson> lessons;// TODO decide if lessons class should be deleted. TODO add lesson constructor. TODO add hardcoded hmap of lessons 
	private HashMap<Integer, Session> sessions; ////TODO define Session Constructor. TODO add hardcoded hmap of Sessions
	private String name;
	private MessageBoard msgBoard;
	
	@Override
	public void setId(int id) {
		this.id = id;
		
	}

	public void addSession(int id, int lessonId) {
		sessions.add(new Session()); //TODO add hardcoded sessions
		
	}

	@Override
	public void writeMessage(String message) {
		this.msgBoard.writeMessage(message);
		
	}
	
	public String[] viewStudentsPerfomance(int sessionId) {
		return this.sessions.get(sessionId).getAllResults();//TODO add viewResults in Session
		
	}
	
	public void editQuestionInSession(int sessionId, int questionId, 
			String questionBody, String correctAnswer) {
		this.sessions.get(sessionId).getQuestions().
		get(questionId).editQuestion(questionBody, correctAnswer);
	}
	
	public void deleteQuestionInSession(int sessionId, int questionId) {
		this.sessions.get(sessionId).getQuestions().remove(questionId);
	}

	@Override
	public void viewMessages() {
		this.msgBoard.viewMessagesBoard();
		
	}
	
}
