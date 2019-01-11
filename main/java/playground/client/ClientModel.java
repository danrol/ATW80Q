package playground.client;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.web.client.RestTemplate;

import playground.constants.Playground;
import playground.layout.UserTO;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;

public class ClientModel {

    

   
	private String current_userPlayground;
	private String current_email;
	private UserEntity current_user;
	private RestTemplate restTemplate;
	private String host;
	private int port;

	public ClientModel(String host, int port) {
		this.restTemplate = new RestTemplate();
		this.host = host;
		this.port = port;
	}

	public boolean SignIn(String userPlayground, String email) {
		if (!(userPlayground.equals("") || email.equals(""))) {
			try {
				UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_3, UserTO.class,
						userPlayground, email);
				setCurrentUser(user.toEntity());
				 
			} catch (Exception e) {
				
				return false;
			}
			return true;
		}
		return false;
	}

	

	public boolean signUp(String username, String email, String avatar, String playground, String role) {
		try {
			NewUserForm form = new NewUserForm(email, username, avatar, role);
			UserTO user = this.restTemplate.postForObject(this.getURL() + Playground.Function_1, form, UserTO.class);
			setCurrentUser(user.toEntity());
			System.err.println("Signed up " + user.toEntity() + " Awaiting verification..");
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public String getURL() {
		return "http://" + host + ":" + port;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserPlayground() {
		return current_userPlayground;
	}

	public void setUserPlayground(String current_userPlayground) {
		this.current_userPlayground = current_userPlayground;
	}

	public String getEmail() {
		return current_email;
	}

	public void setEmail(String current_email) {
		this.current_email = current_email;
	}

	public UserEntity getCurrentUser() {
		return current_user;
	}
	
	public void setCurrentUser(UserEntity entity) {
		this.current_user = entity;
		this.current_email = entity.getEmail();
		this.current_userPlayground = entity.getUsername();

	}

}
