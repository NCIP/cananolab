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
public class CompositionException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CompositionException() {
		super("Exception occurred working with sample composition.");
	}

	public CompositionException(String message) {
		super(message);
	}

	public CompositionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompositionException(Throwable cause) {
		super(cause);
	}
}
