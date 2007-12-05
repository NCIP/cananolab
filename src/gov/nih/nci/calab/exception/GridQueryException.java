package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class GridQueryException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	private String message;

	public GridQueryException() {
		super();
		this.message = "Error in auto-discovering grid nodes.";
	}

	public GridQueryException(String message) {
		super(message);
		this.message = message;
	}

	public GridQueryException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public GridQueryException(Throwable cause) {
		super(cause);
		this.message = "Grid problems.";
	}

	public String getMessage() {
		return this.message;
	}
}
