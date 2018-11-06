package elements;

import java.util.ArrayList;
import java.util.HashMap;

import activities.Session;
import users.Student;
import users.Teacher;

public class Lesson {
private Teacher tchr;
private String Name;
private ArrayList<Student> students;
private HashMap<Integer, Session> sessions;
private int Id;
public Teacher getTchr() {
	return tchr;
}
public void setTchr(Teacher tchr) {
	this.tchr = tchr;
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public ArrayList<Student> getStudents() {
	return students;
}
public void setStudents(ArrayList<Student> students) {
	this.students = students;
}
public HashMap<Integer, Session> getSessions() {
	return sessions;
}

public int getId() {
	return Id;
}
public void setId(int id) {
	Id = id;
}

}
