package gov.nih.nci.cananolab.exception;

/**
 *
 * @author pansu
 *
 */

public class InstrumentTechniqueException extends CaNanoLabException {

	private static final long serialVersionUID = 1234567890L;

	public InstrumentTechniqueException() {
		super("Exception occurred working with Instruments and Techniques");
	}

	public InstrumentTechniqueException(String message) {
		super(message);
	}

	public InstrumentTechniqueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InstrumentTechniqueException(Throwable cause) {
		super(cause);
	}
}
