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
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.StringUtils;

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
	public List<CollaborationGroupBean> setupNew(HttpServletRequest request)
			throws Exception {
		//theForm.set("group", new CollaborationGroupBean());
		request.getSession().removeAttribute("openCollaborationGroup");
		List<CollaborationGroupBean> beans = getExistingGroups(request);
		
		return beans;
	}

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
	public List<CollaborationGroupBean> create(CollaborationGroupBean group,
			HttpServletRequest request)
			throws Exception {
		
//		CollaborationGroupBean group = (CollaborationGroupBean) theForm
//				.get("group");
		//double check the groupName for invalid special characters
		if (!StringUtils.xssValidate(group.getName())) {
			//ActionMessage error = new ActionMessage("group.name.invalid");
//			msgs.add(PropertyUtil.getProperty("community.properties", "group.name.invalid"));
//			//saveErrors(request, msgs);
//			return msgs;
		}
		CommunityService service = setServiceInSession(request);
		service.saveCollaborationGroup(group);
		// update user's groupNames
		UserBean user = ((CommunityServiceLocalImpl) service).getUser();
		request.getSession().setAttribute("user", user);

		return setupNew(request);
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

	public List<CollaborationGroupBean> delete(CollaborationGroupBean group,
			HttpServletRequest request)
			throws Exception {
		
//		CollaborationGroupBean group = (CollaborationGroupBean) theForm
//				.get("group");
		CommunityService service = setServiceInSession(request);
		service.deleteCollaborationGroup(group);
//		resetToken(request);
		return setupNew(request);
	}
}
