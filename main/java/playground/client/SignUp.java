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

public class SignUp { 
	
	ClientModel model;
	
	public SignUp(ClientModel model) {
		this.model = model;
		JFrame frame = new JFrame();
		frame.setTitle("Sign Up");
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(7,2));
		
		JLabel SignUpLabel = new JLabel("Sign Up");
		SignUpLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.getContentPane().add(SignUpLabel);
		frame.getContentPane().add(new JLabel());
		
		frame.getContentPane().add(new JLabel("Username: "));
		frame.getContentPane().add(new JTextField());
		
		frame.getContentPane().add(new JLabel("Email: "));
		frame.getContentPane().add(new JTextField());
		
		frame.getContentPane().add(new JLabel("Avatar: "));
		frame.getContentPane().add(new JTextField());
		
		JRadioButton player = new JRadioButton("Player");
		JRadioButton manager = new JRadioButton("Manager");
		ButtonGroup group = new ButtonGroup();
		player.setSelected(true);
		group.add(player);
		group.add(manager);
		
		frame.getContentPane().add(new JLabel("Choose role: Default is Player"));
		JPanel setRole = new JPanel();
		setRole.add(player);
		setRole.add(manager);
		setRole.setBorder(new TitledBorder("Set Role"));
		frame.getContentPane().add(setRole);
		
		frame.getContentPane().add(new JLabel("Playground: "));
		frame.getContentPane().add(new JTextField());
		
		JButton signUpButton = new JButton("Sign Up");
		JPanel Okbutton = new JPanel();
		Okbutton.add(signUpButton);
		
		frame.getContentPane().add(Okbutton);
		
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Insert User In database");
				frame.dispose();
				new MainFrame();
			}});
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

}
