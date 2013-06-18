/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.exception;

/**
 * @author tanq
 * 
 */
public class PointOfContactException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public PointOfContactException() {
		super("Exception occurred working with PointOfContacts");
	}

	public PointOfContactException(String message) {
		super(message);
	}

	public PointOfContactException(String message, Throwable cause) {
		super(message, cause);
	}

	public PointOfContactException(Throwable cause) {
		super(cause);
	}
}
