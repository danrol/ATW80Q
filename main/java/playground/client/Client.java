package playground.client;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import playground.Application;


public class Client extends Application{
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Around The World 80 questions");
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
