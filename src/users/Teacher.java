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
	private HashMap<Integer, Lesson> lessons;// TODO add lesson constructor. TODO add hardcoded hmap of lessons 
	private HashMap<Integer, Session> sessions; // TODO add hardcoded hmap of Sessions
	private String name;
	private MessageBoard msgBoard;
	
	@Override
	public void setId(int id) {
		this.id = id;
		
	}

	public void addSession(int id, int lessonId) {
		sessionLst.add(new Session()); //TODO define Session Constructor
		
	}

	@Override
	public void writeMessage(String message) {
		this.msgBoard.writeMessage(message);
		
	}
	
	public String[] viewStudentsPerfomance(int sessionId) {
		return sessions.get(sessionId).viewResults(sessionId);//TODO add viewResults in Session
		//TODO merge Session and SessionResult
		
	}
	
	public void editQuestionInSession() {
		
	}
	
	public void deleteQuestionInSession() {
		
	}
	
	public void createNewSession() {
		
	}

	@Override
	public void viewMessages() {
		this.msgBoard.viewMessagesBoard();
		
	}
	
}
