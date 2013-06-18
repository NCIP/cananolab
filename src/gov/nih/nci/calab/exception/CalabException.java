/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.exception;

/**
 * @author zengje
 *
 */

public class CalabException extends Exception {

	private static final long serialVersionUID = 1234567890L;
 
	/**
	 * 
	 */
	public CalabException() {
		super("An exception ocurred in caLAB");
	}

	public CalabException(String message) {
		super(message);

	}

	public CalabException(String message, Throwable cause) {
		super(message, cause);

	}

	public CalabException(Throwable cause) {
		super(cause);

	}

}
