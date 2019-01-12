package playground.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import playground.constants.Client;

public class QuestionWindow implements ActionListener{

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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println("HELO");
	}
	
	public QuestionWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.GET_QUESTIONS);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(4, 0, 5, 5));

		JPanel p1 = new JPanel();
		chooseQuestion = new JLabel(Client.CHOOSE_QUESTION);
		chooseQuestion.setFont(new Font("TimesRoman", Font.BOLD, 20));
		//TODO: get all questions;
		questions = new JComboBox<>(Client.MANAGER_COMBOX);// Client.MANAGER_COMBOX temporary
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
		
		JPanel p4 = new JPanel();
		send = new JButton(Client.SEND);
		submitAnswer = new JTextField(15);
		submitAnswer.setPreferredSize(new Dimension(35, 35));
		submitAnswer.setEditable(false);
		p4.add(send);
		p4.add(submitAnswer);
		frame.add(p4);
		
		questions.addActionListener(this);
		answer.addActionListener(this);
		send.addActionListener(this);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}


	
}
