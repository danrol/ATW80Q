package playground.exceptions;

public class RegisterNewUserException extends RuntimeException {

	public RegisterNewUserException(String exceptionMessage){
		super(exceptionMessage);
	}
}
