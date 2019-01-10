package playground.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class User {
	
	// User types
	
	public static String DEFAULT_USERNAME = "DEFAULT_USERNAME";
	
	public static final String MANAGER_ROLE = "MANAGER";
	public static final String PLAYER_ROLE = "PLAYER";
	public static final String GUEST_ROLE = "GUEST";
	
	// Test values 
	
	public static final String EMAIL_FOR_TESTS = "nudnik@mail.ru";
	public static final String Other_Email_For_Test = "OtherEmail@gmail.com";
	
	public static final String AVATAR_FOR_TESTS = "MyAvatar.jpg";
	public static final String ID_FOR_TESTS = "308748323";
	
	public static final String DEFAULT_VERIFICATION_CODE = "v3r1f1c@ti0nC0d3";
	public static final String USER_FIELD_USER_VERIFICATION_FIELD = "verificationCode";

	
	//Error messages
	public static final String USER_ALREADY_REGISTERED_ERROR = "User already registered";
	public static final String UPDATE_ANOTHER_USER_ERROR = "Users cannot update another user's information.";
	public static final String UPDATE_FAIL_ERROR = "Failed to update user";
	public static final String VERIFICATION_CODE_MISMATCH_ERROR = "Invalid verification code";
	public static final String EMAIL_NOT_REGISTERED_ERROR = "Email is not registered.";
	public static final String USER_NOT_IN_PLAYGROUND_ERROR = "User does not belong to the specified playground : ";
	public static final String DUPLICATE_USER_KEY_ERROR = "User already exists with name: ";
	public static final String USER_NOT_VERIFIED_ERROR = "User is not verified.";
	public static final String LOGIN_ASPECT_ACCESS_RIGHTS_ERROR = " has no access rights to ";


	
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		User.DEFAULT_USERNAME = defaultUsername;
	}

	
	public User() {
		
	}
}
