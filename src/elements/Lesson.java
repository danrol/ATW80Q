package elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import activities.Session;
import users.Student;
import users.Teacher;

public class Lesson {
	private Teacher teacher;
	private String name;
	private ArrayList<Student> students;
	private HashMap<String, Session> sessions;

//constructor
	public Lesson(String name, Teacher teacher) {
		this.name = name;
		this.teacher = teacher;// teacher is a singular variable
		this.students = new ArrayList<Student>();
		this.sessions = new HashMap<String, Session>();

	}

//setter and getters 
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}

	public HashMap<String, Session> getSessions() {
		return sessions;
	}

//
	public void addStudentToLesson(Student student) {
		this.students.add(student);
	}

// create new session in the lesson
	public void createNewSession(String name) {

		Session session = new Session(students, name);
		try {
			this.setSession(name, session);
		} catch (Exception e) {
			System.out.println("could not add session to the sessions");
		}

	}

//this function is meant for the teacher to create new session
	public void setSession(String name, Session session) {
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
	private void replaceSession(String name, Session session) {
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
