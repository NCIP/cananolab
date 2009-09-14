package gov.nih.nci.cananolab.exception;

/**
 * This class represents the exception to be thrown when the web session expires
 * or when the user is not logged in.
 * 
 * @author pansu
 * 
 */
public class InvalidSessionException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public InvalidSessionException() {
		super("User is not logged in or session expired");
	}

	public InvalidSessionException(String message) {
		super(message);
	}

	public InvalidSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSessionException(Throwable cause) {
		super(cause);
	}
}
