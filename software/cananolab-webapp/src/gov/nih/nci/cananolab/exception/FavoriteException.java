package gov.nih.nci.cananolab.exception;

public class FavoriteException extends BaseException{

	private static final long serialVersionUID = 1234567890L;

	public FavoriteException() {
		super("Exception occurred working with my favourites");
	}

	public FavoriteException(String message) {
		super(message);
	}

	public FavoriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public FavoriteException(Throwable cause) {
		super(cause);
	}
}
