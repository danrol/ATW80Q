package playground.logic;

import java.util.Random;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import playground.constants.Constants;
import playground.constants.User;

//KEY IS EMAIL+PLAYGROUND
@Entity
@Table(name = "USER")
public class UserEntity {

	private String email;
	private String avatar;
	private String username;
	private String playground;
	private String role = User.UNDEFINED_ROLE;
	private String verificationCode = Constants.DEFAULT_VERIFICATION_CODE;
	private String superkey;
	private String id;
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
		this.verificationCode = generateCode();
	}

	public UserEntity(NewUserForm user) {
		this(user.getUsername(), user.getEmail(), user.getAvatar(), user.getRole(), User.PLAYGROUND_NAME);
	}

	@Transient
	private static String generateCode() {
		Random r = new Random();
		return String.valueOf(r.nextInt((9999 - 1000) + 1) + 1000);
		// return Constants.DEFAULT_VERIFICATION_CODE;
	}

	@Id
	public String getSuperkey() {
		return email.concat(" " + playground);
	}

	public void setSuperkey(String superkey) {
		// empty
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (emailIsValid(email))
			this.email = email;
		else
			throw new RegisterNewUserException("Registration data is not correct. Check your input");
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
		if (role.equals(User.MANAGER_ROLE.toLowerCase())) {
			this.role = User.MANAGER_ROLE;
		} else if (role.equals(User.PLAYER_ROLE.toLowerCase())) {
			this.role = User.PLAYER_ROLE;
		} else {
			this.role = User.UNDEFINED_ROLE;
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

	@Transient
	public void verifyUser() {
		verificationCode = null;
	}

	@Transient
	public boolean isVerified() {
		if (this.verificationCode == null)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "UserEntity [superkey=" + superkey + ",id=" + id + " email=" + email + ", avatar=" + avatar
				+ ", username=" + username + ", playground=" + playground + ", role=" + role + ", verificationCode="
				+ verificationCode + ", points=" + points + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;

		if (this.getSuperkey().equals(other.getSuperkey()))
			return true;
		else
			return false;
	}

}
