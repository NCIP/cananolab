package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class ParticleException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public ParticleException() {
		super("Exception working with particles");
	}

	public ParticleException(String message) {
		super(message);
	}

	public ParticleException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParticleException(Throwable cause) {
		super(cause);
	}
}
