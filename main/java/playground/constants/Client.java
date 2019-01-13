package playground.constants;

import java.awt.Font;

import org.springframework.stereotype.Component;

@Component
public class Client {
	
	public static final String ATW80Q = "Around The World In 80 Questions";
	
	//Client Model
	public static final String PLAYGROUND_NAME = "playground.rolnik";
	
	// Sign Up
	public static final String PLAYGROUND_LABEL = "Playground";
	public static final String EMAIL_LABEL = "Email";
	public static final String USERNAME_LABEL = "Username";
	public static final String AVATAR_LABEL = "Avatar";
	public static final String ROLE_LABEL = "Role";
	public static final String PLAYER_ROLE = "PLAYER";
	public static final String MANAGER_ROLE = "MANAGER";
	
	public static final String CHOOSE_ROLE_LABEL = "Choose role --> Default is Player";
	public static final String SET_ROLE_TITLEBORDER = "Set Role";
	
	//Verification code window
	public static final String VERIFY_CODE_TITLE = "Enter your verification code";
	
	//Classes
	public static final String UPDATE_USER = "Update User";
	public static final String GAME_CONTROLLER = "Game Controller";
	public static final String VERIFY_CODE = "Verify User";
	
	// Sign In/Out/Up
	public static final String SIGN_IN = "Sign In";
	public static final String SIGN_UP = "Sign Up";
	public static final String SIGN_OUT = "Sign Out";
	
	//Activites
	public static final String CHOOSE = "Choose";
	public static final String ADD_QUESTION = "Add Question";
	public static final String GAME_RULES = "Game Rules";
	public static final String GET_MESSAGE = "Get Message";
	public static final String ADD_MESSAGE = "Add Message";
	public static final String ANSWER_QUESTION = "Answer Questions";
	public static final String VIEW_HIGH_SCORES = "View scores";
	
	//ComboBox Activites
	public static final String[] MANAGER_COMBOX = {Client.CHOOSE, Client.ADD_QUESTION};
	public static final String[] PLAYER_COMBOX = {Client.CHOOSE ,Client.GAME_RULES, Client.ANSWER_QUESTION, Client.VIEW_HIGH_SCORES};

	//Answer Question
	public static final String CHOOSE_QUESTION = "Choose Question";
	public static final String QUESTION = "Question";
	public static final String ANSWER = "Answer";
	public static final String SEND = "Send";
	public static final String MESSAGE = "Message";
	public static final String NEXT_PAGE = "Next page";
	public static final String PREVIOUS_PAGE = "Previous page";
	
	
	
	// Add question
	public static final String QUESTION_TITLE = "Question Title";
	public static final String QUESTION_BODY = "Question Body";
	public static final String POINTS = "Points";
	
	//Names
	public static final String EDEN_SHARONI = "Eden Sharoni - 315371906";
	public static final String EDEN_DUPONT = "Eden Dupont - 204808596";
	public static final String ELIA_BEN_ANAT = "Elia Ben Anat - 308048388";
	public static final String DANIEL_ROLNIK = "Daniel Rolnik - 334018009";
	
	//Error Messages
	public static final Object CANNOT_VERIFY_ERROR_MESSAGE = "User could not be verified, check your information.";
	public static final Object CANNOT_VERIFY_ERROR_MESSAGE_SIGN_UP_ERROR_MESSAGE = "Could not sign up, this mail may already be registered or the server is not running.";
	public static final String SIGN_IN_ERROR = "Could not sign in. Check that you are verified.";
	public static final Object CANNOT_UPDATE_USER = "Your account could not be updated. Check your connection to the server";
	public static final Object CONNECTION_PROBLEM = "Couldn't process your request. Check your connection to the server";
	
	//Font

	public static final Font FONT_TITLE = new Font("Arial", Font.BOLD| Font.HANGING_BASELINE, 30);
	public static final Font FONT_BASIC = new Font("Arial", Font.BOLD| Font.HANGING_BASELINE, 15);

	public static final String INVALID_POINT_INPUT_ERROR = "Points should be a number";

	public static final String SUCCESSFULLY_ADDED_QUESTION = "The question has been successfully added.";

	public static final String CORRECT_ANSWER_MESSAGE = "Correct answer!";
	public static final String INCORRECT_ANSWER_MESSAGE = "Wrong answer!";




}
