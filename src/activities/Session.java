package activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;

import elements.Question;
import users.Student;
import users.Teacher;

public class Session {

	protected HashMap<Integer, Question> questions;
	protected ArrayList<Student> participatingStudents;
	protected String name;
	protected HashMap<Student, Integer> studentResult;

	public Session(ArrayList<Student> participatingStudents, String name) {
		super();
		questions = new HashMap<Integer, Question>();
		this.participatingStudents = participatingStudents;
		this.name = name;
	}

	// add question
	public void addQuestion() {
		int max = 5, min = 2;
		System.out.println("Number of the question");
		int questionNumber = 0; // input from teacher
		System.out.println("What is the question?");
		String questionBody = null; // input from teacher
		System.out.println("Type up to " + (max - 1) + "wrong answers. If less than five answers press enter");
		ArrayList<String> answers = null;
		for (int i = 0; i < 4; i++) {
			answers.add(null); // input from teacher
			if (answers.contains(""))
				break;
		}
		System.out.println("What is the correct answer?");
		String correctAnswer = null; // input from teacher
		questions.put(questionNumber, new Question(questionBody, answers, correctAnswer, max, min));
	}
	
	//TODO
	//update question
	public void updateQuestion() {
		System.out.println("Type question id that you want to update");
		for (int i = 0; i < questions.size(); i++) {
			System.out.println(questions);
		}
	}
	//TODO
	// remove question
	public void removeQuestion() {
		System.out.println("Type question id that you want to remove");
		for (int i = 0; i < questions.size(); i++) {
			System.out.println(questions.get(i));
		}
		int number = 0;// input from teacher
		questions.remove(number);
	}

	public HashMap<Integer, Question> getQuestions() {
		return this.questions;
	}

	// return list of all participating students
	public String[] getAllResults() {
		// convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty = new String[5];
		return empty;

	}

	// return all student that are at the top of the list
	public String[] getTopStudent(int num) {
		// convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty = new String[num];
		return empty;

	}

	// return all student that are at the bottom of the list
	public String[] getBottomStudent(int num) {
		// convert the map to :/name:numOfCorrectAnswers and return the string Array
		String[] empty = new String[num];
		return empty;

	}

	// show on teachers end the results of the session
	public int showSessionResult() {
		int result = 0;
		return result;
	}

	// view result of session
	public HashMap<Student, Integer> getResultOfSession() {
		return studentResult;
	}

	// new Student in ParticipatingStudents
	public void updateParticipatingStudents(ArrayList<Student> participatingStudents) {
		this.participatingStudents = participatingStudents;
	}

}
