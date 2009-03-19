package gov.nih.nci.cananolab.exception;

/**
 *
 * @author pansu
 *
 */

public class CharacterizationResultException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CharacterizationResultException() {
		super("Exception occurred working with Instruments and Techniques");
	}

	public CharacterizationResultException(String message) {
		super(message);
	}

	public CharacterizationResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public CharacterizationResultException(Throwable cause) {
		super(cause);
	}
}
