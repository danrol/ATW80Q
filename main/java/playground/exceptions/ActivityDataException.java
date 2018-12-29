package playground.exceptions;

public class ActivityDataException extends RuntimeException {

	private static final long serialVersionUID = 3886423450545597890L;

	public ActivityDataException(String errorMessage) {
		super(errorMessage);
	}
}
