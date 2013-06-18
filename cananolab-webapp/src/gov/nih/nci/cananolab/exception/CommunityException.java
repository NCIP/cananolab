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
public class CommunityException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public CommunityException() {
		super("Exception working with community related services");
	}

	public CommunityException(String message) {
		super(message);
	}

	public CommunityException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunityException(Throwable cause) {
		super(cause);
	}
}
