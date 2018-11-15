package playground.logic;

import playground.Playground_constants;
import playground.database.Database;

public class UserTO implements Playground_constants {

	private String email;
	private String avatar;
	private String username;
	private String role;
	private String playground;
	private String verificationCode = "";
	private String status = OFFLINE;
	private int verified_user = USER_NOT_VERIFIED;
	private long points;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Database db;

	public UserTO(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
	}

	public UserTO(String username, String email, String avatar, String role, String playground, String code) {
		this(username, email, avatar, role, playground);
		setVerificationCode(code);
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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
		role = role.toLowerCase();
		if (role.equals(TEACHER.toLowerCase())) {
			this.role = TEACHER;
		} else if (role.equals(STUDENT.toLowerCase())) {
			this.role = STUDENT;
		} else {
			this.role = UNDEFINED_ROLE;
			throw new RuntimeException("Undefined role");
		}
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

	public int getVerified_user() {
		return verified_user;
	}

	public void setVerified_user(int verified_user) {
		this.verified_user = verified_user;
	}
	
	public void verifyUser()
	{
		setVerified_user(USER_VERIFIED);
	}
}