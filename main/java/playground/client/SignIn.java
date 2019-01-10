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

public class SignIn { 
	ClientModel model;
	
	
	public SignIn(ClientModel model) {
		this.model = model;
		JFrame frame = new JFrame();
		frame.setTitle("Sign In");
		frame.setSize(500, 300);
		frame.setLayout(new GridLayout(6,2));
		
		JLabel SignInLabel = new JLabel("Sign In");
		SignInLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		
		frame.add(SignInLabel);
		frame.add(new JLabel());
		JTextField userPlayground = new JTextField();
		JTextField email = new JTextField();
		frame.add(new JLabel("Playground: "));
		frame.add(userPlayground);
		frame.add(new JLabel("Email: "));
		frame.add(email);
		
		JButton signInButton = new JButton("Sign In");
		JPanel Okbutton = new JPanel();
		Okbutton.add(signInButton);
		frame.add(Okbutton);
		
		signInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Sign in request:");
				String userPlayground_text = userPlayground.getText().trim();
				String email_text = email.getText().trim();
				//model.signIn(userPlayground_text, email_text);
			}});
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

}
