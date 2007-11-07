package gov.nih.nci.calab.ui.protocol;

/**
 * This class uploads a protocol file and assigns visibility  
 *  
 * @author chenhang
 */

/* CVS $Id: SubmitProtocolAction.java,v 1.2 2007-11-07 21:22:20 pansu Exp $ */

import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;
import gov.nih.nci.calab.service.protocol.SubmitProtocolService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

public class SubmitProtocolAction extends AbstractDispatchAction {

	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String protocolName = (String) theForm.get("protocolName");
		String protocolType = (String) theForm.get("protocolType");
		ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");
		// String version = fileBean.getId();

		ProtocolBean pBean = new ProtocolBean();

		pBean.setId(protocolName);
		pBean.setType(protocolType);

		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		fileBean.setProtocolBean(pBean);
		SubmitProtocolService service = new SubmitProtocolService();

		service.createProtocol(fileBean, uploadedFile);

		if (fileBean.getVisibilityGroups().length == 0) {
			fileBean.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
		}
		ActionMessage msg1 = new ActionMessage("message.submitProtocol.secure",
				StringUtils.join(fileBean.getVisibilityGroups(), ", "));
		ActionMessage msg2 = new ActionMessage("message.submitProtocol.file",
				uploadedFile.getFileName());
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);

		request.getSession().setAttribute("newProtocolCreated", "true");
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitProtocolSetup.getInstance().setAllProtocolTypes(session);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		ActionForward forward = mapping.findForward("setup");
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = request.getParameter("fileId");
		if (fileId == null)
			fileId = (String) request.getAttribute("fileId");
		SearchProtocolService service = new SearchProtocolService();
		ProtocolFileBean fileBean = service.getProtocolFileBean(fileId);
		theForm.set("file", fileBean);
		theForm.set("protocolName", fileBean.getProtocolBean().getName());
		theForm.set("protocolType", fileBean.getProtocolBean().getType());
		request.setAttribute("filename", fileBean.getName());
		return mapping.findForward("setup");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");

		// FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");

		SubmitProtocolService service = new SubmitProtocolService();

		try {
			service.updateProtocol(fileBean, null);

			if (fileBean.getVisibilityGroups().length == 0) {
				fileBean.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
			}
		} catch (Exception e) {
			ActionMessage msg = new ActionMessage("error.updateProtocol", e
					.getMessage());
			msgs.add("message", msg);
			saveMessages(request, msgs);
			request.setAttribute("fileId", fileBean.getId());
			return setupUpdate(mapping, form, request, response);
		}

		ActionMessage msg = new ActionMessage("message.updateProtocol",
				fileBean.getTitle());

		msgs.add("message", msg);
		saveMessages(request, msgs);

		// request.getSession().setAttribute("newProtocolCreated", "true");

		return mapping.findForward("success");
	}

	public ActionForward reSetup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		//updateEditableDropDownList(request, theForm);
		return mapping.findForward("setup");
	}

	public boolean loginRequired() {
		return true;
	}

	private void updateEditableDropDownList(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		String protocolType = (String) theForm.get("protocolType");
		String protocolName = (String) theForm.get("protocolName");
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				protocolType, "protocolTypes");

		InitSessionSetup.getInstance().updateEditableDropdown(session,
				protocolName, "protocolNames");
		/*
		 * HttpSession session = request.getSession(); // update sample source
		 * drop-down list to include the new entry String protocolType =
		 * (String) theForm.get("protocolType"); SortedSet<String>
		 * protocolTypes = (SortedSet) session.getAttribute("protocolTypes");
		 * String protocolName = (String) theForm.get("protocolId");
		 * ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");
		 * String fileId = fileBean.getId();
		 * 
		 * //Update protocolType list first. //If user entered a brand new
		 * protocol type, then added to the protocol type list boolean isNewType =
		 * false; if (protocolType != null && protocolType.length()>0 &&
		 * !protocolTypes.contains(protocolType)) {
		 * protocolTypes.add(protocolType); isNewType = true; } //otherwise
		 * don't do anything
		 * 
		 * //Update protocol name list. boolean isNewName = false; Map<String,
		 * List<String>> typeNamesMap =
		 * (Map)session.getAttribute("AllProtocolTypeNames"); Map<String, List<String>>
		 * typeIdsMap = (Map)session.getAttribute("AllProtocolTypeIds"); if
		 * (protocolName != null && protocolName.length()>0 ){ //if it's a new
		 * protocol type, we need to add this protocol name to the above two
		 * maps if (isNewType){ List<String> newNameList = new ArrayList<String>();
		 * List<String> newIdList = new ArrayList<String>();
		 * newNameList.add(protocolName); newIdList.add(protocolName);
		 * typeNamesMap.put(protocolType, newNameList);
		 * typeIdsMap.put(protocolType, newIdList);
		 * 
		 * //always a new name isNewName = true; } //if an old type. if this is
		 * a new name then added to the list else{ List<String> newNameList =
		 * typeNamesMap.get(protocolType); List<String> newIdList =
		 * typeIdsMap.get(protocolType); //A new name if (newNameList == null)
		 * isNewName = true; else if (newNameList!= null &&
		 * !newIdList.contains(protocolName)){ newNameList.add(protocolName);
		 * newIdList.add(protocolName); isNewName = true; } } } //Else don't do
		 * anything. //Now set the name dropdown box SortedSet<ProtocolBean>
		 * protocols = new TreeSet<ProtocolBean>(); if (protocolType != null &&
		 * protocolType.length()>0){ List<String> protocolNames =
		 * typeNamesMap.get(protocolType); List<String> protocolIds =
		 * typeIdsMap.get(protocolType); if (protocolNames != null &&
		 * !protocolNames.isEmpty()){ int i = 0; for (String id : protocolIds){
		 * ProtocolBean pb = new ProtocolBean(); pb.setId(id);
		 * pb.setName(protocolNames.get(i)); protocols.add(pb); i++; } } //A
		 * protocol type with no protocol created else if (protocolName != null &&
		 * protocolName.length()>0){ ProtocolBean pb = new ProtocolBean();
		 * pb.setId(protocolName); pb.setName(protocolName); protocols.add(pb); } }
		 * else{ if (protocolName != null && protocolName.length()>0 ){
		 * ProtocolBean pb = new ProtocolBean(); pb.setId(protocolName);
		 * pb.setName(protocolName); protocols.add(pb); } }
		 * session.setAttribute("protocolNames", protocols);
		 * 
		 * //Now deal with the version dropdown box boolean isNewVersion =
		 * false; Map<String, List<String>> nameVersionsMap =
		 * (Map)session.getAttribute("AllProtocolNameVersions"); Map<String,
		 * List<String>> nameIdsMap =
		 * (Map)session.getAttribute("AllProtocolNameFileIds"); if (fileId !=
		 * null && fileId.length()>0 ){ //if it's a new protocol name, we need
		 * to add this protocol //version to the above two maps if (isNewName){
		 * List<String> newVersionList = new ArrayList<String>(); List<String>
		 * newVersionIdList = new ArrayList<String>();
		 * newVersionList.add(fileId); newVersionIdList.add(fileId);
		 * nameVersionsMap.put(protocolName, newVersionList);
		 * nameIdsMap.put(protocolName, newVersionIdList); } //if an old name.
		 * if this is a new version then added to the list else{ //must check
		 * the name field is not empty if (protocolName != null &&
		 * protocolName.length()>0){ List<String> newVersionList =
		 * nameVersionsMap.get(protocolName); List<String> newVersionIdList =
		 * nameIdsMap.get(protocolName); if (newVersionIdList!= null &&
		 * !newVersionIdList.contains(fileId)){ newVersionList.add(fileId);
		 * newVersionIdList.add(fileId); } } //else then there is no
		 * correlation, but we still add it to the //dropdown box. else {
		 * isNewVersion = true; } } } //Let build version dropdown box SortedSet<LabelValueBean>
		 * myProtocolFiles = new TreeSet<LabelValueBean>(); if (isNewVersion){
		 * LabelValueBean pfb = new LabelValueBean(fileId, fileId);
		 * myProtocolFiles.add(pfb); } else if (protocolName != null &&
		 * protocolName.length()>0){ List<String> fileVersions =
		 * nameVersionsMap.get(protocolName); List<String> fileIds =
		 * nameIdsMap.get(protocolName); if (fileVersions != null &&
		 * !fileVersions.isEmpty()){ int i = 0;
		 * 
		 * for (String version : fileVersions){ LabelValueBean pfb = new
		 * LabelValueBean(version, fileIds.get(i)); //pfb.setVersion(version);
		 * //pfb.setId(fileIds.get(i)); myProtocolFiles.add(pfb); i++; } } }
		 * session.setAttribute("protocolVersions", myProtocolFiles);
		 */
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PROTOCOL);
	}
}
