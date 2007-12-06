package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class GridQueryException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public GridQueryException() {
		super("Error in auto-discovering grid nodes");
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
