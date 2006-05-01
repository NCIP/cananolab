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

/* CVS $Id: AbstractBaseAction.java,v 1.6 2006-05-01 19:31:04 pansu Exp $ */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class AbstractBaseAction extends Action {
	private static Logger logger = Logger.getLogger(AbstractBaseAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		try {
			//TODO fill in the common operations */
			if (!loginRequired() ||
				loginRequired() && isUserLoggedIn(request)) {
				forward = executeTask(mapping, form, request, response);	
			}
			else {
				logger.debug("an attempt to access the page without authentication.");
				ActionMessage error=new ActionMessage("error.login.required");
				msgs.add("error", error);
				saveMessages(request, msgs);
				forward = mapping.findForward("login");
			}
		} catch (Throwable t) {
			logger.error("Caught System Exception", t);		
			ActionMessage error=new ActionMessage("error.system", t.getMessage());
			msgs.add("error", error);
			saveMessages(request, msgs);
			forward = mapping.findForward("error");
		}
		return forward;
	}

	/**
	 * 
	 * @param request
	 * @return whether the user is successfully logged in.
	 */
	private boolean isUserLoggedIn(HttpServletRequest request) {
		boolean isLoggedIn=false;
		if (request.getSession().getAttribute("user")!=null) {
			isLoggedIn=true;
		}			
		return isLoggedIn;
	}
	
	/**
	 * Provide implementation of this abstract method in each subclass.
	 */
	public abstract ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * Provide implementation of the abstract method in each subclass.
	 */
	public abstract boolean loginRequired();
}