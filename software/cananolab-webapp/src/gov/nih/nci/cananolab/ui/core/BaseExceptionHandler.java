/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.core;

/**
 * This base class extends the Struts exceptionHandler and logs the exceptions
 * as errors to a log file
 *
 * @author pansu
 */

import org.apache.log4j.Logger;
import org.apache.struts.action.ExceptionHandler;

public class BaseExceptionHandler extends ExceptionHandler {
	private static Logger logger = Logger.getLogger(BaseExceptionHandler.class);

	// overwrite the default logException method to log exception as errors
	protected void logException(Exception ex) {
		logger.error(ex.getMessage(), ex);
	}
}