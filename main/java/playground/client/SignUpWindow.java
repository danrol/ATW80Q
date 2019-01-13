package playground.client;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import playground.constants.Client;
import playground.constants.User;

public class SignUpWindow implements ActionListener{ 
	
	private ClientModel model;
	private JTextField username_text = new JTextField();
	private JTextField email_text = new JTextField();
	private JTextField avatar_text = new JTextField();
	private JTextField playground_text = new JTextField();
	private JRadioButton player;
	private JRadioButton manager;
	private ButtonGroup group;
	private JFrame frame;
		
		@Override
		public void actionPerformed(ActionEvent e) {
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
				new VerificationCodeWindow(model);
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, Client.CANNOT_VERIFY_ERROR_MESSAGE_SIGN_UP_ERROR_MESSAGE);
			}
			
		}
		
		
	public SignUpWindow(ClientModel model) {
		this.model = model;
		frame = new JFrame();
		frame.setTitle(Client.SIGN_UP);
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(7,2));
		
		JLabel SignUpLabel = new JLabel(Client.SIGN_UP);
		SignUpLabel.setFont(Client.FONT_TITLE);
		frame.getContentPane().add(SignUpLabel);
		frame.getContentPane().add(new JLabel());
		
		frame.getContentPane().add(new JLabel(Client.USERNAME_LABEL));
		frame.getContentPane().add(username_text);
		
		frame.getContentPane().add(new JLabel(Client.EMAIL_LABEL));
		frame.getContentPane().add(email_text);
		
		frame.getContentPane().add(new JLabel(Client.AVATAR_LABEL));
		frame.getContentPane().add(avatar_text);
		
		player = new JRadioButton(Client.PLAYER_ROLE);
		manager = new JRadioButton(Client.MANAGER_ROLE);
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

		
		JButton signUpButton = new JButton(Client.SIGN_UP);
		JPanel Okbutton = new JPanel();
		Okbutton.add(signUpButton);
		
		frame.getContentPane().add(Okbutton);
		
		signUpButton.addActionListener(this);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}


	

}
