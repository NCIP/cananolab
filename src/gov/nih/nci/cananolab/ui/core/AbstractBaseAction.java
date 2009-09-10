/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

/**
 * This abstract class is the basis for all other action classes to extend in
 * caLAB. It includes common operations need to be included in all caLAB actions
 * to reduce redundancy.
 * 
 * @author pansu
 */

/* CVS $Id: AbstractBaseAction.java,v 1.1 2008-04-07 20:11:53 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractBaseAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		if (!loginRequired()) {
			return executeTask(mapping, form, request, response);
		}
		if (user != null) {
			boolean accessStatus = canUserExecute(user);
			if (!accessStatus) {
				throw new NoAccessException();
			}
		} else {
			throw new InvalidSessionException();
		}
		return executeTask(mapping, form, request, response);
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

	/**
	 * Check whether the current user in the session can perform the action
	 * 
	 * @param session
	 * @return
	 * @throws SecurityException
	 */
	public abstract boolean canUserExecute(UserBean user)
			throws SecurityException;
}