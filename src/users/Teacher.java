package users;

import java.util.ArrayList;
import java.util.HashMap;

import org.assertj.core.util.Arrays;

import com.sun.javafx.collections.MappingChange.Map;

import activities.Session;
import database.Database;
import elements.Lesson;
import elements.MessageBoard;

public class Teacher implements User{
	private int id;
	private MessageBoard msgBoard;
	private Database db;
	
	@Override
	public void setId(int id) {
		this.id = id;
	}

	public void addSession(String lessonId, String sessionId) {
//		if(!this.db.getLessons().get(lessonId).getSessions().containsKey(sessionId))
//			this.db.getLessons().get(lessonId).getSessions().put(sessionId, new Session()); //TODO add hardcoded sessions
	}

	@Override
	public void writeMessage(String message) {
		this.msgBoard.writeMessage(message);
		
	}
	
	public String[] viewStudentsPerfomance(String lessonId, String sessionId) {
		return this.db.getLessons().get(lessonId).getSessions().get(sessionId).getAllResults();//TODO add viewResults in Session
		
	}
	
	public void editQuestionInSession(String lessonId,String sessionId, int questionId, 
			String questionBody, String correctAnswer) {
		this.db.getLessons().get(lessonId).getSessions().get(sessionId).getQuestions().
		get(questionId).editQuestion(questionBody, correctAnswer);
	}
	
	public void deleteQuestionInSession(int lessonId, int sessionId, int questionId) {
		this.db.getLessons().get(lessonId).getSessions().get(sessionId).getQuestions().remove(questionId);
	}

	@Override
	public void viewMessages() {
		this.msgBoard.viewMessagesBoard();
		
	}
	
}
