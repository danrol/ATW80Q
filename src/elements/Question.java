package elements;

import java.util.ArrayList;

public class Question {
	private int id;
	private String questionBody;
	//private ArrayList<String> answers;
	private String correctAnswer;
	public Question(int id, String questionBody, String correctAnswer) {
		super();
		this.id = id;
		this.questionBody = questionBody;
		this.correctAnswer = correctAnswer;
	}
	
	private void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}
	
	private void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	private void editQuestion(String questionBody, String correctAnswer) {
		this.questionBody = questionBody;
		this.correctAnswer = correctAnswer;
	}
}
