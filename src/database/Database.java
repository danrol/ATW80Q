package database;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import activities.Session;
import elements.Lesson;
import elements.MessageBoard;
import users.Student;
import users.Teacher;

@Component
public class Database {

	
	//TODO Eden D: add functions with keys to return values
	//TODO Eden D: class should support easy change of database

	private static HashMap<String, Lesson> lessons; //TODO add hard-coded hmap of lessons TODO add hardcoded hmap of Sessions
	private static HashMap<String, Teacher> teachers;
	private static HashMap<String, Student> students;
	private static MessageBoard messageBoard = new MessageBoard();
	
	public void addTeacher(Teacher teacher) {
		this.teachers = teachers;
	}
	public void addStudent(Student student) {
		this.students = students;
	}

	private String gameRules = "Our vision is to become a part of the official school program.\r\n" + 
			"We aim to improve the memory of students on facts about the world in all topics, and to become the best tool in the market which could help teachers create more interactive lessons for their students. \r\n" + 
			"For students it will be an alternative way to improving their knowledge in the school material and possibly quiz themselves before tests. \r\n" + 
			"";

	
	public String getGameRules() {
		return gameRules;
	}
	public HashMap<String, Lesson> getLessons() {
		return lessons;
	}
	public HashMap<String, Teacher> getTeachers() {
		return teachers;
	}
	public HashMap<String, Student> getStudents() {
		return students;
	}
	
	public MessageBoard getMessageBoard() {
		return messageBoard;
	}


	
	public void addSession(String lessonId, String sessionId)
	{
		/*
		if(!this.getLessons().get(lessonId).getSessions().containsKey(sessionId))
			this.getLessons().get(lessonId).getSessions().put(sessionId, new Session()); 
	
		*/
		//TODO add hardcoded sessions
		//TODO
	}
	
	public String[] viewStudentsPerfomance(String lessonId, String sessionId) {
		return this.getLessons().get(lessonId).getSessions().get(sessionId).getAllResults();
		//TODO add viewResults in Session
		
	}
	
	public void editQuestionInSession(String lessonId,String sessionId, int questionId, 
			String questionBody, String correctAnswer) {
		this.getLessons().get(lessonId).getSessions().get(sessionId).getQuestions().
		get(questionId).editQuestion(questionBody, correctAnswer);
	}
	
	public void deleteQuestionInSession(int lessonId, int sessionId, int questionId) {
		this.getLessons().get(lessonId).getSessions().get(sessionId).getQuestions().remove(questionId);
	}
}
 
 
 