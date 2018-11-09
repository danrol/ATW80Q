
package users;

import java.util.ArrayList;

import elements.Lesson;

public class Student extends User {
	
	private ArrayList<Lesson> lessonList;
	
	
	public Student (String name, String email, String avatar)
	{
		super(name, email,avatar);
	}

	/*
	public void setClassId(int id) {
		
	}
	 */
	
	public void viewRules() {
	//TODO	
	}
	
	public void openActiveSession() {
		//TODO
	}
	
	
	
	public void addLesson(String lesson)
	{
		//DB get the lesson
		//this.lessonList.add(lesson);
		//TODO
	}


	@Override
	public void viewMessages() {
		// TODO Auto-generated method stub
		
	}



}
