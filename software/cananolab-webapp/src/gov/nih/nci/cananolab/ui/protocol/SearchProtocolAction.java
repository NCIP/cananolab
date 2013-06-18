/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Search protocol file and protocol
 * 
 * @author pansu
 * 
 */
public class SearchProtocolAction extends BaseAnnotationAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);
		List<ProtocolBean> protocolBeans = null;
		// retrieve from session if it's not null and not the first page
		if (session.getAttribute("protocolSearchResults") != null
				&& displayPage > 0) {
			protocolBeans = new ArrayList<ProtocolBean>(
					(List) session.getAttribute("protocolSearchResults"));
		} else {
			protocolBeans = queryProtocols(form, request);
			if (protocolBeans != null && !protocolBeans.isEmpty()) {
				session.setAttribute("protocolSearchResults", protocolBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.searchProtocol.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		// display 25 protocols at a time
		List<ProtocolBean> protocolBeansPerPage = getProtocolsPerPage(
				protocolBeans, displayPage, Constants.DISPLAY_TAG_TABLE_SIZE,
				request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user != null) {
			loadUserAccess(request, protocolBeansPerPage);
		}
		// set in sessionScope so user can go back to the result from the sample
		// summary page
		request.getSession().setAttribute("protocols", protocolBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		// set in sessionScope so user can go back to the result from the sample
		// summary page
		request.getSession().setAttribute("resultSize",
				new Integer(protocolBeans.size()));

		return mapping.findForward("success");
	}

	private List<ProtocolBean> getProtocolsPerPage(
			List<ProtocolBean> protocolBeans, int page, int pageSize,
			HttpServletRequest request) throws Exception {
		List<ProtocolBean> protocolsPerPage = new ArrayList<ProtocolBean>();
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < protocolBeans.size()) {
				ProtocolBean protocolBean = protocolBeans.get(i);
				if (protocolBean != null) {
					protocolsPerPage.add(protocolBean);
				}
			}
		}
		return protocolsPerPage;
	}

	private List<ProtocolBean> queryProtocols(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileTitle = (String) theForm.get("fileTitle");
		// strip wildcards if entered by user
		fileTitle = StringUtils.stripWildcards(fileTitle);
		String titleOperand = (String) theForm.get("titleOperand");
		if (titleOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(fileTitle)) {
			fileTitle = "*" + fileTitle + "*";
		}
		String protocolType = (String) theForm.get("protocolType");
		String protocolName = (String) theForm.get("protocolName");
		// strip wildcards if entered by user
		protocolName = StringUtils.stripWildcards(protocolName);

		String nameOperand = (String) theForm.get("nameOperand");
		if (nameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(protocolName)) {
			protocolName = "*" + protocolName + "*";
		}
		String protocolAbbreviation = (String) theForm
				.get("protocolAbbreviation");
		// strip wildcards if entered by user
		protocolAbbreviation = StringUtils.stripWildcards(protocolAbbreviation);

		String abbreviationOperand = (String) theForm
				.get("abbreviationOperand");
		if (abbreviationOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(protocolAbbreviation)) {
			protocolAbbreviation = "*" + protocolAbbreviation + "*";
		}

		ProtocolService service = this.setServiceInSession(request);
		List<ProtocolBean> allProtocols = service.findProtocolsBy(protocolType,
				protocolName, protocolAbbreviation, fileTitle);
		return allProtocols;
	}

	private void loadUserAccess(HttpServletRequest request,
			List<ProtocolBean> protocolBeans) throws Exception {
		List<String> protocolIds = new ArrayList<String>();
		for (ProtocolBean protocolBean : protocolBeans) {
			protocolIds.add(protocolBean.getDomain().getId().toString());
		}
		SecurityService securityService = getSecurityServiceFromSession(request);
		Map<String, List<String>> privilegeMap = securityService
				.getPrivilegeMap(protocolIds);
		for (ProtocolBean protocolBean : protocolBeans) {
			List<String> privileges = privilegeMap.get(protocolBean.getDomain()
					.getId().toString());
			if (privileges.contains(AccessibilityBean.CSM_UPDATE_PRIVILEGE)) {
				protocolBean.setUserUpdatable(true);
			} else {
				protocolBean.setUserUpdatable(false);
			}
			if (privileges.contains(AccessibilityBean.CSM_DELETE_PRIVILEGE)) {
				protocolBean.setUserDeletable(true);
			} else {
				protocolBean.setUserDeletable(false);
			}
		}
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setLocalSearchDropdowns(request);
		return mapping.getInputForward();
	}

	private ProtocolService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		ProtocolService protocolService = new ProtocolServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("protocolService", protocolService);
		return protocolService;
	}
}
