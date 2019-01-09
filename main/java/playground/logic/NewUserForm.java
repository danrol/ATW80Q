package playground.logic;

public class NewUserForm {

	private String email;
	private String username;
	private String avatar;
	private String role;

	public NewUserForm() {
		this.email = "default";
		this.username = "random";
		this.avatar = "ava";
		this.role = "no";
	}

	public NewUserForm(String email, String username, String avatar, String role) {
		super();
		if (username != null && role != null) {
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		} else
			throw new RegisterNewUserException("Registration data is not correct. Check your input");
//		this.playground = Constants.PLAYGROUND_NAME;
	}

//	private String createVerificationCode() {
//		Random r = new Random();
//		return String.valueOf(r.nextInt((9999 - 1000) + 1) + 1000);
//	}
	
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
				+  "]";
	}



}
