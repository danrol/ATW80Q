package playground.constants;

import org.springframework.beans.factory.annotation.Value;

public class Playground {
	public static String PLAYGROUND_MAIL = "PLAYGROUND_MAIL";
	public static String PLAYGROUND_NAME = "PLAYGROUND_NAME";
	public static final String Other_Playground = "OtherPlayground";
	
	
	
	@Value("${playground.mail:playgroundrolnik@gmail.com}")
	public void setDefaultPlaygroundMail(String defaultPlaygroundMail) {
		Playground.PLAYGROUND_MAIL = defaultPlaygroundMail;
	}
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Playground.PLAYGROUND_NAME = defaultPlaygroundName;
	}
}
