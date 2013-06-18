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
public class CurationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CurationException() {
		super("Exception working with curation related services");
	}

	public CurationException(String message) {
		super(message);
	}

	public CurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CurationException(Throwable cause) {
		super(cause);
	}
}
