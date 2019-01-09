package playground.logic;

public class ErrorException {
	private String message;

	public ErrorException() {
	}

	public ErrorException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}