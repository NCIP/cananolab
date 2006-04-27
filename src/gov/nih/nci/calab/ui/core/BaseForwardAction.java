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

/* CVS $Id: BaseForwardAction.java,v 1.1 2006-04-27 17:05:41 pansu Exp $ */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		return true;
	}
}

// public class BaseForwardAction extends ForwardAction {
// private static Logger logger = Logger.getLogger(BaseForwardAction.class);
//	
// public ActionForward execute(ActionMapping mapping, ActionForm form,
// HttpServletRequest request, HttpServletResponse response)
// throws Exception {
//		
// ActionForward forward = null;
// ActionMessages msgs = new ActionMessages();
//
// try {
// //TODO fill in the common operations */
// if (!loginRequired() ||
// loginRequired() && isUserLoggedIn(request)) {
// forward=super.execute(mapping, form, request, response);
// }
// else {
// logger.debug("an attempt to access the page without authentication.");
// ActionMessage error=new ActionMessage("error.login.required");
// msgs.add("error", error);
// saveMessages(request, msgs);
// forward = mapping.findForward("login");
// }
// } catch (Throwable t) {
// logger.error("Caught System Exception", t);
// ActionMessage error=new ActionMessage("error.system");
// msgs.add("error", error);
// saveMessages(request, msgs);
// forward = mapping.findForward("error");
// }
// return forward;
// }
//	
// /**
// *
// * @param request
// * @return whether the user is successfully logged in.
// */
// private boolean isUserLoggedIn(HttpServletRequest request) {
// boolean isLoggedIn=false;
// if (request.getSession().getAttribute("user")!=null) {
// isLoggedIn=true;
// }
// return isLoggedIn;
// }
//	
// public boolean loginRequired() {
// return true;
// }
// }
