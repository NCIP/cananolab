package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 *
 */
public class CharacterizationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CharacterizationException() {
		super("Exception occurred working with particle characterizations");
	}

	public CharacterizationException(String message) {
		super(message);
	}

	public CharacterizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CharacterizationException(Throwable cause) {
		super(cause);
	}
}
