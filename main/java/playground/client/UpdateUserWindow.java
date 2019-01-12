package playground.client;

import java.awt.Font;
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

public class UpdateUserWindow implements ActionListener {
	private ClientModel model;
	private JFrame frame;
	private JTextField username_text;
	private JTextField avatar_text;
	private JRadioButton player;
	private JRadioButton manager;
	private ButtonGroup group;
	private JButton updateButton;
	private GameController gameController;

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean changed = model.updateAccount(username_text.getText(), avatar_text.getText(),
				(player.isSelected() ? User.PLAYER_ROLE : User.MANAGER_ROLE));
		if (changed) {
			gameController.getFrame().dispose();
			new GameController(model);
			frame.dispose();
		} else {
			JOptionPane.showMessageDialog(null, Client.CANNOT_UPDATE_USER);
		}

	}

	public UpdateUserWindow(ClientModel model, GameController gameController) {
		this.model = model;
		this.gameController = gameController;
		frame = new JFrame();
		frame.setTitle(Client.UPDATE_USER);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(5, 2, 5, 5));

		JLabel updatUserLabel = new JLabel(Client.UPDATE_USER);
		updatUserLabel.setFont(Client.FONT_TITLE);
		frame.getContentPane().add(updatUserLabel);
		frame.getContentPane().add(new JLabel());
		username_text = new JTextField(model.getCurrentUser().getUsername());
		avatar_text = new JTextField(model.getCurrentUser().getAvatar());
		frame.getContentPane().add(new JLabel(Client.USERNAME_LABEL));
		frame.getContentPane().add(username_text);

		frame.getContentPane().add(new JLabel(Client.AVATAR_LABEL));
		frame.getContentPane().add(avatar_text);

		player = new JRadioButton(Client.PLAYER_ROLE);
		manager = new JRadioButton(Client.MANAGER_ROLE);
		if (model.getCurrentUser().getRole().equals(User.PLAYER_ROLE)) {
			player.setSelected(true);
		} else {

			manager.setSelected(true);
		}
		group = new ButtonGroup();

		group.add(player);
		group.add(manager);

		frame.getContentPane().add(new JLabel(Client.CHOOSE_ROLE_LABEL));
		JPanel setRole = new JPanel();
		setRole.add(player);
		setRole.add(manager);
		setRole.setBorder(new TitledBorder(Client.SET_ROLE_TITLEBORDER));
		frame.getContentPane().add(setRole);

		updateButton = new JButton(Client.UPDATE_USER);
		JPanel Okbutton = new JPanel();
		Okbutton.add(updateButton);

		frame.getContentPane().add(Okbutton);

		updateButton.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
