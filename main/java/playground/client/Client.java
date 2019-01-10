package playground.client;

import java.awt.BorderLayout;
import javax.swing.JFrame;


public class Client {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Around The World 80 days");
		frame.setSize(550, 550);
		ClientFrame s = new ClientFrame();
		frame.add(s, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
}
