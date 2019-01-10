package playground.constants;

import org.springframework.beans.factory.annotation.Value;

public class User {
	public static String PLAYGROUND_MAIL = "PLAYGROUND_MAIL";
	public static String PLAYGROUND_NAME = "PLAYGROUND_NAME";
	public static final String Other_Playground = "OtherPlayground";
	public static final String MANAGER_ROLE = "MANAGER";
	public static final String PLAYER_ROLE = "PLAYER";
	public static final String UNDEFINED_ROLE = "NO_ROLE";
	
	@Value("${playground.mail:playgroundrolnik@gmail.com}")
	public void setDefaultPlaygroundMail(String defaultPlaygroundMail) {
		User.PLAYGROUND_MAIL = defaultPlaygroundMail;
	}
	
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		User.PLAYGROUND_NAME = defaultPlaygroundName;
	}
}
