package playground.exceptions;

public class ElementDataException extends RuntimeException {

	private static final long serialVersionUID = 3886423450545597890L;

	public ElementDataException(String errorMessage) {
		super(errorMessage);
	}
}
