package gov.nih.nci.cananolab.exception;

/**
 *
 * @author pansu
 *
 */

public class ExperimentConfigException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public ExperimentConfigException() {
		super("Exception occurred working with Instruments and Techniques");
	}

	public ExperimentConfigException(String message) {
		super(message);
	}

	public ExperimentConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExperimentConfigException(Throwable cause) {
		super(cause);
	}
}
