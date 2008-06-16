package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class ParticleCompositionException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public ParticleCompositionException() {
		super("Exception occurred working with particle composition.");
	}

	public ParticleCompositionException(String message) {
		super(message);
	}

	public ParticleCompositionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParticleCompositionException(Throwable cause) {
		super(cause);
	}
}
