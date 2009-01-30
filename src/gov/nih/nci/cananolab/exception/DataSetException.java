package gov.nih.nci.cananolab.exception;

/**
 *
 * @author pansu
 *
 */

public class DataSetException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public DataSetException() {
		super("Exception occurred working with Instruments and Techniques");
	}

	public DataSetException(String message) {
		super(message);
	}

	public DataSetException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSetException(Throwable cause) {
		super(cause);
	}
}
