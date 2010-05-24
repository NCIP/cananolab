package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class GridQueryException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public GridQueryException() {
		super("Error in query against grid node");
	}

	public GridQueryException(String message) {
		super(message);
	}

	public GridQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public GridQueryException(Throwable cause) {
		super(cause);
	}
}
