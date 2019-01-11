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

public class MainFrame implements ActionListener{
	ClientModel model;
	JButton signOut; 
	JButton updatuser; 
	
	JComboBox<String> activity; 
	JLabel main;
	JFrame frame;

		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.err.println("Sign in request:");			
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
		
		activity = new JComboBox<String>(Client.comBox);
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
