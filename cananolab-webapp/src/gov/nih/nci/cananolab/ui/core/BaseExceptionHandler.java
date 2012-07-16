/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

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