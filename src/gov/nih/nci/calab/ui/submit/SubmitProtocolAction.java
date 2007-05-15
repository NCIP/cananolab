package gov.nih.nci.calab.ui.submit;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: SubmitProtocolAction.java,v 1.4 2007-05-15 18:05:24 chenhang Exp $ */

import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.service.submit.SubmitProtocolService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class SubmitProtocolAction extends AbstractDispatchAction {

	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String protocolName = (String)theForm.get("protocolName");
		String protocolType = (String)theForm.get("protocolType");
		ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");
		String version = fileBean.getVersion();
		
		//Check for name and version
		if (isNameVersionConflict(request, protocolName, version)){
			updateEditableDropDownList(request, theForm);
			ActionMessage msg = new ActionMessage("message.submitProtocol.nameVersionConflict");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			return mapping.findForward("setup");	
		}
		
		ProtocolBean pBean = new ProtocolBean();
		
		pBean.setName(protocolName);
		pBean.setType(protocolType);
		
		
		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		fileBean.setProtocolBean(pBean);
		SubmitProtocolService service = new SubmitProtocolService();

		service.createProtocol(fileBean, uploadedFile);

		if (fileBean.getVisibilityGroups().length == 0) {
			fileBean
					.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
		}
		request.getSession().removeAttribute("AllProtocolNameVersions");
		ActionMessage msg1 = new ActionMessage("message.submitProtocol.secure",
				StringUtils.join(fileBean.getVisibilityGroups(), ", "));
		ActionMessage msg2 = new ActionMessage("message.submitProtocol.file",
				uploadedFile.getFileName());
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);

		request.getSession().setAttribute("newReportCreated", "true");
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setProtocolSubmitPage(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);
		ActionForward forward = mapping.findForward("setup");
		return forward;
	}
	
	public ActionForward reSetup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		updateEditableDropDownList(request, theForm);
		return mapping.findForward("setup");
	}

	public boolean loginRequired() {
		return true;
	}
	private void updateEditableDropDownList(HttpServletRequest request,
			DynaValidatorForm theForm) {
		HttpSession session = request.getSession();
		// update sample source drop-down list to include the new entry
		String protocolType = (String) theForm.get("protocolType");
		List<String> protocolTypes = (List) session.getAttribute("protocolTypes");

		String protocolName = (String) theForm.get("protocolName");
		List<String> protocolNames = (List) session.getAttribute("protocolNames");
		if (protocolNames == null){
			protocolNames = new ArrayList<String>();
		}
		else
			protocolNames.clear();
		
		if (protocolType != null && protocolType.length()>0 
				&& !protocolTypes.contains(protocolType)) {
			protocolTypes.add(protocolType);
		}
		else if (protocolType != null && protocolType.length()>0 
				&& protocolTypes.contains(protocolType)){
			Map<String, List<String>> map = (Map)session.getAttribute("AllProtocolNameTypes");
			List<String> list = map.get(protocolType);
			protocolNames.addAll(list);
		}
			
		if (protocolName != null && protocolName.length()>0 
				&& !protocolNames.contains(protocolName)) {
			protocolNames.add(protocolName);
		}
	}

	private boolean isNameVersionConflict(HttpServletRequest request, 
		String protocolName, String version) throws Exception{
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllProtocolNamesWithVersion(session);
		Map<String, List<String>> map = (Map)request.getSession().getAttribute("AllProtocolNamesWithVersions");
		if (!map.containsKey(protocolName))
			return false;
		
		List<String> list = map.get(protocolName);
		if (!list.contains(version))
			return false;
		return true;
	}
}
