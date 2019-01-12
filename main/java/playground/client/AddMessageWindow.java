package playground.client;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import playground.constants.Client;

public class AddMessageWindow implements ActionListener {
	ClientModel model;
	JFrame frame;
	JLabel title;
	JLabel message;
	JTextField messageText;
	JButton messageButton;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
	public AddMessageWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.ADD_MESSAGE);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(3, 0));
		title = new JLabel(Client.ADD_MESSAGE);
		title.setFont(new Font("TimesRoman", Font.BOLD, 30));
		frame.add(title, BorderLayout.CENTER);
		
		
		
		JPanel p1 = new JPanel();
		message = new JLabel(Client.MESSAGE);
		message.setFont(new Font("TimesRoman", Font.BOLD, 20));
		p1.add(message);
		messageText = new JTextField(30);
		//messageText.setPreferredSize(new Dimension(30, 30));
		p1.add(messageText);
		frame.add(p1);
		
		JPanel p2 = new JPanel();
		messageButton = new JButton(Client.ADD_MESSAGE);
		messageButton.setPreferredSize(new Dimension(150, 40));
		p2.add(messageButton);
		frame.add(p2);
		
		messageButton.addActionListener(this);
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}



	
}
