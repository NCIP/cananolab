package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class SampleException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public SampleException() {
		super("Exception working with particles");
	}

	public SampleException(String message) {
		super(message);
	}

	public SampleException(String message, Throwable cause) {
		super(message, cause);
	}

	public SampleException(Throwable cause) {
		super(cause);
	}
}
