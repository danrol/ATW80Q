package playground.logic;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.catalina.User;
import activities.GameSession;
import elements.Element;

public class Lesson extends Element {
	private User teacher;
//	private String name;
//	private String id;
	private ArrayList<User> students;
	private HashMap<String, GameSession> sessions;

//constructor
	public Lesson(String name, User teacher) {
		this.name = name;
		this.teacher = teacher;// teacher is a singular variable
		this.type = LESSON;
		this.students = new ArrayList<User>();
		this.sessions = new HashMap<String, GameSession>();

	}

//setter and getters 
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<User> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<User> students) {
		this.students = students;
	}

	public HashMap<String, GameSession> getSessions() {
		return sessions;
	}

//
	public void addStudentToLesson(User student) {
		this.students.add(student);
	}

// create new session in the lesson
	public void createNewSession(String name) {

		GameSession session = new GameSession(students, name);
		try {
			this.setSession(name, session);
		} catch (Exception e) {
			System.out.println("could not add session to the sessions");
		}

	}

//this function is meant for the teacher to create new session
	public void setSession(String name, GameSession session) {
		if (sessions.containsKey(name)) {
			// throw error "there is already a session under the same name"
		} else if (sessions.containsValue(session)) {
			System.out.println("there is allredy a session under " + "this name , would you like to replace it?");

			// call a function that get an input from the user
			boolean toReplace = false;// todo
			if (toReplace == true) {
				replaceSession(name, session);
			}

		} else {
			this.sessions.put(name, session);
		}

	}

//this function will replace session 
	private void replaceSession(String name, GameSession session) {
		this.sessions.replace(name, session);
		System.out.println("session has been replaced");
	}

//this function will remove a session from the map
	public void removeSession(String name) {
		if (this.sessions.containsKey(name)) {
			sessions.remove(name);
			System.out.println(name + "has been removed from the sessions");
		}
	}

//this function will show the results for the session by its name in the map
	public void watchResultOfSession(String name) {

		this.sessions.get(name).getResultOfSession();

	}

//this function show the last session that took place 
	public void watchLastResultOfSession() {
		String[] nameSet = this.sessions.keySet().toArray(new String[this.sessions.size()]);
		watchResultOfSession(nameSet[nameSet.length]);
	}

}
