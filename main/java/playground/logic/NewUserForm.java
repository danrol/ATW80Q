package playground.logic;

import java.io.Serializable;
import java.util.Random;

import playground.Constants;

public class NewUserForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String username;
	private String avatar;
	private String role;
	private String playground;
	private String verificationCode;

	public NewUserForm() {
		this.email = "default";
		this.username = "random";
		this.avatar = "ava";
		this.role = "no";
		this.playground = Constants.PLAYGROUND_NAME;
		this.setVerificationCode(createVerificationCode());
	}

	public NewUserForm(String email, String username, String avatar, String role, String playground) {
		super();
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.playground = playground;
	}

	private String createVerificationCode() {
		Random r = new Random();
		return String.valueOf(r.nextInt((9999 - 1000) + 1) + 1000);
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

	@Override
	public String toString() {
		return "NewUserForm [email=" + email + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ ", playground=" + playground + "]";
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
