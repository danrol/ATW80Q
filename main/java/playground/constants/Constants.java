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
	
	
	
	public static final int USER_VERIFIED = 1; //TODO:Eden Sharoni:  not used in code.
	public static final int USER_NOT_VERIFIED = 0; //TODO:Eden Sharoni:  not used in code.

	
	
	public static final int PAGE_NUMBER = 1; //TODO: Eden Sharoni: Don't think this is necessary
	public static final int SIZE_NUMBER = 3; //TODO: Eden Sharoni: Don't think this is necessary

	

	public static int DEFAULT_EXPIRATION_YEAR;
	public static int DEFAULT_EXPIRATION_MONTH;
	public static int DEFAULT_EXPIRATION_DAY;
	
	@SuppressWarnings("deprecation")
	public static final Date EXP_DATE = new Date(Constants.DEFAULT_EXPIRATION_YEAR, Constants.DEFAULT_EXPIRATION_MONTH,
			Constants.DEFAULT_EXPIRATION_DAY);

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
