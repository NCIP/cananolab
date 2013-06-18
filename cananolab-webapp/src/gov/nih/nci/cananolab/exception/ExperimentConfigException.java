/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.exception;

/**
 *
 * @author pansu
 *
 */

public class ExperimentConfigException extends BaseException {

	private static final long serialVersionUID = 1234567890L;

	public ExperimentConfigException() {
		super("Exception occurred working with Instruments and Techniques");
	}

	public ExperimentConfigException(String message) {
		super(message);
	}

	public ExperimentConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExperimentConfigException(Throwable cause) {
		super(cause);
	}
}
