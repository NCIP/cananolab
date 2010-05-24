package gov.nih.nci.cananolab.exception;

/**
 * @author tanq
 * 
 */
public class PublicationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public PublicationException() {
		super("Exception occurred working with publications");
	}

	public PublicationException(String message) {
		super(message);
	}

	public PublicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PublicationException(Throwable cause) {
		super(cause);
	}
}
