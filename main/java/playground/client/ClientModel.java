package playground.client;

import javax.swing.JOptionPane;

import org.springframework.web.client.RestTemplate;

import playground.constants.Activity;
import playground.constants.Client;
import playground.constants.Playground;
import playground.constants.User;
import playground.layout.ActivityTO;
import playground.layout.UserTO;
import playground.logic.ActivityEntity;
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

	public boolean SignIn(String email) {
		if (!email.equals("")) {
			try {
				UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_3, UserTO.class,
						Client.PLAYGROUND_NAME, email);
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
		this.current_userPlayground = entity.getPlayground();

	}

	public boolean verifyUser(String code, String mail) {
		try {
			UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_2, UserTO.class, Client.PLAYGROUND_NAME, mail, code);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean updateAccount(String username, String avatar, String role) {
		UserEntity user = this.getCurrentUser();
		user.setUsername(username);
		user.setAvatar(avatar);
		user.setRole(role);
		try {
			this.restTemplate.put(this.getURL() + Playground.Function_4, new UserTO(user), Client.PLAYGROUND_NAME, this.current_email);
			this.current_user = user;
			return true;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			return false;
		}
	}

	public String getGameRules() {
	
		try {

			ActivityEntity ent = new ActivityEntity();
			ent.setType(Activity.GET_GAME_RULES_ACTIVITY);
			ActivityTO act = new ActivityTO(ent);
			String rules = this.restTemplate.postForObject(this.getURL() + Playground.Function_11, act, String.class,
					this.current_userPlayground, this.current_email);
			return rules;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(null, Client.FETCH_GAME_RULES_ERROR);
		}
		return null;
	}

}
