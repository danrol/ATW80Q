package playground.client;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import playground.constants.Client;

public class QuestionWindow {
	ClientModel model;
	JFrame frame;
	JLabel chooseQuestion;
	JComboBox<String> questions;
	JLabel questionLabel;
	JTextField Question;
	JLabel AnswerLabel;
	JTextField Answer;
	

	public QuestionWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.GET_QUESTIONS);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(4, 2, 5, 5));

		JPanel p1 = new JPanel(new FlowLayout());
		chooseQuestion = new JLabel(Client.CHOOSE_QUESTION);
		questions = new JComboBox<>(Client.MANAGER_COMBOX); //TODO: Get All Questions
		p1.add(chooseQuestion);
		p1.add(questions);
		
		frame.add(p1);
		
		
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
