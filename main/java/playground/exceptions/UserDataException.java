package playground.exceptions;

public class UserDataException extends RuntimeException {

	private static final long serialVersionUID = 3886423450545597890L;

	public UserDataException(String errorMessage) {
		super(errorMessage);
	}
}
