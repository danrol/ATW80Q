package database;

import java.util.HashMap;

import elements.Lesson;
import users.Student;
import users.Teacher;

public class Database {

	private HashMap<String, Lesson> lessons; //TODO add hard-coded HashMap of lessons TODO add hard-coded HashMap of Sessions
	private HashMap<String, Teacher> teachers;
	private HashMap<String, Student> students;

	//TODO Eden D: add functions with keys to return values
	//TODO Eden D: class should support easy change of database
	public HashMap<String, Lesson> getLessons() {
		return lessons;
	}
	public HashMap<String, Teacher> getTeachers() {
		return teachers;
	}
	public HashMap<String, Student> getStudents() {
		return students;
	}
	
	
}
