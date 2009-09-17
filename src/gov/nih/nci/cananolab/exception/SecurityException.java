package gov.nih.nci.cananolab.exception;


/**
 * @author pansu
 * 
 */
public class SecurityException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public SecurityException() {
		super("Exception in user authentication and authorization");
	}

	public SecurityException(String message) {
		super(message);
	}

	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityException(Throwable cause) {
		super(cause);
	}
}
