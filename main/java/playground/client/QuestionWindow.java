package playground.client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import playground.constants.Client;
import playground.constants.Element;
import playground.logic.ElementEntity;

public class QuestionWindow implements ActionListener {

	private ClientModel model;
	private JFrame frame;
	private JLabel chooseQuestion;
	private JComboBox<String> questions;
	private JLabel questionLabel;
	private JTextField question;
	private JLabel answerLabel;
	private JTextField answer;
	private JButton send;
	private JTextField submitAnswer;
	private JTextField points;
	private ElementEntity[] question_list;
	private JButton left;
	private JButton right;

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = answer.getText();
		switch (e.getActionCommand()) {
		case Client.SEND: {
			boolean answer = model.answerQuestion(question_list[questions.getSelectedIndex()].getSuperkey(), s);
			if(answer)
			{

				JOptionPane.showMessageDialog(null, Client.CORRECT_ANSWER_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, Client.INCORRECT_ANSWER_MESSAGE);
			}
			
		}
		case Client.LEFT:
		{
			setLabels();
			frame.repaint();
		}
		case Client.RIGHT:
		{
			setLabels();
			frame.repaint();
		}
		case "ComboBoxChanged":
		{
			
		}
		
		}
		
	}
	public QuestionWindow(ClientModel model, GameController gameController)
	{
		this(model);
		gameController.getFrame().dispose();
	}
	public QuestionWindow(ClientModel model) {
		this.model = model;
		ElementEntity[] question_list = model.getQuestions();
		this.question_list = question_list;
		String[] question_titles = getTitles(question_list);
		

		frame = new JFrame();
		frame.setTitle(Client.ANSWER_QUESTION);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(6, 0, 5, 5));

		JPanel p1 = new JPanel();
		chooseQuestion = new JLabel(Client.CHOOSE_QUESTION);
		chooseQuestion.setFont(Client.FONT_BASIC);

		questions = new JComboBox<>(question_titles);
		p1.add(chooseQuestion);
		p1.add(questions);
	
		frame.add(p1);

		JPanel p7 = new JPanel();
		left = new JButton(Client.LEFT);
		right = new JButton(Client.RIGHT);
		p7.add(left);
		p7.add(right);
		frame.add(p7);

		JPanel p2 = new JPanel();
		questionLabel = new JLabel(Client.QUESTION);
		questionLabel.setFont(Client.FONT_BASIC);
		question = new JTextField(40);
		question.setPreferredSize(new Dimension(40, 40));
		question.setEditable(false);
		p2.add(questionLabel);
		p2.add(question);
		frame.add(p2);

		JPanel p3 = new JPanel();
		answerLabel = new JLabel(Client.ANSWER);
		answerLabel.setFont(Client.FONT_BASIC);
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

		JPanel p5 = new JPanel();
		points = new JTextField(15);
		points.setEditable(false);
		points.setBorder(new TitledBorder(Client.POINTS));
		points.setPreferredSize(new Dimension(50, 50));
		points.setFont(Client.FONT_BASIC);
		p5.add(points);
		frame.add(p5);

		send.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		questions.addActionListener(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		setLabels();

	}

	private void setLabels() {
		question.setText((String) question_list[questions.getSelectedIndex()].getAttributes().get(Element.ELEMENT_QUESTION_KEY));
		points.setText(String.valueOf(((int) question_list[questions.getSelectedIndex()].getAttributes().get(Element.ELEMENT_POINT_KEY))));
		frame.repaint();
	}
	private String[] getTitles(ElementEntity[] question_list) {
		ArrayList<String> s = new ArrayList<String>();
		for (ElementEntity e : question_list) {
			s.add(e.getName());
		}
		return s.toArray(new String[0]);
	}

}
