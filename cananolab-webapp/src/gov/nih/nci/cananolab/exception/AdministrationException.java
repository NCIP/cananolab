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
public class AdministrationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public AdministrationException() {
		super("Exception working with admin related services");
	}

	public AdministrationException(String message) {
		super(message);
	}

	public AdministrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdministrationException(Throwable cause) {
		super(cause);
	}
}
