package elements;

import java.util.Map;

import users.Student;

//import Users.Student;

public class GameResult {
	
	protected Map <Integer,Student> StudentResult;
	//return list of all participating students
	public String[] getAll(){
		//convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty =new String[5];
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
