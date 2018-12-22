package playground;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
	public static String PLAYGROUND_MAIL="PLAYGROUND_MAIL";
	public static String PLAYGROUND_NAME="PLAYGROUND_NAME";
	public static final String MODERATOR_ROLE = "MODERATOR";
	public static final String PLAYER_ROLE = "PLAYER";
	public static final String UNDEFINED_ROLE = "NO_ROLE";
	public static final String ELEMENT_NAME = "DEFAULT_ELEMENT_NAME";
	

	public static final String GAME_RULES = "Rules are simple. We ask and you answer. \r\n";
	public static final String MESSAGEBOARD = "MESSAGE_BOARD";
	public static final String QUESTION = "QUESTION";
	public static final String DEFAULT_VERIFICATION_CODE = "1234";

	public static String DEFAULT_USERNAME ="DEFAULT_USERNAME";
	public static final String DEFAULT_ACTIVITY_TYPE = "echo";
	public static final int USER_VERIFIED = 1;
	public static final int USER_NOT_VERIFIED = 0;
	public static final String ELEMENT_DEFAULT_TYPE = "Element";
	public static final String EMAIL_FOR_TESTS = "nudnik@mail.ru";
	public static final String ID_FOR_TESTS = "308748323";
	public static final String CREATOR_PLAYGROUND_FOR_TESTS = "creatorPlaygroundName";
	public static final String AVATAR_FOR_TESTS = "MyAvatar.jpg";
	
	public static int DEFAULT_EXPIRATION_YEAR;
	public static int DEFAULT_EXPIRATION_MONTH;
	public static int DEFAULT_EXPIRATION_DAY;
	//
	public static final String MESSAGE_NAME = "MESSAGE";
	public static final String MESSAGE_READ = "MESSAGE_READ";
	public static final String MESSAGE_WRITE = "MESSAGE_WRITE";
	public static final String MESSAGE_DELETE = "MESSAGE_DELETE";
	
	public static String MESSAGE_ID_ATTR = "MessageID";
	
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Constants.PLAYGROUND_NAME = defaultPlaygroundName;
	}
	
	@Value("${playground.mail:playgroundrolnik@gmail.com}")
	public void setDefaultPlaygroundMail(String defaultPlaygroundMail) {
		Constants.PLAYGROUND_MAIL = defaultPlaygroundMail;
	}

	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		Constants.DEFAULT_USERNAME = defaultUsername;
	}
	
	@Value("${playground.default.exp.month:1}")
	public void setDefaultExpMonth(int DEFAULT_EXPIRATION_MONTH) {
		Constants.DEFAULT_EXPIRATION_MONTH = DEFAULT_EXPIRATION_MONTH;
	}
	
	@Value("${playground.default.exp.year:3000}")
	public void setDefaultExpYear(int DEFAULT_EXPIRATION_YEAR) {
		Constants.DEFAULT_EXPIRATION_YEAR = DEFAULT_EXPIRATION_YEAR;
	}
	
	@Value("${playground.default.exp.day:1}")
	public void setDefaultExpDay(int DEFAULT_EXPIRATION_DAY) {
		Constants.DEFAULT_EXPIRATION_DAY = DEFAULT_EXPIRATION_DAY;
	}

	public Constants() {

	}
}
