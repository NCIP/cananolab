package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class NotExistException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public NotExistException() {
		super("The record doesn't exist in the system.");
	}

	public NotExistException(String message) {
		super(message);
	}

	public NotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotExistException(Throwable cause) {
		super(cause);
	}
}
