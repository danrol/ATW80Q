package playground.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JComboBox<String> questions = new JComboBox<String>();
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
	private int page = 0;
	private int size = 30;
	private GameController gameController;
	private JPanel p1 = new JPanel();
	private String[] question_titles = new String[0];
	private DefaultComboBoxModel<String> combo_box_titles = new DefaultComboBoxModel<String>(question_titles);

	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println(e.getActionCommand());
		String s = answer.getText();
		int index = questions.getSelectedIndex();
		switch (e.getActionCommand()) {
		case Client.SEND:

			if (index >= 0 && index < question_list.length) {
				String question_key = question_list[index].getSuperkey();
				boolean answer = model.answerQuestion(question_key, s);

				System.err.println(index + "\n" + question_key);
				if (answer) {
					submitAnswer.setText(Client.CORRECT_ANSWER_MESSAGE);
				} else {
					submitAnswer.setText(Client.INCORRECT_ANSWER_MESSAGE);
				}
				this.repaint();
				gameController.updateController();
			}

			break;
		case Client.PREVIOUS_PAGE:

			if (page != 0) {
				page--;

			}
			this.repaint();
			this.refreshPage();
			break;
		case Client.NEXT_PAGE:
			if (question_list.length == size) {

				page++;
				this.repaint();
				this.refreshPage();
			}

			break;

		}

		String question_to_paint = (String) question_list[index].getAttributes().get(Element.ELEMENT_QUESTION_KEY);
		String num_of_points = String
				.valueOf(((int) question_list[index].getAttributes().get(Element.ELEMENT_POINT_KEY)));
		question.setText(question_to_paint);
		points.setText(num_of_points);
		setLabels();
		this.repaint();

	}

	public QuestionWindow(ClientModel model, GameController gameController) {

		this.model = model;
		this.gameController = gameController;

		frame = new JFrame();
		frame.setTitle(Client.ANSWER_QUESTION);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(6, 0, 5, 5));
		questions.setModel(combo_box_titles);
		getQuestions();
		p1 = new JPanel();
		chooseQuestion = new JLabel(Client.CHOOSE_QUESTION);
		chooseQuestion.setFont(Client.FONT_BASIC);
		p1.add(chooseQuestion);
		p1.add(questions);

		frame.add(p1);

		JPanel p7 = new JPanel();
		left = new JButton(Client.PREVIOUS_PAGE);
		right = new JButton(Client.NEXT_PAGE);
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
		setLabels();
		send.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		questions.addActionListener(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}

	public void repaint() {

		p1.repaint();
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	private void refreshPage() {
		getQuestions();
		setLabels();

	}

	private void setLabels() {
		int index = questions.getSelectedIndex();
		System.out.println(index);
		if (index >= 0 && index < question_list.length) {

			String question_to_paint = (String) question_list[index].getAttributes().get(Element.ELEMENT_QUESTION_KEY);
			String num_of_points = String
					.valueOf(((int) question_list[index].getAttributes().get(Element.ELEMENT_POINT_KEY)));
			question.setText(question_to_paint);
			points.setText(num_of_points);

		}

	}

	private void getQuestions() {
		ElementEntity[] question_list = model.getQuestions(page, size);
		if (question_list.length > 0) {

			this.question_list = question_list;
			question_titles = getTitles(question_list);
			combo_box_titles.removeAllElements();

			for (int i = 0; i < question_titles.length; i++) {
				combo_box_titles.addElement(question_titles[i]);
			}
			questions.validate();
		}
	}

	private String[] getTitles(ElementEntity[] question_list) {
		ArrayList<String> s = new ArrayList<String>();
		for (ElementEntity e : question_list) {
			s.add(e.getName());
		}
		return s.toArray(new String[0]);
	}

}
