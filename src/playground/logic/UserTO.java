package playground.logic;

import java.io.Serializable;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;

import playground.Constants;
import playground.database.Database;

public class UserTO implements Serializable{

	private String email = "";
	private String avatar = "";
	private String username;
	private String playground;
	private String role = Constants.UNDEFINED_ROLE;
	private String verificationCode = "";
	private int verified_user = Constants.USER_NOT_VERIFIED;
	private long points = 0;
	private Database db;


	public UserTO(){
	}

	public UserTO(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
		setVerificationCode("0");
		// verification is done separately
	}

	public UserTO(NewUserForm newUserForm) {
		if(emailIsValid(newUserForm.getEmail()) && newUserForm.getUsername() 
				!= null && newUserForm.getRole() != null) {
		this.email = newUserForm.getEmail();
		this.username = newUserForm.getUsername();
		this.avatar = newUserForm.getAvatar();
		this.role = newUserForm.getRole();
		}
		else
			throw new RegisterNewUserException("registration data is not correct. Check your input");
		setPoints(0);
		setPlayground(Constants.PLAYGROUND_NAME);
	}


	public static boolean emailIsValid(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
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

	public void verifyUser() {
		setVerified_user(Constants.USER_VERIFIED);
	}

	public boolean isVerified() {
		if (getVerified_user() == Constants.USER_VERIFIED)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "UserTO [email=" + email + ", avatar=" + avatar + ", username=" + username + ", playground=" + playground
				+ ", role=" + role + ", verificationCode=" + verificationCode + ", verified_user=" + verified_user + ", points=" + points + ", db=" + db + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}