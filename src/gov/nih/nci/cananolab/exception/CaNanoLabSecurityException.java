package gov.nih.nci.cananolab.exception;


/**
 * @author pansu
 * 
 */
public class CaNanoLabSecurityException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public CaNanoLabSecurityException() {
		super("Exception in user authentication and authorization");
	}

	public CaNanoLabSecurityException(String message) {
		super(message);
	}

	public CaNanoLabSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaNanoLabSecurityException(Throwable cause) {
		super(cause);
	}
}
