package gov.nih.nci.cananolab.ui.community;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CollaborationGroupAction extends AbstractDispatchAction {
	// logger
	// private static Logger logger =
	// Logger.getLogger(ReviewDataAction.class);

	/**
	 * Handle edit sample request on sample search result page (curator view).
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("group", new CollaborationGroupBean());
		request.getSession().removeAttribute("openCollaborationGroup");
		setExistingGroups(request);
		return mapping.findForward("setup");
	}

	private void setExistingGroups(HttpServletRequest request) throws Exception {
		CommunityService service = setServiceInSession(request);
		List<CollaborationGroupBean> existingCollaborationGroups = service
				.findCollaborationGroups();
		request.setAttribute("existingCollaborationGroups",
				existingCollaborationGroups);
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		checkOpenForms(theForm, request);
		setExistingGroups(request);
		return mapping.findForward("setup");
	}

	private void checkOpenForms(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openCollaborationGroup = false;
		if (dispatch.equals("input")) {
			openCollaborationGroup = true;
			session.setAttribute("openCollaborationGroup",
					openCollaborationGroup);
		}
	}

	/**
	 * Save or update collaboration group.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CollaborationGroupBean group = (CollaborationGroupBean) theForm
				.get("group");
		CommunityService service = setServiceInSession(request);
		service.saveCollaborationGroup(group);
		// update user's groupNames
		UserBean user = ((CommunityServiceLocalImpl) service).getUser();
		request.getSession().setAttribute("user", user);
		return setupNew(mapping, form, request, response);
	}

	private CommunityService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		CommunityService service = new CommunityServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("communityService", service);
		return service;
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CollaborationGroupBean group = (CollaborationGroupBean) theForm
				.get("group");
		CommunityService service = setServiceInSession(request);
		service.deleteCollaborationGroup(group);
		return setupNew(mapping, form, request, response);
	}
}
