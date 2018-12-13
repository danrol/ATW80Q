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
	//KEY IS EMAIL+PLAYGROUND
	private String email;
	private String avatar;
	private String username;
	private String playground;
	private String role = Constants.UNDEFINED_ROLE;
	private String verificationCode = null;
	private String superkey;
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
		setSuperkey();
	}

	public UserEntity(String username, String email, String avatar, String role, String playground, String code) {
		this(username, email, avatar, role, playground);
		setVerificationCode(code);
	}

	public UserEntity(NewUserForm user) {
		this(user.getUsername(),user.getEmail(),user.getAvatar(),user.getRole(),Constants.PLAYGROUND_NAME,createCode());
	}

	
	private String generateCode() {
		return Constants.DEFAULT_VERIFICATION_CODE;
	}

	@Id
	public String getSuperkey() {
		return superkey;
	}

	public void setSuperkey(String superkey) {
		this.superkey = superkey;
	}
	
	public void setSuperkey() {
		superkey = setSuperkey(email, playground);
	}
	@Transient
	public static String setSuperkey(String string1, String string2) {
		return string1.concat(" " + string2);
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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
	@Transient
	public String toString() {
		return "UserEntity [email=" + email + ", avatar=" + avatar + ", username=" + username + ", playground="
				+ playground + ", role=" + role + ", verificationCode=" + verificationCode + " points=" + points + "]";
	}

}
// elia:
// there is a problem with functions in hibernate :becuse the object is en entity ,in class functions must be ignored
//another problem is to understend what exactly is the key in the database of USERS
