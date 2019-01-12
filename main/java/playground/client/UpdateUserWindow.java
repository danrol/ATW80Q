package playground.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import playground.constants.Client;

public class UpdateUserWindow implements ActionListener{
	ClientModel model;
	JFrame frame;
	JTextField username_text = new JTextField();
	JTextField avatar_text = new JTextField();
	JRadioButton player;
	JRadioButton manager;
	ButtonGroup group;
	JButton updateButton;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println("Sign in request:");
		frame.dispose();
		//TODO: Update User
	}
	
	public UpdateUserWindow(ClientModel model) {
		this.model = model;
		
		frame = new JFrame();
		frame.setTitle(Client.UPDATE_USER);
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(5,2, 5, 5));
		
		JLabel updatUserLabel = new JLabel(Client.UPDATE_USER);
		updatUserLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.getContentPane().add(updatUserLabel);
		frame.getContentPane().add(new JLabel(""));
		
		frame.getContentPane().add(new JLabel(Client.USERNAME_LABEL));
		frame.getContentPane().add(username_text);
		
		frame.getContentPane().add(new JLabel(Client.AVATAR_LABEL));
		frame.getContentPane().add(avatar_text);
		
		player = new JRadioButton(Client.PLAYER_RADIOBUTTON);
		manager = new JRadioButton(Client.MANAGER_RADIOBUTTON);
		group = new ButtonGroup();
		player.setSelected(true);
		group.add(player);
		group.add(manager);
		
		frame.getContentPane().add(new JLabel(Client.CHOOSE_ROLE_LABEL));
		JPanel setRole = new JPanel();
		setRole.add(player);
		setRole.add(manager);
		setRole.setBorder(new TitledBorder(Client.SET_ROLE_TITLEBORDER));
		frame.getContentPane().add(setRole);
		
		updateButton = new JButton(Client.UPDATE_USER);
		JPanel Okbutton = new JPanel();
		Okbutton.add(updateButton);
		
		frame.getContentPane().add(Okbutton);
		
		updateButton.addActionListener(this);
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	

}
