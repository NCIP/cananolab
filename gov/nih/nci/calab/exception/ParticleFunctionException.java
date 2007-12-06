package gov.nih.nci.calab.exception;

/**
 * @author pansu
 * 
 */
public class ParticleFunctionException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public ParticleFunctionException() {
		super("Exception working with particle functions");
	}

	public ParticleFunctionException(String message) {
		super(message);
	}

	public ParticleFunctionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParticleFunctionException(Throwable cause) {
		super(cause);
	}
}
