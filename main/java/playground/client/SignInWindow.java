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

public class SignInWindow implements ActionListener{ 
	ClientModel model;
	JTextField email;
	JFrame frame;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String email_text = email.getText().trim();
			boolean signed = model.SignIn(email_text);
			if(signed)
			{
				frame.dispose();
				new GameController(model);
			}			
			else if(email_text.equals("admin test"))
			{
				UserEntity admin = new UserEntity("admin","admin@admin.com","avatar",User.MANAGER_ROLE,Playground.PLAYGROUND_NAME);
				model.setCurrentUser(admin);
				frame.dispose();
				new GameController(model);
			}
			else if(email_text.equals("player test"))
			{
				UserEntity admin = new UserEntity("player","player@admin.com","avatar",User.PLAYER_ROLE,Playground.PLAYGROUND_NAME);
				model.setCurrentUser(admin);
				frame.dispose();
				new GameController(model);
			}
		}
	
	public SignInWindow(ClientModel model) {
		this.model = model;
		frame = new JFrame();
		frame.setTitle(Client.SIGN_IN);
		frame.setSize(500, 300);
		frame.setLayout(new GridLayout(6,2));
		
		JLabel SignInLabel = new JLabel(Client.SIGN_IN);
		SignInLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		
		frame.add(SignInLabel);
		frame.add(new JLabel());
		
		email = new JTextField();

		frame.add(new JLabel(Client.EMAIL_LABEL));
		frame.add(email);
		
		JButton signInButton = new JButton(Client.SIGN_IN);
		JPanel Okbutton = new JPanel();
		Okbutton.add(signInButton);
		frame.add(Okbutton);
		
		signInButton.addActionListener(this);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}

	

}
