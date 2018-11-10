package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.mockito.internal.util.collections.Iterables;
import org.springframework.stereotype.Component;

import activities.GameSession;
import application.Playground_constants;
import elements.Element;
import elements.Lesson;
import elements.MessageBoard;
import elements.Question;
import users.User;

@Component
public class Database implements Playground_constants{

	//TODO add databases from Session and Lesson
	private static HashMap<String, Lesson> lessons; //TODO add hard-coded hmap of lessons TODO add hardcoded hmap of Sessions
	//	private static HashMap<String, User> teachers;
	//	private static HashMap<String, User> students;
	private static HashMap<String, User> users;
	private static MessageBoard messageBoard = new MessageBoard();

	//	public void addTeacher(User teacher) {
	//		if(TEACHER.equals(teacher.getRole()) && teachers.get(teacher.getEmail()) == null)
	//			teachers.put(teacher.getEmail(), teacher);
	//		
	//	}
	//	public void addStudent(User student) {
	//		if(STUDENT.equals(student.getRole()) && students.get(student.getEmail()) == null)
	//			teachers.put(student.getEmail(), student);
	//	}

	//	public void addUser(User user) {
	//		if(user.getRole().equals(TEACHER))
	//			addTeacher(user);
	//		else if(user.getRole().equals(STUDENT))
	//			addStudent(user);
	//		
	//	}

	public void addUser(User user) {
		this.users.put(user.getEmail(), user);
	}

	public String getGameRules() {
		return GAME_RULES;
	}
	public HashMap<String, Lesson> getLessons() {
		return lessons;
	}
	//	public HashMap<String, User> getTeachers() {
	//		return teachers;
	//	}
	//	public HashMap<String, User> getStudents() {
	//		return students;
	//	}

	public MessageBoard getMessageBoard() {
		return messageBoard;
	}



	public HashMap<String, User> getUsers() {
		return users;
	}

	public void addSession(String lessonId, String sessionId)
	{
		/*
		if(!this.getLessons().get(lessonId).getSessions().containsKey(sessionId))
			this.getLessons().get(lessonId).getSessions().put(sessionId, new Session()); 

		 */
		//TODO add hardcoded sessions

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

	public void deleteQuestionInSession(String lesson_key, String session_key, int questionId) {
		this.getLessons().get(lesson_key).getSessions().get(session_key).getQuestions().remove(questionId);
	}


	public Element[] getAllElementsByEmailAndCreatorPlayground(String userPlayground, String email) {

		ArrayList<Element> elementsList = new ArrayList<>();
		for (Lesson el : this.lessons.values()) {
			if (el.getCreatorPlayground() == userPlayground && el.getCreatorEmail() == email)
				elementsList.add(el);
			for (GameSession ses : el.getSessions().values()) {
				for (Question question : ses.getQuestions().values()) {
					if(question.getCreatorPlayground() == userPlayground && question.getCreatorEmail() == email){
						elementsList.add(question);
					}
				}
			}
		}
		// TODO add message board check
		return (Element[]) elementsList.toArray();
	}	
}


