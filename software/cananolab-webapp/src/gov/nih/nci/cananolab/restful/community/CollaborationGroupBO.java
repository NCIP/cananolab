/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.community;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.restful.bean.SimpleCollaborationGroup;
import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class CollaborationGroupBO  extends AbstractDispatchBO {
	private Logger logger = Logger.getLogger(CollaborationGroupBO.class);

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
//	public void setupNew(HttpServletRequest request)
//			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		theForm.set("group", new CollaborationGroupBean());
//		request.getSession().removeAttribute("openCollaborationGroup");
//		getExistingGroups(request);
//		
//		return mapping.findForward("setup");
//	}

	public List<CollaborationGroupBean> getExistingGroups(HttpServletRequest request) throws Exception {
		CommunityService service = setServiceInSession(request);
		List<CollaborationGroupBean> existingCollaborationGroups = service
				.findCollaborationGroups();
//		request.setAttribute("existingCollaborationGroups",
//				existingCollaborationGroups);
		
		List<SimpleCollaborationGroup> simpleGroups = transferToSimpleCollaborationGroups(existingCollaborationGroups);
		
		return existingCollaborationGroups;
	}
	
	protected List<SimpleCollaborationGroup> transferToSimpleCollaborationGroups(
			List<CollaborationGroupBean> existingCollaborationGroups)
			throws Exception {
		List<SimpleCollaborationGroup> simpleGroups = new ArrayList<SimpleCollaborationGroup>();
		
		if (existingCollaborationGroups == null) return simpleGroups;
		
		for (CollaborationGroupBean group : existingCollaborationGroups) {
			SimpleCollaborationGroup simpleGroup = new SimpleCollaborationGroup();
			simpleGroup.transferFromCollaborationGroupBean(group);
			simpleGroups.add(simpleGroup);
		}
		
		return simpleGroups;
			
		
	}
	

//	public ActionForward input(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		checkOpenForms(theForm, request);
//		setExistingGroups(request);
//		return mapping.findForward("setup");
//	}

//	private void checkOpenForms(DynaValidatorForm theForm,
//			HttpServletRequest request) throws Exception {
//		String dispatch = request.getParameter("dispatch");
//		String browserDispatch = getBrowserDispatch(request);
//		HttpSession session = request.getSession();
//		Boolean openCollaborationGroup = false;
//		if (dispatch.equals("input")) {
//			openCollaborationGroup = true;
//			session.setAttribute("openCollaborationGroup",
//					openCollaborationGroup);
//		}
//	}

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
//	public ActionForward create(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		if (!validateToken(request)) {
//			return input(mapping, form, request, response);
//		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		CollaborationGroupBean group = (CollaborationGroupBean) theForm
//				.get("group");
//		//double check the groupName for invalid special characters
//		if (!StringUtils.xssValidate(group.getName())) {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage error = new ActionMessage("group.name.invalid");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, error);
//			saveErrors(request, msgs);
//			return mapping.findForward("input");
//		}
//		CommunityService service = setServiceInSession(request);
//		service.saveCollaborationGroup(group);
//		// update user's groupNames
//		UserBean user = ((CommunityServiceLocalImpl) service).getUser();
//		request.getSession().setAttribute("user", user);
//		resetToken(request);
//		return setupNew(mapping, form, request, response);
//	}

	private CommunityService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		CommunityService service = new CommunityServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("communityService", service);
		return service;
	}

//	public ActionForward delete(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		if (!validateToken(request)) {
//			return input(mapping, form, request, response);
//		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		CollaborationGroupBean group = (CollaborationGroupBean) theForm
//				.get("group");
//		CommunityService service = setServiceInSession(request);
//		service.deleteCollaborationGroup(group);
//		resetToken(request);
//		return setupNew(mapping, form, request, response);
//	}
}
