package playground.logic;

import org.springframework.beans.factory.annotation.Value;

import playground.Constants;
import playground.Playground_constants;
import playground.database.Database;

public class UserTO {

	private static String defaultusername;
	private static String defaultPlaygroundName;
	
	private String email = "";
	private String avatar = "";
	private String username;
	private String playground;
	private String role = Constants.UNDEFINED_ROLE;
	private String verificationCode = "";
	private String status = Constants.OFFLINE;
	private int verified_user = Constants.USER_NOT_VERIFIED;
	private long points=0;
	private Database db;
	
	
	@Value("${playground.name:no-playground}")
	public void setDefaultUserName(String PlaygroundName) {
		defaultPlaygroundName = PlaygroundName;
	}
	
	@Value("${playground.default.username:noname}")
	public static void setDefaultUsername(String username) {
		setDefaultusername(username);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserTO()
	{
		playground = defaultPlaygroundName;
		username = defaultusername;
	}

	public UserTO(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
		//verification is done separately
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
		db.writeMessage(message);

	}

	public void viewMessages() {
		db.viewMessagesBoard();

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
		if (role.equals(Constants.MODERATOR_ROLE.toLowerCase())) {
			this.role = Constants.MODERATOR_ROLE;
		} else if (role.equals(Constants.PLAYER_ROLE.toLowerCase())) {
			this.role = Constants.PLAYER_ROLE;
		} else {
			this.role = Constants.UNDEFINED_ROLE;
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
		setVerified_user(Constants.USER_VERIFIED);
	}
	
	public boolean isVerified()
	{
		if(getVerified_user() == Constants.USER_VERIFIED)
			return true;
		else return false;
	}

	public static String getDefaultusername() {
		return defaultusername;
	}

	public static void setDefaultusername(String defaultusername) {
		UserTO.defaultusername = defaultusername;
	}
}