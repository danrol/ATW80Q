package playground.logic;

import java.io.Serializable;

public class NewUserForm implements Serializable {
	
	private String email;
	private String username;
	private String avatar;
	private String role;
	private String playground;
	
	public NewUserForm(String email, String username, String avatar, String role, String playground) {
		super();
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.playground = playground;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	
	@Override
	public String toString() {
		return "NewUserForm [email=" + email + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ ", playground=" + playground + "]";
	}
	
	
}
