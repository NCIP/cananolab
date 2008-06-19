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

/* CVS $Id: WelcomeAction.java,v 1.5 2008-06-19 20:19:06 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.GridService;
import gov.nih.nci.cananolab.ui.core.AbstractBaseAction;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.ForwardAction;

public class WelcomeAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request); // save token to avoid back and refresh on the login
		// page.
		String refreshGrid = request.getParameter("refreshGrid");

		if (refreshGrid != null && refreshGrid.equals("true")) {
			// auto-discover grid nodes and save in session
			Map<String, GridNodeBean> gridNodeMap = GridService
					.discoverServices(
							CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
							CaNanoLabConstants.DOMAIN_MODEL_NAME,
							CaNanoLabConstants.APP_OWNER);
			if (gridNodeMap == null) {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.grid.discovery.none",
						CaNanoLabConstants.DOMAIN_MODEL_NAME);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
			}
			request.getSession().getServletContext().setAttribute(
					"allGridNodes", gridNodeMap);
		}
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
