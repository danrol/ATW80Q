package playground.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Client;
import playground.logic.UserEntity;

public class MainFrame implements ActionListener{
	ClientModel model;
	JButton signOut; 
	JButton updatuser; 
	UserEntity user;
	
	JComboBox<String> activity; 
	JLabel main;
	JFrame frame;

		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(Client.UPDATE_USER)) {
				new updateUser(model);
			}
			//TODO: CheckBox!! 
			frame.dispose();
		}
		
	public MainFrame(ClientModel model) {
		this.model = model;
		
		frame = new JFrame();
		frame.setTitle(Client.MAIN);
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(7,2));
		
		main = new JLabel(Client.MAIN);
		main.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(main);
		
		signOut = new JButton(Client.SIGN_OUT);
		JPanel signoutbutton = new JPanel();
		signoutbutton.add(signOut);
		frame.add(signoutbutton, BorderLayout.EAST);
		
		updatuser= new JButton(Client.UPDATE_USER);
		updatuser.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(updatuser);
		
		user= model.getCurrentUser();
		System.err.println(user);
		if(user.getRole().equals(Client.PLAYER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.PLAYER_COMBOX);
		}
		else if(user.getRole().equals(Client.MANAGER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.MANAGER_COMBOX);
		}
		
		frame.add(activity);
		
		
		signOut.addActionListener(this);
		updatuser.addActionListener(this);
		activity.addActionListener(this);

		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		
		
		
	}

	
}
