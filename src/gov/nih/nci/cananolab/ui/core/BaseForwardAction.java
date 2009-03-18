/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

/**
 * This class calls the Struts ForwardAction to forward to a page, aslo extends
 * AbstractBaseAction to inherit the user authentication features.
 * 
 * @author pansu
 */

/* CVS $Id: BaseForwardAction.java,v 1.5 2008-09-23 21:53:24 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

public class BaseForwardAction extends ForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		AuthorizationService authorizationService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		Boolean createProtocol = false;
		Boolean createPublication = false;
		Boolean createParticle = false;
		if (user != null) {
			createProtocol = authorizationService.checkCreatePermission(user,
					Constants.CSM_PG_PROTOCOL);
			createPublication = authorizationService.checkCreatePermission(
					user, Constants.CSM_PG_PUBLICATION);
			createParticle = authorizationService.checkCreatePermission(user,
					Constants.CSM_PG_PARTICLE);
		}
		session.setAttribute("canCreateProtocol", createProtocol);
		session.setAttribute("canCreatePublication", createPublication);
		session.setAttribute("canCreateNanoparticle", createParticle);
		return super.execute(mapping, form, request, response);
	}
}
