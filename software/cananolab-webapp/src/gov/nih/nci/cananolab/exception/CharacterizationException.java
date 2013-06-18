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
public class CharacterizationException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CharacterizationException() {
		super("Exception occurred working with particle characterizations");
	}

	public CharacterizationException(String message) {
		super(message);
	}

	public CharacterizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CharacterizationException(Throwable cause) {
		super(cause);
	}
}
