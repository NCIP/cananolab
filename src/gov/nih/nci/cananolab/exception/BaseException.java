/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.exception;

/**
 * @author zengje
 * 
 */

public class BaseException extends Exception {

	private static final long serialVersionUID = 1234567890L;

	/**
	 * 
	 */
	public BaseException() {
		super("An exception occurred in caNanoLab");
	}

	public BaseException(String message) {
		super(message);

	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);

	}

	public BaseException(Throwable cause) {
		super(cause);
	}
}
