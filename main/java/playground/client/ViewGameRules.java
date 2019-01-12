package playground.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Activity;
import playground.constants.Client;

public class ViewGameRules {
	JFrame frame;
	JLabel gameRule;

	public ViewGameRules() {

		frame = new JFrame();
		frame.setTitle(Client.GAME_RULES);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(5, 2, 5, 5));

		JPanel p = new JPanel();
		this.gameRule = new JLabel(Activity.GAME_RULES);
		this.gameRule.setForeground(Color.BLUE);
		this.gameRule.setFont(new Font("TimesRoman", Font.BOLD, 20));
		p.add(this.gameRule);
		frame.add(p);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
