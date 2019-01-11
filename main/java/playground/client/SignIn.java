package playground.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import playground.constants.Client;
import playground.constants.Playground;
import playground.constants.User;
import playground.logic.UserEntity;

public class SignIn implements ActionListener{ 
	ClientModel model;
	JTextField userPlayground;
	JTextField email;
	JFrame frame;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.err.println("Sign in request:");
			String userPlayground_text = userPlayground.getText().trim();
			String email_text = email.getText().trim();
			boolean signed = model.SignIn(userPlayground_text, email_text);
			if(signed)
			{
				frame.dispose();
				new MainFrame(model);
			}			
			else if(userPlayground_text.equals("admin") && email_text.equals("test"))
			{
				UserEntity admin = new UserEntity("admin","admin@admin.com","avatar",User.MANAGER_ROLE,Playground.PLAYGROUND_NAME);
				model.setCurrentUser(admin);
				frame.dispose();
				new MainFrame(model);
			}
			else if(userPlayground_text.equals("player") && email_text.equals("test"))
			{
				UserEntity admin = new UserEntity("player","player@admin.com","avatar",User.PLAYER_ROLE,Playground.PLAYGROUND_NAME);
				model.setCurrentUser(admin);
				frame.dispose();
				new MainFrame(model);
			}
		}
	
	public SignIn(ClientModel model) {
		this.model = model;
		userPlayground = new JTextField();
		email = new JTextField();
		frame = new JFrame();
		frame.setTitle(Client.SIGN_IN);
		frame.setSize(500, 300);
		frame.setLayout(new GridLayout(6,2));
		
		JLabel SignInLabel = new JLabel(Client.SIGN_IN);
		SignInLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		
		frame.add(SignInLabel);
		frame.add(new JLabel());
		
		frame.add(new JLabel(Client.PLAYGROUND_LABEL));
		frame.add(userPlayground);
		frame.add(new JLabel(Client.EMAIL_LABEL));
		frame.add(email);
		
		JButton signInButton = new JButton(Client.SIGN_IN);
		JPanel Okbutton = new JPanel();
		Okbutton.add(signInButton);
		frame.add(Okbutton);
		
		signInButton.addActionListener(this);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

	

}
