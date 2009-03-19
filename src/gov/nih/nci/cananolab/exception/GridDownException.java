package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class GridDownException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public GridDownException() {
		super("Grid node is not available at this time.");
	}

	public GridDownException(String message) {
		super(message);
	}

	public GridDownException(String message, Throwable cause) {
		super(message, cause);
	}

	public GridDownException(Throwable cause) {
		super(cause);
	}
}
