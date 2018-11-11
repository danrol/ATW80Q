package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.mockito.internal.util.collections.Iterables;
import org.springframework.stereotype.Component;

import activities.GameSession;
import elements.ElementTO;
import playground.Playground_constants;
import playground.logic.Lesson;
import playground.logic.MessageBoard;
import playground.logic.Question;
import users.UserTo;

@Component
public class Database implements Playground_constants {

	// TODO add databases from Session and Lesson
	private static HashMap<String, Lesson> lessons; // TODO add hard-coded hmap of lessons TODO add hardcoded hmap of
													// Sessions
	// private static HashMap<String, User> teachers = new HashMap<String,UserTo>();
	// private static HashMap<String, User> students = new HashMap<String,UserTo>();
	private static HashMap<String, UserTo> users = new HashMap<String, UserTo>();
	private static MessageBoard messageBoard = new MessageBoard();

	// public void addTeacher(User teacher) {
	// if(TEACHER.equals(teacher.getRole()) && teachers.get(teacher.getEmail()) ==
	// null)
	// teachers.put(teacher.getEmail(), teacher);
	//
	// }
	// public void addStudent(User student) {
	// if(STUDENT.equals(student.getRole()) && students.get(student.getEmail()) ==
	// null)
	// teachers.put(student.getEmail(), student);
	// }

	// public void addUser(User user) {
	// if(user.getRole().equals(TEACHER))
	// addTeacher(user);
	// else if(user.getRole().equals(STUDENT))
	// addStudent(user);
	//
	// }
	public Database() {
		UserTo user1 = new UserTo("username1", "username1@gmail.com", "avatar1", "Teacher", PLAYGROUND_NAME, 1234);
		UserTo user2 = new UserTo("username2", "username2@gmail.com", "avatar2", "Student", PLAYGROUND_NAME, 545);
		UserTo user3 = new UserTo("username2", "username3@gmail.com", "avatar3", "TeAchEr", PLAYGROUND_NAME, 312);
		this.addUser(user1);
		this.addUser(user2);
		this.addUser(user3);
	}

	public void addUser(UserTo user) {
		this.users.put(user.getEmail(), user);
	}

	public String getGameRules() {
		return GAME_RULES;
	}

	public HashMap<String, Lesson> getLessons() {
		return lessons;
	}
	// public HashMap<String, User> getTeachers() {
	// return teachers;
	// }
	// public HashMap<String, User> getStudents() {
	// return students;
	// }

	public MessageBoard getMessageBoard() {
		return messageBoard;
	}

	public HashMap<String, UserTo> getUsers() {
		return users;
	}

	public void addSession(String lessonId, String sessionId) {
		/*
		 * if(!this.getLessons().get(lessonId).getSessions().containsKey(sessionId))
		 * this.getLessons().get(lessonId).getSessions().put(sessionId, new Session());
		 * 
		 */
		// TODO add hardcoded sessions

	}

	public String[] viewStudentsPerfomance(String lessonId, String sessionId) {
		return this.getLessons().get(lessonId).getSessions().get(sessionId).getAllResults();
		// TODO add viewResults in Session

	}

	public void editQuestionInSession(String lessonId, String sessionId, int questionId, String questionBody,
			String correctAnswer) {
		this.getLessons().get(lessonId).getSessions().get(sessionId).getQuestions().get(questionId)
				.editQuestion(questionBody, correctAnswer);
	}

	public void deleteQuestionInSession(String lesson_key, String session_key, int questionId) {
		this.getLessons().get(lesson_key).getSessions().get(session_key).getQuestions().remove(questionId);
	}

	public ElementTO[] getAllElementsByEmailAndCreatorPlayground(String userPlayground, String email) {

		ArrayList<ElementTO> elementsList = new ArrayList<>();
		for (Lesson el : Database.lessons.values()) {

			if (el.getCreatorPlayground().equals(userPlayground) && el.getCreatorEmail().equals(email))
				elementsList.add(el);

			for (GameSession ses : el.getSessions().values()) {
				for (Question question : ses.getQuestions().values())
					if (question.getCreatorPlayground().equals(userPlayground)
							&& question.getCreatorEmail().equals(email))
						elementsList.add(question);
			}

		}
		// TODO add message board check
		return (ElementTO[]) elementsList.toArray();
	}

	public ElementTO[] getElementsWithValueInAttribute(String attributeName, String value) {

		ArrayList<ElementTO> elementsList = new ArrayList<>();
		for (Lesson el : Database.lessons.values()) {

			if (el.attributeExists(attributeName, value))
				elementsList.add(el);

			for (GameSession ses : el.getSessions().values()) {
				for (Question question : ses.getQuestions().values())
					if (question.attributeExists(attributeName, value))
						elementsList.add(question);
			}

			
		}
		return elementsList.toArray(new ElementTO[elementsList.size()]);
	}
}
