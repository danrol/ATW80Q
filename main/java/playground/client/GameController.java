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

	JComboBox<String> activity;
	JLabel main;
	JFrame frame;

	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println(activity.getSelectedItem());
		if (e.getActionCommand().equals(Client.UPDATE_USER))
			new UpdateUserWindow(model);
		else if (e.getActionCommand().equals(Client.SIGN_OUT)) {
			frame.dispose();
		} else if (activity.getSelectedItem().equals(Client.ADD_QUESTION)) {
			System.err.println(Client.ADD_QUESTION);
		} else if (activity.getSelectedItem().equals(Client.GAME_RULE)) {
			System.err.println(Client.GAME_RULE);
			new ViewGameRules(model);
		} else if (activity.getSelectedItem().equals(Client.GET_MESSAGE)) {
			System.err.println(Client.GET_MESSAGE);
		} else if (activity.getSelectedItem().equals(Client.ADD_MESSAGE)) {
			System.err.println(Client.ADD_MESSAGE);
		} else if (activity.getSelectedItem().equals(Client.GET_QUESTION)) {
			System.err.println(Client.GET_QUESTION);
		} else if (activity.getSelectedItem().equals(Client.ANSWER_QESTION)) {
			System.err.println(Client.ANSWER_QESTION);
		}

	}

	public GameController(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.GAME_CONTROLLER);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(7, 2));

		main = new JLabel(Client.GAME_CONTROLLER);
		main.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(main);

		signOut = new JButton(Client.SIGN_OUT);
		JPanel signoutbutton = new JPanel();
		signoutbutton.add(signOut);
		frame.add(signoutbutton, BorderLayout.EAST);

		updatuser = new JButton(Client.UPDATE_USER);
		updatuser.setFont(new Font("TimesRoman", Font.BOLD, 20));
		frame.add(updatuser);

		user = model.getCurrentUser();
		System.err.println(user);
		if (user.getRole().equals(Client.PLAYER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.PLAYER_COMBOX);
		} else if (user.getRole().equals(Client.MANAGER_RADIOBUTTON)) {
			activity = new JComboBox<String>(Client.MANAGER_COMBOX);
		}

		frame.add(activity);

		signOut.addActionListener(this);
		updatuser.addActionListener(this);
		activity.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

}