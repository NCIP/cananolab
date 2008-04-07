/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.cananolab.ui.security;

/**
 * This class calls the Struts ForwardAction to forward to a page, aslo extends
 * AbstractBaseAction to inherit the user authentication features.
 * 
 * @author pansu
 */

/* CVS $Id: WelcomeAction.java,v 1.1 2008-04-07 20:12:28 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

public class WelcomeAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request); //save token to avoid back and refresh on the login page.		
		ForwardAction forwardAction = new ForwardAction();		
		return forwardAction.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}
}
