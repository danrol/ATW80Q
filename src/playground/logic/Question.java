package playground.logic;

import java.util.ArrayList;

import elements.Element;


public class Question extends Element {
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

	public int questionResult(int studentResult, String studentAnswer) {
		if (studentAnswer == correctAnswer)
			studentResult = studentResult + 50;
		else
			studentResult = studentResult - 50;
		return studentResult;
	}

	@Override
	public String toString() {
		return questionBody + ": " + answers;
	}
}
