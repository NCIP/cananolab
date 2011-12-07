package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 *
 */
public class CommunityException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CommunityException() {
		super("Exception working with community related services");
	}

	public CommunityException(String message) {
		super(message);
	}

	public CommunityException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunityException(Throwable cause) {
		super(cause);
	}
}
