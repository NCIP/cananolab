package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class ParticleCharacterizationException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public ParticleCharacterizationException() {
		super("Exception occurred working with particle characterizations");
	}

	public ParticleCharacterizationException(String message) {
		super(message);
	}

	public ParticleCharacterizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParticleCharacterizationException(Throwable cause) {
		super(cause);
	}
}
