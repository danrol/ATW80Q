package playground.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import playground.constants.User;

public class SignUp { 
	
	ClientModel model;
	JTextField username_text = new JTextField();
	JTextField email_text = new JTextField();
	JTextField avatar_text = new JTextField();
	JTextField playground_text = new JTextField();
	JRadioButton player;
	JRadioButton manager;
	ButtonGroup group;
	JFrame frame;
	ActionListener signUpAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.err.println("Sign in request:");
			String username = username_text.getText().trim();
			String email = email_text.getText().trim();
			String playground = playground_text.getText().trim();
			String avatar = avatar_text.getText().trim();
			String role = User.PLAYER_ROLE;
			if(manager.isSelected())
				role = User.MANAGER_ROLE;
			boolean registered = model.signUp(username,email,avatar,playground,role);
			if(registered)
			{
				frame.dispose();
				new MainFrame(model);
			}
			
		}};
		
		
	public SignUp(ClientModel model) {
		this.model = model;
		frame = new JFrame();
		frame.setTitle("Sign Up");
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(7,2));
		
		JLabel SignUpLabel = new JLabel("Sign Up");
		SignUpLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.getContentPane().add(SignUpLabel);
		frame.getContentPane().add(new JLabel());
		
		frame.getContentPane().add(new JLabel("Username: "));
		frame.getContentPane().add(username_text);
		
		frame.getContentPane().add(new JLabel("Email: "));
		frame.getContentPane().add(email_text);
		
		frame.getContentPane().add(new JLabel("Avatar: "));
		frame.getContentPane().add(avatar_text);
		
		player = new JRadioButton("Player");
		manager = new JRadioButton("Manager");
		group = new ButtonGroup();
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
		
		signUpButton.addActionListener(signUpAction);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

}
