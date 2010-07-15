package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 *
 */
public class CurationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CurationException() {
		super("Exception working with curation related services");
	}

	public CurationException(String message) {
		super(message);
	}

	public CurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CurationException(Throwable cause) {
		super(cause);
	}
}
