package users;

import database.Database;
import playground.Playground_constants;

public class User implements Playground_constants{
	
	private String email;
	private String avatar;
	private String username;
	private String role;
	private String playground;
	private long points;
	
	
	
	private Database db;
	
	public User(String username, String email, String avatar, String role, String playground) {
		super();
		this.username = username;
		this.email = email;
		this.avatar = avatar;
		if(role.toLowerCase().equals("teacher"))
			this.setRole(TEACHER);
		else if(role.toLowerCase().equals("student"))
			this.setRole(STUDENT);
		else
			this.setRole(UNDEFINED_ROLE);
		this.setPlayground(playground);
		setPoints(0);
	}


	

	public void writeMessage(String message) {
		db.getMessageBoard().writeMessage(message);
		
	}
	
	public void viewMessages() {
		db.getMessageBoard().viewMessagesBoard();
		
	}
	
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}




	public String getRole() {
		return role;
	}




	public void setRole(String role) {
		this.role = role;
	}




	public String getPlayground() {
		return playground;
	}




	public void setPlayground(String playground) {
		this.playground = playground;
	}




	public long getPoints() {
		return points;
	}




	public void setPoints(long points) {
		this.points = points;
	}	
}