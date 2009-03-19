package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class FileException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public FileException() {
		super("Exception working with files");
	}

	public FileException(String message) {
		super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(Throwable cause) {
		super(cause);
	}
}
