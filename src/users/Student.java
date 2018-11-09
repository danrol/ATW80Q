
package users;

import java.util.ArrayList;

import elements.Lesson;

public class Student extends User {
	
	private String name;
	private ArrayList<Lesson> lessonList;
	//private DB;
	//TODO DB?
	
	//constructor
	public Student (int id,String name)
	{
		super(id);
		this.name=name;
		this.lessonList=new ArrayList<Lesson>();
	}


	
	public void setClassId(int id) {
		
	}

	

	
	public void viewRules() {
		
	}
	
	public void openActiveSession() {
		
	}
	
	
	
	public void addLesson(String lesson)
	{
		//DB get the lesson
		//this.lessonList.add(lesson);
	}


	@Override
	public void viewMessages() {
		// TODO Auto-generated method stub
		
	}



}
