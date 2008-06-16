package gov.nih.nci.cananolab.exception;

/**
 * @author zengje
 * 
 */

public class CaNanoLabException extends Exception {

	private static final long serialVersionUID = 1234567890L;

	/**
	 * 
	 */
	public CaNanoLabException() {
		super("An exception occurred in caNanoLab");
	}

	public CaNanoLabException(String message) {
		super(message);

	}

	public CaNanoLabException(String message, Throwable cause) {
		super(message, cause);

	}

	public CaNanoLabException(Throwable cause) {
		super(cause);
	}
}
