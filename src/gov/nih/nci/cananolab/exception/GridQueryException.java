/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.exception;

/**
 * @author pansu
 * 
 */
public class GridQueryException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public GridQueryException() {
		super("Error in query against grid node");
	}

	public GridQueryException(String message) {
		super(message);
	}

	public GridQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public GridQueryException(Throwable cause) {
		super(cause);
	}
}
