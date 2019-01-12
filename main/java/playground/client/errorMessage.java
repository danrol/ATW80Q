package playground.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Client;

public class errorMessage {
	JFrame frame;
	JLabel message;
	
	public errorMessage(String message) {
		frame = new JFrame();
		frame.setTitle(Client.ERROR);
		frame.setSize(350,250);
		frame.setLayout(new GridLayout(3,0));
		frame.add(new JLabel());
		
		JPanel p = new JPanel();
		this.message = new JLabel(message);
		this.message.setForeground(Color.RED);
		this.message.setFont(new Font("TimesRoman", Font.BOLD, 40));
		p.add(this.message);
		
		frame.add(p, BorderLayout.CENTER);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
}
