/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.community;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
//import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu
 *
 */
public class CollaborationGroupManager {
	private CommunityService service;
	private Logger logger = Logger.getLogger(CollaborationGroupManager.class);
	private SecurityService securityService;

	private CommunityService getService(HttpServletRequest request) throws Exception {
		securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		service = new CommunityServiceLocalImpl(securityService);
		return service;
	}

//	public CollaborationGroupBean resetTheCollaborationGroup() {
//		DynaValidatorForm collaborationGroupForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("collaborationGroupForm"));
//		if (collaborationGroupForm == null) {
//			return null;
//		}
//		CollaborationGroupBean group = new CollaborationGroupBean();
//		collaborationGroupForm.set("group", group);
//		return group;
//	}
//
	public CollaborationGroupBean getCollaborationGroupById(HttpServletRequest request, String id)
			throws Exception {
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		CollaborationGroupBean group = getService(request).findCollaborationGroupById(
				id);
		request.getSession().setAttribute("group", group);
		return group;
	}

	public CollaborationGroupBean addUserAccess(HttpServletRequest request, AccessibilityBean userAccess)
			throws Exception {

		CollaborationGroupBean group = (CollaborationGroupBean) (request.getSession().getAttribute("group"));
		// check whether user is a valid user
		getService(request);
		String userLogin = userAccess.getUserBean().getLoginName();
		CollaborationGroupBean bogusGroup=new CollaborationGroupBean();
		if (!securityService.isUserValid(userLogin)) {
			bogusGroup.setName("!!invalid user");
			return bogusGroup;
		}
		// if the user is already a curator, don't add the user
		else if (securityService.isCurator(userLogin)) {
			bogusGroup.setName("!!user is a curator");
			return bogusGroup;
		}
		group.addUserAccess(userAccess);
		return group;
	}

	public CollaborationGroupBean deleteUserAccess(HttpServletRequest request, AccessibilityBean userAccess)
			throws Exception {
		CollaborationGroupBean group = (CollaborationGroupBean) (request.getSession().getAttribute("group"));
		group.removeUserAccess(userAccess);
		return group;
	}

//	public UserBean[] getMatchedUsers(String groupOwner, String searchStr)
//			throws Exception {
//		try {
//			List<UserBean> matchedUsers = getService(request).findUserBeans(searchStr);
//			List<UserBean> updatedUsers = new ArrayList<UserBean>(matchedUsers);
//			// remove current user from the list
//			WebContext wctx = WebContextFactory.get();
//			UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//			updatedUsers.remove(user);
//			// remove data owner from the list if owner is not the current user
//			if (!groupOwner.equalsIgnoreCase(user.getLoginName())) {
//				for (UserBean userBean : matchedUsers) {
//					if (userBean.getLoginName().equalsIgnoreCase(groupOwner)) {
//						updatedUsers.remove(userBean);
//						break;
//					}
//				}
//			}
//			// exclude curators;
//			List<String> curators = securityService
//					.getUserNames(AccessibilityBean.CSM_DATA_CURATOR);
//			for (UserBean userBean : matchedUsers) {
//				for (String curator : curators) {
//					if (userBean.getLoginName().equalsIgnoreCase(curator)) {
//						updatedUsers.remove(userBean);
//					}
//				}
//			}
//			return updatedUsers.toArray(new UserBean[updatedUsers.size()]);
//		} catch (Exception e) {
//			logger.error("Problem getting matched user login names", e);
//			return null;
//		}
//	}
}
