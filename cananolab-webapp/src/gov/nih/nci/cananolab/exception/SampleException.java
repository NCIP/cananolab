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
public class SampleException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public SampleException() {
		super("Exception working with particles");
	}

	public SampleException(String message) {
		super(message);
	}

	public SampleException(String message, Throwable cause) {
		super(message, cause);
	}

	public SampleException(Throwable cause) {
		super(cause);
	}
}
