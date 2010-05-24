package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class ProtocolException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public ProtocolException() {
		super("Exception occurred working with protocols");
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}
}
