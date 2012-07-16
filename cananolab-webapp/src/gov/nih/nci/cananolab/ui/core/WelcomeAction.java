/*
 The caNanoLab Software License, Version 1.4

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class calls the Struts ForwardAction to forward to a page without login
 * 
 * @author pansu
 */

public class WelcomeAction extends AbstractForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//set public data counts in the context
		InitSetup.getInstance().setPublicCountInContext(
				request.getSession().getServletContext());
		//save the token
		saveToken(request);
		return super.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return false;
	}
}
