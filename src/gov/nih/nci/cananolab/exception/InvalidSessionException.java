/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.exception;

/**
 * This class represents the exception to be thrown when the web session expires
 * or when the user is not logged in.
 * 
 * @author pansu
 * 
 */
public class InvalidSessionException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public InvalidSessionException() {
		super("User is not logged in or session expired");
	}

	public InvalidSessionException(String message) {
		super(message);
	}

	public InvalidSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSessionException(Throwable cause) {
		super(cause);
	}
}
