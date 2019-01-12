package playground.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import playground.constants.Client;

public class QuestionWindow {
	ClientModel model;
	JFrame frame;
	JLabel chooseQuestion;
	JComboBox<String> questions;
	JLabel questionLabel;
	JTextField question;
	JLabel answerLabel;
	JTextField answer;
	JButton send;
	JTextField submitAnswer;
	

	public QuestionWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.GET_QUESTIONS);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(4, 0, 5, 5));

		JPanel p1 = new JPanel();
		chooseQuestion = new JLabel(Client.CHOOSE_QUESTION);
		chooseQuestion.setFont(new Font("TimesRoman", Font.BOLD, 20));
		questions = new JComboBox<>(Client.MANAGER_COMBOX); //TODO: Get All Questions
		p1.add(chooseQuestion);
		p1.add(questions);
		frame.add(p1);
		
		JPanel p2 = new JPanel();
		questionLabel = new JLabel(Client.QUESTION);
		questionLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		question = new JTextField(40);
		question.setPreferredSize(new Dimension(40, 40));
		question.setEditable(false);
		p2.add(questionLabel);
		p2.add(question);
		frame.add(p2);
		
		JPanel p3 = new JPanel();
		answerLabel = new JLabel(Client.ANSWER);
		answerLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		answer = new JTextField(40);
		answer.setPreferredSize(new Dimension(40, 40));
		p3.add(answerLabel);
		p3.add(answer);
		frame.add(p3);
		
		
		
		
		
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}