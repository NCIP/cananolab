/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.calab.ui.core;

/**
 * This class calls the Struts ForwardAction to forward to a page, aslo 
 * extends AbstractBaseAction to inherit the user authentication features.
 *  
 * @author pansu
 */

/* CVS $Id: BaseForwardAction.java,v 1.4 2007-10-25 20:56:51 cais Exp $ */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

public class BaseForwardAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ForwardAction forwardAction = new ForwardAction();
		return forwardAction.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		//return true;
		return false;
	}
	
	public boolean canUserExecute(HttpSession session) {
		return true;
	}
}
