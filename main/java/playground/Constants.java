package playground;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Constants {
	public static String PLAYGROUND_NAME;
	public static String MODERATOR_ROLE = "MODERATOR";
	public static String PLAYER_ROLE = "PLAYER";
	public static String UNDEFINED_ROLE = "NO_ROLE";
	
	public static String GAME_RULES = "Rules are simple. We ask and you answer. \r\n";
	public static String MESSAGEBOARD = "MESSAGE_BOARD";
	public static String QUESTION = "QUESTION";
	public static String DEFAULT_VERIFICATION_CODE = "1234";
	
	public static String DEFAULT_USERNAME;
	public static String DEFAULT_ACTIVITY_TYPE = "echo";
	public static int USER_VERIFIED = 1;
	public static int USER_NOT_VERIFIED = 0;
	public static String ELEMENT_DEFAULT_TYPE = "Element";
	public static String EMAIL_FOR_TESTS = "nudnik@mail.ru";
	public static String ID_FOR_TESTS = "308748323";
	public static String CREATOR_PLAYGROUND_FOR_TESTS = "creatorPlaygroundName";
	public static String AVATAR_FOR_TESTS = "MyAvatar.jpg";
	
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Constants.PLAYGROUND_NAME = defaultPlaygroundName;
	}
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		Constants.DEFAULT_USERNAME = defaultUsername;
	}
	
	
	public Constants() {
		
	}
}
