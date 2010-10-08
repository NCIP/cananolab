/**
 * The caNanoLab Software License, Version 1.5
 *
 * Copyright 2006 SAIC. This software was developed in conjunction with the National
 * Cancer Institute, and so to the extent government employees are co-authors, any
 * rights in such works shall be subject to Title 17 of the United States Code,
 * section 105.
 *
 */
package gov.nih.nci.cananolab.ui.admin;

/**
 * Action class for Transfer Ownership section.
 *
 * @author lethai, pansu
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.admin.impl.OwnershipTransferServiceImpl;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class TransferOwnerAction extends AbstractDispatchAction {
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("input");
	}

	public ActionForward transfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SecurityService securityService = getSecurityService(request);
		String currentOwner = (String) theForm.get("currentOwner");
		String newOwner = (String) theForm.get("newOwner");

		// String[] dataTypes = new String[0];
		// if (theForm != null) {
		// dataTypes = (String[]) theForm
		// .get("dataType");
		//
		// }

		String dataType = theForm.getString("dataType");
		String[] dataTypes = new String[] { dataType };

		OwnershipTransferService transferService = new OwnershipTransferServiceImpl();
		List<String> dataIds = null;
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i]
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_SAMPLE)) {
				SampleService sampleService = new SampleServiceLocalImpl(
						securityService);
				dataIds = sampleService.findSampleIdsByOwner(currentOwner);
				transferService.transferOwner(sampleService, dataIds,
						currentOwner, newOwner);
			} else if (dataTypes[i]
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PROTOCOL)) {
				ProtocolService protocolService = new ProtocolServiceLocalImpl(
						securityService);
				dataIds = protocolService.findProtocolIdsByOwner(currentOwner);
				transferService.transferOwner(protocolService, dataIds,
						currentOwner, newOwner);
			} else if (dataTypes[i]
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PUBLICATION)) {
				PublicationService publicationService = new PublicationServiceLocalImpl(
						securityService);
				dataIds = publicationService
						.findPublicationIdsByOwner(currentOwner);
				transferService.transferOwner(publicationService, dataIds,
						currentOwner, newOwner);
			} else if (dataTypes[i]
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_GROUP)) {
				CommunityService communityService = new CommunityServiceLocalImpl(
						securityService);
				dataIds = communityService
						.findCollaborationGroupIdsByOwner(currentOwner);
				transferService.transferOwner(communityService, dataIds,
						currentOwner, newOwner);
			}
		}

		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("message.transferOwner");
		messages.add("message", message);
		saveMessages(request, messages);

		return mapping.findForward("success");
	}

	private SecurityService getSecurityService(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");

		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		return securityService;
	}
}
