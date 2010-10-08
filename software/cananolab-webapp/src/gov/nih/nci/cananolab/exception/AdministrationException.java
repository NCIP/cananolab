package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 *
 */
public class AdministrationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public AdministrationException() {
		super("Exception working with curation related services");
	}

	public AdministrationException(String message) {
		super(message);
	}

	public AdministrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdministrationException(Throwable cause) {
		super(cause);
	}
}
