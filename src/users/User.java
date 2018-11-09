package users;

import database.Database;

public class User {
	
	private String email;
	private String avatar;
	private String name;
	private Database db;
	
	public User(String name, String email, String avatar) {
		super();
		this.name = name;
		this.email = email;
		this.avatar = avatar;
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}	
}