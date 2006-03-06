/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.calab.ui.core;

/**
 * This abstract class is the basis for all other action classes to extend in
 * caLAB. It includes common operations need to be included in all caLAB actions
 * to reduce redundancy.
 * 
 * @author pansu
 */

/* CVS $Id: */

import org.apache.log4j.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractBaseAction extends Action {
	private static Logger logger = Logger.getLogger(AbstractBaseAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		try {
			/**@todo fill in the common operations */
			forward = executeTask(mapping, form, request, response);
		} catch (Throwable t) {
			logger.error("Caught System Exception", t);
			
			/**@todo add error handling details here */
			forward = mapping.findForward("error");
		}
		return forward;
	}

	/**
	 * Provide implementation of this abstract method in each subclass.
	 */
	public abstract ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}