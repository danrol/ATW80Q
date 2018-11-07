package users;

import java.util.ArrayList;

import elements.Lesson;

public class Student implements User{
	
	private int id;
	private String name;
	private ArrayList<Lesson> lessonLst;
	private DB;
	
	//constructor
	public Student(int id,String name )
	{
		this.id=id;
		this.name=name;
		this.lessonLst=new ArrayList<Lesson>();
	}

	@Override
	public void setId(int id) {

	}
	
	public void setClassId(int id) {
		
	}

	
	@Override
	public void writeMessage(String message) {
		
	}
	
	public void viewRules() {
		
	}
	
	public void openActiveSession() {
		
	}
	
	
	
	public void addLesson(String lesson)
	{
		//DB get the lesson
		//this.lessonLst.add(lesson);
	}


	@Override
	public void viewMessages() {
		// TODO Auto-generated method stub
		
	}



}
