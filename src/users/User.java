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
		this.role = role;
		this.playground = playground;
		points = 0;
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
}