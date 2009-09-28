package gov.nih.nci.cananolab.exception;

/**
 * @author zengje
 * 
 */
public class DuplicateEntriesException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public DuplicateEntriesException() {
		super("Duplicate entries are found");
	}

	public DuplicateEntriesException(String message) {
		super(message);
	}

	public DuplicateEntriesException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateEntriesException(Throwable cause) {
		super(cause);
	}
}
