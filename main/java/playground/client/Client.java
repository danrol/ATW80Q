package playground.client;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import playground.Application;

public class Client extends Application {
	public static void main(String[] args) {

		String host = System.getProperty("playground.host");
		if (host == null) {
			host = "localhost";
		}
		int port;
		try {
			port = Integer.parseInt(System.getProperty("server.port"));
		} catch (Exception e) {
			port = 8083;
		}

		ClientModel model = new ClientModel(host, port);
		JFrame frame = new JFrame();
		frame.setTitle(playground.constants.Client.ATW80Q);
		frame.setSize(550, 550);
		ClientLogin s = new ClientLogin(model);
		frame.add(s, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
}
