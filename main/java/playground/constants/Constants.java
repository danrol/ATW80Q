package playground.constants;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
	
	
	
	
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
