package gov.nih.nci.cananolab.exception;

public class CustomSearchException extends BaseException{
	private static final long serialVersionUID = 1234567890L;

	public CustomSearchException() {
		super("Exception working with particles custom search");
	}

	public CustomSearchException(String message) {
		super(message);
	}

	public CustomSearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomSearchException(Throwable cause) {
		super(cause);
	}
}
