package elements;

import java.util.ArrayList;
import java.util.HashMap;

import activities.Location;
import users.Student;

public class Question {
<<<<<<< HEAD

	private Location lctn;
=======
	
	private Location location;
>>>>>>> 6781a750db81e61a953a85be84213201fb1330f4
	private String questionBody;
	private ArrayList<String> answers;
	private String correctAnswer;

	public Question(String questionBody, ArrayList<String> answers, String correctAnswer, int max, int min) {
		super();
		this.questionBody = questionBody;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		int random = (int) (Math.random() % (max - min) + min);
		this.answers.add(random, correctAnswer);
	}

	public void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public void editQuestion(String questionBody, String correctAnswer) {
		this.questionBody = questionBody;
		this.correctAnswer = correctAnswer;
	}

	// return array of answers
	// if student answer right or wrong

	/**
	 * public int questionResult(HashMap<Student, Integer> studentResult, String
	 * studentAnswer) { if (studentAnswer == correctAnswer) studentResult =
	 * studentResult + 50; else studentResult = studentResult - 50; return
	 * studentResult; }
	 **/

	@Override
	public String toString() {
		return questionBody + ": " + answers;
	}

}
