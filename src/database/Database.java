package database;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import elements.Lesson;
import elements.MessageBoard;
import users.Student;
import users.Teacher;

@Component
public class Database {

	
	//TODO Eden D: add functions with keys to return values
	//TODO Eden D: class should support easy change of database

	private HashMap<String, Lesson> lessons; //TODO add hard-coded hmap of lessons TODO add hardcoded hmap of Sessions
	private HashMap<String, Teacher> teachers;
	private HashMap<String, Student> students;
	private MessageBoard msgBoard = new MessageBoard();
	
	public void addTeacher(Teacher teacher) {
		this.teachers = teachers;
	}
	public void addStudent(Student student) {
		this.students = students;
	}

	private String gameRules = "Our vision is to become a part of the official school program.";
	
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
		return msgBoard;
	}

}

