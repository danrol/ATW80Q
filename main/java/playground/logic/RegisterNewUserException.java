package playground.logic;

public class RegisterNewUserException extends RuntimeException {

	private static final long serialVersionUID = 2489070485816396153L;

	public RegisterNewUserException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
