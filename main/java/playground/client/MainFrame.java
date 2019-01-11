package playground.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame {
	ClientModel model;
	JButton signOut = new JButton("Sign Out");
	JButton updatuser = new JButton("Update User");
	String[] comBox = {"Add Question", "Get Game Rule", "Get Message", "Add Message", "Get Question", "Aswer Question" };
	JComboBox<String> activity = new JComboBox<String>(comBox);
	JLabel main = new JLabel("MAIN");
	
	public MainFrame(ClientModel model) {
		this.model = model;
		JFrame frame = new JFrame();
		frame.setTitle("Main");
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(7,2));
		
		main.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JPanel signoutbutton = new JPanel();
		signoutbutton.add(signOut);
		frame.add(signoutbutton, BorderLayout.EAST);
		
		updatuser.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(main);
		frame.add(updatuser);
		frame.add(activity);
		
		
		
		

		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		
		
		
	}
}
