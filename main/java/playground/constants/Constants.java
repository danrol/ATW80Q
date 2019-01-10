package playground.constants;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
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
	
	

	public static final String GAME_RULES = "Rules are simple. We ask and you answer. \r\n";

	public static final String QUESTION = "QUESTION";
	

	public static String DEFAULT_USERNAME = "DEFAULT_USERNAME";
	

	public static final int USER_VERIFIED = 1;
	public static final int USER_NOT_VERIFIED = 0;

	// Values for tests

	
	public static final String ID_FOR_TESTS = "308748323";
	public static final String CREATOR_PLAYGROUND_FOR_TESTS = "creatorPlaygroundName";
	public static final String AVATAR_FOR_TESTS = "MyAvatar.jpg";

	public static final double Zero_Distance = 0;
	public static final int PAGE_NUMBER = 1;
	public static final int SIZE_NUMBER = 3;

	

	

	public static final boolean CORRECT_ANSWER = true;
	public static final boolean WRONG_ANSWER = false;

	public static int DEFAULT_EXPIRATION_YEAR;
	public static int DEFAULT_EXPIRATION_MONTH;
	public static int DEFAULT_EXPIRATION_DAY;

	@SuppressWarnings("deprecation")
	public static final Date EXP_DATE = new Date(Constants.DEFAULT_EXPIRATION_YEAR, Constants.DEFAULT_EXPIRATION_MONTH,
			Constants.DEFAULT_EXPIRATION_DAY);

	

	

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
