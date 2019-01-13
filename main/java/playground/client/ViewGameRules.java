package playground.client;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Client;

public class ViewGameRules {
	private JFrame frame;
	private JLabel gameRule;
	private String rules;
	private ClientModel model;
	public ViewGameRules(ClientModel model) {
		this.model = model;
		frame = new JFrame();
		frame.setTitle(Client.GAME_RULES);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(5, 2, 5, 5));
		
		updateGameRules();
		
		JPanel p = new JPanel();
		this.gameRule = new JLabel(rules);
		this.gameRule.setForeground(Color.BLUE);
		this.gameRule.setFont(Client.FONT_BASIC);
		p.add(this.gameRule);
		frame.add(p);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void updateGameRules() {
		String s = model.getGameRules();
		rules = s;
		
		
	}

}
