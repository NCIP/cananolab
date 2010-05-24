package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class GridAutoDiscoveryException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public GridAutoDiscoveryException() {
		super("Error in auto-discovering grid nodes");
	}

	public GridAutoDiscoveryException(String message) {
		super(message);
	}

	public GridAutoDiscoveryException(String message, Throwable cause) {
		super(message, cause);
	}

	public GridAutoDiscoveryException(Throwable cause) {
		super(cause);
	}
}
