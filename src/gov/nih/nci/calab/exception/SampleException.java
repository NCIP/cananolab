package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class SampleException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public SampleException() {
		super("Exception ocurred working with samples/aliquots");
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
