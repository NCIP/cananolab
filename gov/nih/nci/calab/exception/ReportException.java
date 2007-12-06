package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class ReportException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public ReportException() {
		super("Exception ocurred working with reports");
	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}
}
