package playground.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClientFrame extends JPanel implements ActionListener{
	ClientModel model;
	public JButton signIn = new JButton("Sign In");
	public JButton signUp = new JButton("Sign Up");
	
	public final String EdenSharoni = "Eden Sharoni - 315371906";
	public final String EdenDupont = "Eden Dupont - 204808596";
	public final String EliaBenAnat = "Elia Ben Anat - 308048388";
	public final String DanielRolnik = "Daniel Rolnik - 334018009";
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Sign In"))
			new SignIn(model);
		else if(e.getActionCommand().equals("Sign Up"))
			new SignUp(model);
	}
	
	
	ClientFrame(ClientModel model) {
		this.model = model;
		JPanel init = new JPanel(new GridLayout(6, 0, 2, 2));
		
		JLabel ATWQ80 = new JLabel("Around The World 80 Questions");
		ATWQ80.setFont(new Font("TimesRoman", Font.BOLD, 35));
		init.add(ATWQ80);
		
		
		JPanel sign = new JPanel(new GridLayout(2, 0));
		sign.add(signIn, BorderLayout.CENTER);
		sign.add(signUp, BorderLayout.CENTER);
		init.add(sign, BorderLayout.CENTER);
		
		JLabel EdenS = new JLabel(EdenSharoni);
		EdenS.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel EdenD = new JLabel(EdenDupont);
		EdenD.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel Elia = new JLabel(EliaBenAnat);
		Elia.setFont(new Font("TimesRoman", Font.BOLD, 20));
		JLabel Daniel = new JLabel(DanielRolnik);
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
