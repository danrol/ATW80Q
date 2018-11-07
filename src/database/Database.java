package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import elements.Lesson;
import elements.MessageBoard;
import users.Student;
import users.Teacher;

@Component
public class Database {

	private HashMap<String, Lesson> lessons; //TODO add hardcoded hmap of lessons TODO add hardcoded hmap of Sessions
	private HashMap<String, Teacher> teachers;
	private HashMap<String, Student> students;
	private MessageBoard msgBoard = new MessageBoard();
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
		return msgBoard;
	}

	
	
}
