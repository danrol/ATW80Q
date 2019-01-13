package playground.client;

import java.awt.GridLayout;

import javax.swing.JFrame;

import playground.constants.Client;

public class GetMessageWindow {

	private ClientModel model;
	private JFrame frame;
	
	
	public GetMessageWindow(ClientModel model) {
		this.setModel(model);

		frame = new JFrame();
		frame.setTitle(Client.GET_MESSAGE);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(3, 0));
		
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}


	public ClientModel getModel() {
		return model;
	}


	public void setModel(ClientModel model) {
		this.model = model;
	}
}
