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
			model.SignIn(userPlayground_text, email_text);
			frame.dispose();
			new MainFrame(model);			
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
