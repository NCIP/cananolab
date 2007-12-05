package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class FileNotFoundException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	private String message;

	public FileNotFoundException() {
		super();
		this.message = "File is not found.";
	}

	public FileNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public FileNotFoundException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public FileNotFoundException(Throwable cause) {
		super(cause);
		this.message = "File is not found.";
	}

	public String getMessage() {
		return this.message;
	}
}
