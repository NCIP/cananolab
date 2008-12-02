package gov.nih.nci.cananolab.exception;

/**
 * @author tanq
 * 
 */
public class OrganizationException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public OrganizationException() {
		super("Exception occurred working with organizations");
	}

	public OrganizationException(String message) {
		super(message);
	}

	public OrganizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrganizationException(Throwable cause) {
		super(cause);
	}
}
