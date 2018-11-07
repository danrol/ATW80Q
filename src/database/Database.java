package database;

import java.util.HashMap;

import elements.Lesson;
import users.Student;
import users.Teacher;

public class Database {

	private HashMap<String, Lesson> lessons; //TODO add hardcoded hmap of lessons TODO add hardcoded hmap of Sessions
	private HashMap<String, Teacher> teachers;
	private HashMap<String, Student> students;

	
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
