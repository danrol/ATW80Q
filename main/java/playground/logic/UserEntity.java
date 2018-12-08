package playground.logic;

import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import playground.Constants;
import playground.exceptions.RegisterNewUserException;

@Entity
@Table(name = "USER")
public class UserEntity {

	private String email;
	private String avatar;
	private String username;
	private String playground;
	private String role = Constants.UNDEFINED_ROLE;
	private String verificationCode = null;
	private long points = 0;

	public UserEntity() {

	}

	public UserEntity(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
		setVerificationCode(Constants.DEFAULT_VERIFICATION_CODE);
	}

	public UserEntity(NewUserForm newUserForm) {
		if (emailIsValid(newUserForm.getEmail()) && newUserForm.getUsername() != null
				&& newUserForm.getRole() != null) {
			this.email = newUserForm.getEmail();
			this.username = newUserForm.getUsername();
			this.avatar = newUserForm.getAvatar();
			this.role = newUserForm.getRole();
		} else
			throw new RegisterNewUserException("Registration data is not correct. Check your input");
		setPoints(0);
		setPlayground(Constants.PLAYGROUND_NAME);
	}
	@Transient
	public static boolean emailIsValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	public UserEntity(String username, String email, String avatar, String role, String playground, String code) {
		this(username, email, avatar, role, playground);
		setVerificationCode(code);
	}

	@Transient
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Id
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

	@Transient
	public String getRole() {
		return role;
	}
	@Transient
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

	@Lob
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
	@Transient
	public void verifyUser() {
		setVerified_user(Constants.USER_VERIFIED);
		verificationCode = null;
	}
	
	@Transient
	public boolean isVerified() {
		if (getVerified_user() == Constants.USER_VERIFIED)
			return true;
		else
			return false;
	}

	@Override
	@Lob
	public String toString() {
		return "UserEntity [email=" + email + ", avatar=" + avatar + ", username=" + username + ", playground="
				+ playground + ", role=" + role + ", verificationCode=" + verificationCode + ", verified_user="
				+ verified_user + ", points=" + points + "]";
	}

}
// elia:
// there is a problem with functions in hibernate :becuse the object is en entity ,in class functions must be ignored
//another problem is to understend what exactly is the key in the database of USERS
