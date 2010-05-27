package gov.nih.nci.cananolab.exception;

/**
 * This class represents the exception to be thrown when the user access a
 * function that he/she doesn't have access to.
 * 
 * @author pansu
 * 
 */
public class NoAccessException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public NoAccessException() {
		super("You don't have the required privileges to access this function");
	}

	public NoAccessException(String message) {
		super(message);
	}

	public NoAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoAccessException(Throwable cause) {
		super(cause);
	}
}
