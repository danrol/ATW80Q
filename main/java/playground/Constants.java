package playground;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Constants {

	public static String MODERATOR_ROLE = "MODERATOR";
	public static String PLAYER_ROLE = "PLAYER";
	public static String UNDEFINED_ROLE = "NO_ROLE";
	public static String GAME_RULES = "Rules are simple. We ask and you answer. \r\n"
			+ "";
	public static String LESSON = "LESSON";
	public static String MESSAGEBOARD = "MESSAGE_BOARD";
	public static String QUESTION = "QUESTION";
	public static String PLAYGROUND_NAME = "playground.rolnikNotFromProperties";
	public static String DEFAULT_USERNAME = "Curiosity";
	public static int USER_VERIFIED = 1;
	public static int USER_NOT_VERIFIED = 0;
	public static String LOCAL_HOST_ADDRESS = "";
	public static String ELEMENT_DEFAULT_TYPE = "Element";
	public static String EMAIL_FOR_TESTS = "nudnik@mail.ru";
	public static String ID_FOR_TESTS = "123";
	public static String CREATOR_PLAYGROUND_FOR_TESTS = "crPlayground";
	public static String AVATAR_FOR_TESTS = "ava";
	
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Constants.PLAYGROUND_NAME = defaultPlaygroundName;
	}
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		Constants.DEFAULT_USERNAME = defaultUsername;
	}
	
	//TODO fix the get values from properties
	
	public Constants() {
		
	}
}
