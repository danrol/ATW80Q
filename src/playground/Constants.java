package playground;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

	public static String MODERATOR_ROLE = "MODERATOR";
	public static String PLAYER_ROLE = "PLAYER";
	public static String UNDEFINED_ROLE = "NO_ROLE";
	public static String GAME_RULES = "Our vision is to become a part of the official school program.\r\n"
			+ "We aim to improve the memory of students on facts about the world in all topics, and to become the best tool in the market which could help teachers create more interactive lessons for their students. \r\n"
			+ "For students it will be an alternative way to improving their knowledge in the school material and possibly quiz themselves before tests. \r\n"
			+ "";
	public static String ONLINE = "ONLINE";
	public static String OFFLINE = "OFFLINE";
	public static String LESSON = "LESSON";
	public static String MESSAGEBOARD = "MESSAGE_BOARD";
	public static String QUESTION = "QUESTION";
	public static String PLAYGROUND_NAME;
	public static String DEFAULT_USERNAME = "playground.rolnik";
	public static int USER_VERIFIED = 1;
	public static int USER_NOT_VERIFIED = 0;
	
	@Value("${playground.name:playground.rolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Constants.PLAYGROUND_NAME = defaultPlaygroundName;
	}
	
	@Value("${playground.default.username:noname}")
	public void setDefaultUsername(String defaultUsername) {
		Constants.DEFAULT_USERNAME = defaultUsername;
	}
}
