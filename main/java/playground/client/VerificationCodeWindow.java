package playground.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import playground.constants.Client;

public class VerificationCodeWindow implements ActionListener {
	ClientModel model;
	JFrame frame;
	JLabel verificationCode;
	JTextField verificationText;
	JButton verificationButton;
	JTextField userPlayground;
	JTextField email;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: Verify Code
		new GameController(model);
		frame.dispose();
	}

	public VerificationCodeWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.VERIFY_CODE_TITLE);
		frame.setSize(500, 400);
		// frame.setLayout(new GridLayout(5,2));

		JPanel p = new JPanel();
		JPanel verificationCodePanel = new JPanel();
		verificationCode = new JLabel(Client.VERIFY_CODE);
		verificationCode.setFont(new Font("TimesRoman", Font.BOLD, 30));
		verificationCodePanel.add(verificationCode);
		p.add(verificationCodePanel);
		p.add(new JLabel());

		frame.add(p, BorderLayout.NORTH);

		JPanel p1 = new JPanel(new GridLayout(2, 2));

		userPlayground = new JTextField();
		email = new JTextField();
		p1.add(new JLabel(Client.PLAYGROUND_LABEL));
		p1.add(userPlayground);
		p1.add(new JLabel(Client.EMAIL_LABEL));
		p1.add(email);

		frame.add(p1, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new GridLayout(0, 2));

		
		JPanel verificationTextPanel = new JPanel();
		verificationText = new JTextField(20);
		verificationText.setPreferredSize(new Dimension(50, 40));
		verificationText.setBorder(new TitledBorder(Client.VERIFY_CODE_TITLE));
		verificationText.setToolTipText(Client.VERIFY_CODE_TITLE);
		verificationTextPanel.add(verificationText);
		p2.add(verificationTextPanel);

		JPanel verificationButtonPanel = new JPanel();
		verificationButton = new JButton(Client.VERIFY_CODE);
		verificationButton.setPreferredSize(new Dimension(150, 50));
		verificationButtonPanel.add(verificationButton);
		p2.add(verificationButtonPanel);

		frame.add(p2, BorderLayout.SOUTH);
//		verificationButton.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

}
