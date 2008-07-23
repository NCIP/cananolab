package gov.nih.nci.cananolab.exception;

/**
 * @author tanq
 * 
 */
public class DocumentException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public DocumentException() {
		super("Exception occurred working with documents");
	}

	public DocumentException(String message) {
		super(message);
	}

	public DocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentException(Throwable cause) {
		super(cause);
	}
}
