package users;

import database.Database;

public class User {
	
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
		this.setRole(role);
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