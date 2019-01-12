package playground.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Playground {
	
	// URL API
	
	public static final String Function_1 = "/playground/users";
	public static final String Function_2 = "/playground/users/confirm/{playground}/{email}/{code}";
	public static final String Function_3 = "/playground/users/login/{playground}/{email}";
	public static final String Function_4 = "/playground/users/{playground}/{email}";
	public static final String Function_5 = "/playground/elements/{userPlayground}/{email}";
	public static final String Function_6 = "/playground/elements/{userPlayground}/{email}/{playground}/{id}";
	public static final String Function_7 = "/playground/elements/{userPlayground}/{email}/{playground}/{id}";
	public static final String Function_8 = "/playground/elements/{userPlayground}/{email}/all";
	public static final String Function_9 = "/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}";
	public static final String Function_10 = "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}";
	public static final String Function_11 = "/playground/activities/{userPlayground}/{email}";
	
	
	//Email values
	
	public static final String VERIFICATION_MAIL_SUBJECT = "Verify your account ATW80Q";

	
	// Values for JUnit tests
	public static String PLAYGROUND_MAIL = "ATW80Q@gmail.com";
	public static String PLAYGROUND_NAME = "PLAYGROUND_NAME";
	public static boolean MESSAGE_SENDER_ENABLED = true;
	
	public static final String Other_Playground = "OtherPlayground";
	public static final String CREATOR_PLAYGROUND_FOR_TESTS = "creatorPlaygroundName";
	
	public static final String DUMMY_MANAGER_USERNAME = "manager";	
	public static final String DUMMY_PLAYER_USERNAME = "player";	
	public static final String DUMMY_ADMIN_USERNAME = "admin";
	
	
	
	//Error messages
	
	public static final String NO_RELEVANT_MESSAGE_ERROR = "There is no relevant message";
	public static final String NO_SUCH_ATTRIBUTE_NAME = "There is no such attribute name in system";
	

	

	
	@Value("${playground.mail.enable:false}")
	public void setDefaultMailEnabled(boolean MESSAGE_SENDER_ENABLED) {
		Playground.MESSAGE_SENDER_ENABLED = MESSAGE_SENDER_ENABLED;
	}
	
	@Value("${playground.mail:ATW80Q@gmail.com}")
	public void setDefaultPlaygroundMail(String defaultPlaygroundMail) {
		Playground.PLAYGROUND_MAIL = defaultPlaygroundMail;
	}
	
	
	@Value("${playground.name:playgroundrolnik}")
	public void setDefaultUserName(String defaultPlaygroundName) {
		Playground.PLAYGROUND_NAME = defaultPlaygroundName;
	}
	
	public Playground() {
		
	}


}
