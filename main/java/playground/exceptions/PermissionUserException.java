package playground.exceptions;

public class PermissionUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PermissionUserException(String string) {
		super(string);
	}

}
