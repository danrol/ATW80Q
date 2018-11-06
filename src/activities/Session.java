package activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import elements.SessionResult;
import elements.Question;
import users.Student;
import users.Teacher;

public class Session {
	
	protected Teacher teacher;
	protected HashMap<Integer, Question> questions;
	protected ArrayList<Student> participatingStudents;
	protected int id;
	
	//show on teachers end the results of the session

	public HashMap<Integer, Question> getQuestions(){
		return this.questions;
	}
	
	protected Map <Student,Integer> StudentResult;
	//return list of all participating students
	public String[]  getAllResults(){
		//convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty = new String[5];
		return empty;
		
	}
	//return all student that are at the top of the list
	public String[] getTopStudent(int num) {
		//convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty =new String[num];
		return empty;
		
	}
	//return all student that are at the bottom of the list
	public String[] getBottomStudent(int num) {
		//convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty =new String[num];
		return empty;
		
	}
	
	
	
	
}
