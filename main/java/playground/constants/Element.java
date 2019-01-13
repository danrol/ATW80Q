package playground.constants;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Element {

	public static final String DEFAULT_ELEMENT_NAME = "Rolnik_element";

	// Element types

	public static final String DEFAULT_TYPE = "DEFAULT_ELEMENT";
	public static final String ELEMENT_QUESTION_TYPE = "QUESTION_TYPE";
	public static final String ELEMENT_MESSAGEBOARD_TYPE = "MESSAGE_BOARD_TYPE";

	// Element Attribute types

	public static final String MESSAGEBOARD_MESSAGE_COUNT = "MESSAGES_COUNT";
	public static final String ELEMENT_QUESTION_KEY = "QUESTION_BODY_KEY"; // Actual question
	public static final String ELEMENT_ANSWER_KEY = "ANSWER_KEY"; // Answer to question
	public static final String ELEMENT_POINT_KEY = "POINTS_VALUE"; // Answer to question

	public static final String NAME_FIELD = "name";
	public static final String TYPE_FIELD = "type";
	public static final String QUESTION_TITLE_TEST = "1+1";
	public static final String QUESTION_BODY_TEST = "1+1?";
	public static final String QUESTION_CORRECT_ANSWER_TEST = "2";
	public static final int QUESTION_POINT_VALUE_TEST = 5;

	public static final String ELEMENT_FIELD_creationDate = "creationDate";
	public static final String ELEMENT_FIELD_creatorEmail = "creatorEmail";
	public static final String ELEMENT_FIELD_creatorPlayground = "creatorPlayground";
	public static final String ELEMENT_FIELD_id = "id";
	public static final String ELEMENT_FIELD_superkey = "superkey";

	// Error messages

	public static final String MESSAGEBOARD_NOT_FOUND_ERROR = "No such Message Board : ";
	public static final String QUESTION_NOT_FOUND_ERROR = "No question found in database";
	public static final String QUESTION_TYPE_ELEMENT_EXPECTED_ERROR = "Invalid element - expected ELEMENT_QUESTION_TYPE";
	public static final String NO_SUCH_ELEMENT_ERROR = "Could not find element : ";
	public static final String NEGATIVE_DISTANCE_ERROR = "Negative distance : ";
	public static final String CANNOT_MODIFY_KEY_VALUES_ERROR = "Cannot change elements key values : \"Id\" or \"creatorPlayground\"";
	public static final String ELEMENT_TYPE_INVALID_ERROR = "Element does not meet type requirements. View reference for Element types.";
	public static final String ADD_ELEMENT_FAIL_DUPLICATE_ERROR = "Cannot add already existing element key.";

	// For tests related to Element

	public static int DEFAULT_EXPIRATION_YEAR;
	public static int DEFAULT_EXPIRATION_MONTH;
	public static int DEFAULT_EXPIRATION_DAY;

	public static final Date EXP_DATE = new Date(2100, 1, 1);

	@Value("${playground.default.exp.month:1}")
	public void setDefaultExpMonth(int DEFAULT_EXPIRATION_MONTH) {
		Element.DEFAULT_EXPIRATION_MONTH = DEFAULT_EXPIRATION_MONTH;
	}

	@Value("${playground.default.exp.year:3000}")
	public void setDefaultExpYear(int DEFAULT_EXPIRATION_YEAR) {
		Element.DEFAULT_EXPIRATION_YEAR = DEFAULT_EXPIRATION_YEAR;
	}

	@Value("${playground.default.exp.day:1}")
	public void setDefaultExpDay(int DEFAULT_EXPIRATION_DAY) {
		Element.DEFAULT_EXPIRATION_DAY = DEFAULT_EXPIRATION_DAY;
	}

	public Element() {

	}

}
