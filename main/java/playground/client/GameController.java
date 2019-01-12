package playground.client;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private ClientModel model;
	private JButton signOut;
	private JButton updatuser;
	private UserEntity user;
	private JLabel userInfo;
	private JComboBox<String> activity;
	private JLabel main;
	private JFrame frame;
	
	

	private JLabel username_label;
	private JLabel mail_label;
	private JLabel avatar_label;
	private JLabel role_label;
	private JLabel playground_label ;
	private JLabel point_label;
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Client.UPDATE_USER))
		{
			new UpdateUserWindow(model, this);
		}
			
		else if (e.getActionCommand().equals(Client.SIGN_OUT))
			frame.dispose();
		else {
			switch (activity.getSelectedItem() + "") {
			case Client.ADD_QUESTION:
				new AddQuestionWindow(model);
				break;
			case Client.GAME_RULES:
				new ViewGameRules(model);
				break;
			case Client.GET_MESSAGE:
				new GetMessageWindow(model);
				break;
			case Client.ADD_MESSAGE:
				new AddMessageWindow(model);
				break;
			case Client.ANSWER_QUESTION:
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
		user=model.getCurrentUser();
		
		
		frame.setTitle(Client.GAME_CONTROLLER);
		frame.setSize(500, 500);
		frame.setLayout(new GridLayout(5, 0));

		JPanel p1 = new JPanel(new GridLayout(1, 2));
		main = new JLabel(Client.GAME_CONTROLLER);
		main.setFont(Client.FONT_TITLE);
		p1.add(main);

		signOut = new JButton(Client.SIGN_OUT);
		JPanel signoutbutton = new JPanel();
		signoutbutton.add(signOut);
		p1.add(signoutbutton, BorderLayout.EAST);
		frame.add(p1);

		JPanel updatePanel = new JPanel();
		updatuser = new JButton(Client.UPDATE_USER);
		updatuser.setFont(Client.FONT_BASIC);
		updatePanel.add(updatuser);
		frame.add(updatePanel);

		JPanel activityPanel = new JPanel();
		updateComboBox();
		
	
		activityPanel.add(activity);
		frame.add(activityPanel);
		
		
		
		updateUserInfo();

		JPanel p = new JPanel(new GridLayout(6, 2));
		p.add(new JLabel(Client.USERNAME_LABEL)).setFont(Client.FONT_BASIC);
		p.add(username_label).setFont(Client.FONT_BASIC);
		p.add(new JLabel(Client.EMAIL_LABEL)).setFont(Client.FONT_BASIC);
		p.add(mail_label).setFont(new Font("TimesRoman", Font.BOLD, 20));
		p.add(new JLabel(Client.AVATAR_LABEL)).setFont(Client.FONT_BASIC);
		p.add(avatar_label).setFont(new Font("TimesRoman", Font.BOLD, 20));
		p.add(new JLabel(Client.ROLE_LABEL)).setFont(Client.FONT_BASIC);
		p.add(role_label).setFont(Client.FONT_BASIC);
		p.add(new JLabel(Client.PLAYGROUND_LABEL)).setFont(Client.FONT_BASIC);
		p.add(playground_label).setFont(Client.FONT_BASIC);
		p.add(new JLabel(Client.POINTS)).setFont(Client.FONT_BASIC);
		p.add(point_label).setFont(Client.FONT_BASIC);
		frame.add(p);
		
		
		frame.add(new JLabel());
		
		
		

		signOut.addActionListener(this);
		updatuser.addActionListener(this);
		activity.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}

	private void updateUserInfo() {

		username_label = new JLabel(user.getUsername());
		mail_label = new JLabel(user.getEmail());
		avatar_label = new JLabel(user.getAvatar());
		role_label = new JLabel(user.getRole());
		playground_label = new JLabel(user.getPlayground());
		point_label = new JLabel(user.getPoints() + "");
		
		
	}

	public void updateController() {
		user = model.getCurrentUser();
		updateComboBox();
		updateUserInfo();
		
	}

	private void updateComboBox() {
		if (user.getRole().equals(Client.PLAYER_ROLE)) {
			activity = new JComboBox<String>(Client.PLAYER_COMBOX);
		} else if (user.getRole().equals(Client.MANAGER_ROLE)) {
			activity = new JComboBox<String>(Client.MANAGER_COMBOX);
		}
		
	}
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
