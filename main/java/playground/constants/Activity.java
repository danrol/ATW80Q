package playground.constants;

import org.springframework.stereotype.Component;

@Component
public class Activity {
	public static final String DEFAULT_ACTIVITY_TYPE = "echo";
	
	// Activity types

		public static final String GET_SCORES_ACTIVITY = "GET_SCORES_ACTIVITY";
		public static final String GET_MESSAGES_ACTIVITY = "MESSAGE_READ_ACTIVITY";
		public static final String MESSAGE_ACTIVITY = "MESSAGE_WRITE_ACTIVITY";

		public static final String QUESTION_READ_ACTIVITY = "QUESTION_READ_ACTIVITY";
		public static final String QUESTION_ANSWER_ACTIVITY = "QUESTION_ANSWER_ACTIVITY";
		public static final String GET_GAME_RULES_ACTIVITY = "GAME_RULES_ACTIVITY";
		
		// Activity attribute keys
		
		public static final String ACTIVITY_QUESTION_ID_KEY = "USER_CHOICE_OF_QUESTION_ID";//TODO:Eden Sharoni:  not used in code.
		public static final String ACTIVITY_USER_ANSWER_KEY = "User_answer";
		public static final String USER_ID_OF_ACTIVITY_KEY = "User_id"; //TODO:Eden Sharoni:  not used in code.
		public static final String ACTIVITY_MESSAGE_KEY = "Message"; // String of the actual message
		
		// test values
		
		public static final String attributeName = "attribute";
		public static final String attrValue = "attrValue";
		public static final String noSuchAttribute = "noSuchAttribute";
		public static final String wrongAttributeValue = "wrongAttributeValue";

		// Rule for the game
		
		public static final String GAME_RULES = "Rules are simple. We ask and you answer. \r\n";
				
		public static final boolean CORRECT_ANSWER = true;
		public static final boolean WRONG_ANSWER = false;

		public static final String ACTIVITY_TYPE_NOT_RECOGNIZED_ERROR = "No such activity type.";
		
		public static final int PAGE_NUMBER = 1; //TODO: Eden Sharoni: Don't think this is necessary
		public static final int SIZE_NUMBER = 5; 

		public Activity() {
			
		}
}
