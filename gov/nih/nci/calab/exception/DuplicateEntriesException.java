package gov.nih.nci.calab.exception;

/**
 * @author zengje
 * 
 */
public class DuplicateEntriesException extends CalabException {

	private static final long serialVersionUID = 1234567890L;

	private String message;

	public DuplicateEntriesException() {
		super();
		this.message = "Duplicate entries are found.";
	}

	public DuplicateEntriesException(String message) {
		super(message);
		this.message = message;
	}

	public DuplicateEntriesException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public DuplicateEntriesException(Throwable cause) {
		super(cause);
		this.message = "Duplicate entries are found.";
	}

	public String getMessage() {
		return this.message;
	}
}
