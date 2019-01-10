package playground.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class User {
	
	// User types
	
	public static String DEFAULT_USERNAME = "DEFAULT_USERNAME";
	
	public static final String MANAGER_ROLE = "MANAGER";
	public static final String PLAYER_ROLE = "PLAYER";
	public static final String UNDEFINED_ROLE = "NO_ROLE";
	
	// Test values 
	
	public static final String EMAIL_FOR_TESTS = "nudnik@mail.ru";
	public static final String Other_Email_For_Test = "OtherEmail@gmail.com";
	
	public static final String AVATAR_FOR_TESTS = "MyAvatar.jpg";
	public static final String ID_FOR_TESTS = "308748323";
	
	public static final String DEFAULT_VERIFICATION_CODE = "v3r1f1c@ti0nC0d3";
	public static final String USER_FIELD_USER_VERIFICATION_FIELD = "verificationCode";	
	
	
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		User.DEFAULT_USERNAME = defaultUsername;
	}

	
	public User() {
		
	}
}
