package playground.client;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;

import playground.Application;

public class MainClient extends Application {
	public static void main(String[] args) {
		
		String host=null;
		System.out.println(Arrays.toString(args));

		int port;
		try {
			port = Integer.parseInt(System.getProperty("server.port"));
		} catch (Exception e) {
			port = 8083;
		}
		if(args.length == 1)
			host = args[0];
		if (host == null) {
			host = "localhost" + ":"+ port;
		}
		ClientModel model = new ClientModel(host, port);
		ClientLogin s = new ClientLogin(model);

		JFrame frame = new JFrame();
		frame.setTitle(playground.constants.Client.ATW80Q);
		frame.setSize(550, 550);
		frame.add(s, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
