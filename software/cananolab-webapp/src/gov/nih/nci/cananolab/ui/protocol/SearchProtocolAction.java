package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
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
		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

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

		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		ProtocolService service = new ProtocolServiceLocalImpl(securityService);

		List<ProtocolBean> allProtocols = service.findProtocolsBy(protocolType,
				protocolName, protocolAbbreviation, fileTitle);
		this.loadUserAccess(request, allProtocols);
		if (allProtocols != null && !allProtocols.isEmpty()) {
			request.getSession().setAttribute("protocols", allProtocols);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchProtocol.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}
		return forward;
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
			if (privileges.contains(Constants.CSM_UPDATE_PRIVILEGE)) {
				protocolBean.setUserUpdatable(true);
			} else {
				protocolBean.setUserUpdatable(false);
			}
		}
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setLocalSearchDropdowns(request);
		return mapping.getInputForward();
	}
}
