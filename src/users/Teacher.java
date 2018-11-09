package users;

import java.util.ArrayList;
import java.util.HashMap;

import org.assertj.core.util.Arrays;

import com.sun.javafx.collections.MappingChange.Map;

import activities.Session;
import elements.Lesson;
import elements.MessageBoard;

public class Teacher extends User {




	private HashMap<Integer, Lesson> lessons;// TODO add lesson constructor. TODO add hardcoded hmap of lessons 
//	private HashMap<Integer, Session> sessions; ////TODO define Session Constructor. TODO add hardcoded hmap of Sessions
	private MessageBoard msgBoard;
	
	public Teacher(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public void addSession(int lessonId, int sessionId) {
//		if(!this.lessons.get(lessonId).getSessions().containsKey(sessionId))
//		this.lessons.get(lessonId).getSessions().put(sessionId, new Session()); //TODO add hardcoded sessions
			}

	@Override
	public void writeMessage(String message) {
		this.msgBoard.writeMessage(message);
		
	}
	
	public String[] viewStudentsPerfomance(int lessonId, int sessionId) {
		return this.lessons.get(lessonId).getSessions().get(sessionId).getAllResults();//TODO add viewResults in Session
		
	}
	
	public void editQuestionInSession(int lessonId,int sessionId, int questionId, 
			String questionBody, String correctAnswer) {
		this.lessons.get(lessonId).getSessions().get(sessionId).getQuestions().
		get(questionId).editQuestion(questionBody, correctAnswer);
	}
	
	public void deleteQuestionInSession(int lessonId, int sessionId, int questionId) {
		this.lessons.get(lessonId).getSessions().get(sessionId).getQuestions().remove(questionId);
	}

	@Override
	public void viewMessages() {
		this.msgBoard.viewMessagesBoard();
		
	}
	
}
