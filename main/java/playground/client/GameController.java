package playground.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import playground.constants.Client;
import playground.logic.UserEntity;

public class GameController implements ActionListener {
	ClientModel model;
	JButton signOut;
	JButton updatuser;
	UserEntity user;
	JLabel userInfo;

	JComboBox<String> activity;
	JLabel main;
	JFrame frame;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Client.UPDATE_USER))
			new UpdateUserWindow(model);
		else if (e.getActionCommand().equals(Client.SIGN_OUT))
			frame.dispose();
		else {
			switch (activity.getSelectedItem() + "") {
			case Client.ADD_QUESTION:
				System.err.println(Client.ADD_QUESTION);
				new AddQuestionWindow(model);
				break;
			case Client.GAME_RULES:
				System.err.println(Client.GAME_RULES);
				new ViewGameRules();
				break;
			case Client.GET_MESSAGE:
				System.err.println(Client.GET_MESSAGE);
				new GetMessageWindow(model);
				break;
			case Client.ADD_MESSAGE:
				System.err.println(Client.ADD_MESSAGE);
				new AddMessageWindow(model);
				break;
			case Client.ANSWER_QUESTION:
				System.err.println(Client.ANSWER_QUESTION);
				new QuestionWindow(model);
				break;
			default:
				break;
			}
		}

	}

	public GameController(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.GAME_CONTROLLER);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(4, 2));

		main = new JLabel(Client.GAME_CONTROLLER);
		main.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(main);

		signOut = new JButton(Client.SIGN_OUT);
		JPanel signoutbutton = new JPanel();
		signoutbutton.add(signOut);
		frame.add(signoutbutton, BorderLayout.EAST);

		JPanel updatePanel = new JPanel();
		updatuser = new JButton(Client.UPDATE_USER);
		updatuser.setFont(new Font("TimesRoman", Font.BOLD, 20));
		updatePanel.add(updatuser);
		frame.add(updatePanel);
		frame.add(new JLabel());

		JPanel activityPanel = new JPanel();
		user = model.getCurrentUser();
		if (user.getRole().equals(Client.PLAYER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.PLAYER_COMBOX);
		} else if (user.getRole().equals(Client.MANAGER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.MANAGER_COMBOX);
		}
		activityPanel.add(activity);
		frame.add(activityPanel);
		frame.add(new JLabel());
		
		JPanel p = new JPanel(new GridLayout(6, 2));
		p.add(new JLabel(Client.USERNAME_LABEL));
		p.add(new JLabel(user.getUsername()));
		p.add(new JLabel(Client.EMAIL_LABEL));
		p.add(new JLabel(user.getEmail()));
		p.add(new JLabel(Client.AVATAR_LABEL));
		p.add(new JLabel(user.getAvatar()));
		p.add(new JLabel(Client.ROLE_LABEL));
		p.add(new JLabel(user.getRole()));
		p.add(new JLabel(Client.PLAYGROUND_LABEL));
		p.add(new JLabel(user.getPlayground()));
		p.add(new JLabel(Client.POINTS));
		p.add(new JLabel(user.getPoints() +""));
		
		frame.add(p);
		
		
		

		signOut.addActionListener(this);
		updatuser.addActionListener(this);
		activity.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}

}
