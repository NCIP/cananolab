package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class GridAutoDiscoveryException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	private String message;

	public GridAutoDiscoveryException() {
		super();
		this.message = "Error in auto-discovering grid nodes.";
	}

	public GridAutoDiscoveryException(String message) {
		super(message);
		this.message = message;
	}

	public GridAutoDiscoveryException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public GridAutoDiscoveryException(Throwable cause) {
		super(cause);
		this.message = "Grid problems.";
	}

	public String getMessage() {
		return this.message;
	}
}
