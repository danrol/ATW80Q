package playground.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Client;

public class ClientFrame extends JPanel implements ActionListener{
	ClientModel model;
	public JButton signIn; 
	public JButton signUp;
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Client.SIGN_IN))
			new SignIn(model);
		else if(e.getActionCommand().equals(Client.SIGN_UP))
			new SignUp(model);
	}
	
	
	ClientFrame(ClientModel model) {
		this.model = model;
		JPanel init = new JPanel(new GridLayout(6, 0, 2, 2));
		
		JLabel ATWQ80 = new JLabel(Client.ATW80Q);
		ATWQ80.setFont(new Font("TimesRoman", Font.BOLD, 35));
		init.add(ATWQ80);
		
		signIn = new JButton(Client.SIGN_IN);
		signUp  = new JButton(Client.SIGN_UP);
		JPanel sign = new JPanel(new GridLayout(2, 0));
		sign.add(signIn, BorderLayout.CENTER);
		sign.add(signUp, BorderLayout.CENTER);
		init.add(sign, BorderLayout.CENTER);
		
		JLabel EdenS = new JLabel(Client.EDEN_SHARONI);
		EdenS.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel EdenD = new JLabel(Client.EDEN_DUPONT);
		EdenD.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel Elia = new JLabel(Client.ELIA_BEN_ANAT);
		Elia.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel Daniel = new JLabel(Client.DANIEL_ROLNIK);
		Daniel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JPanel names = new JPanel(new GridLayout(4, 0, 2, 2));
		names.add(EdenS);
		names.add(EdenD);
		names.add(Elia);
		names.add(Daniel);
		init.add(names);
		
		add(init, BorderLayout.CENTER);
		
		signIn.addActionListener(this);
		signUp.addActionListener(this);
	
	}

	
}
