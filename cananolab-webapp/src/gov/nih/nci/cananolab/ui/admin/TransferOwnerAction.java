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
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.admin.impl.BatchOwnershipTransferProcess;
import gov.nih.nci.cananolab.service.admin.impl.OwnershipTransferServiceImpl;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class TransferOwnerAction extends AbstractDispatchAction {
	private static final int CUT_OFF_NUM_DATA = 30;

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
		ActionMessages messages = new ActionMessages();
		// validate owner names
		if (!securityService.isUserValid(currentOwner)) {
			ActionMessage message = new ActionMessage(
					"message.transferOwner.invalid.currentOwner");
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveErrors(request, messages);
			return mapping.findForward("input");
		}
		if (!securityService.isUserValid(newOwner)) {
			ActionMessage message = new ActionMessage(
					"message.transferOwner.invalid.newOwner");
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveErrors(request, messages);
			return mapping.findForward("input");
		}
		HttpSession session = request.getSession();

		// String[] dataTypes = new String[0];
		// if (theForm != null) {
		// dataTypes = (String[]) theForm
		// .get("dataType");
		//
		// }

		String dataType = theForm.getString("dataType");
		OwnershipTransferService transferService = new OwnershipTransferServiceImpl();
		List<String> dataIds = null;
		BaseService service = null;
		if (dataType
				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_SAMPLE)) {
			service = new SampleServiceLocalImpl(securityService);
			dataIds = ((SampleService) service)
					.findSampleIdsByOwner(currentOwner);
		} else if (dataType
				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PROTOCOL)) {
			service = new ProtocolServiceLocalImpl(securityService);
			dataIds = ((ProtocolService) service)
					.findProtocolIdsByOwner(currentOwner);
		} else if (dataType
				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PUBLICATION)) {
			service = new PublicationServiceLocalImpl(securityService);
			dataIds = ((PublicationService) service)
					.findPublicationIdsByOwner(currentOwner);
		} else if (dataType
				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_GROUP)) {
			service = new CommunityServiceLocalImpl(securityService);
			dataIds = ((CommunityService) service)
					.findCollaborationGroupIdsByOwner(currentOwner);
		} else {
			ActionMessage message = new ActionMessage(
					"message.transferOwner.invalid.type");
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveErrors(request, messages);
			return mapping.findForward("input");
		}
		if (dataIds == null || dataIds.isEmpty()) {
			ActionMessage message = new ActionMessage(
					"message.transferOwner.empty.data");
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			return mapping.findForward("input");
		}
		if (dataIds.size() < CUT_OFF_NUM_DATA) {
			int failures = transferService.transferOwner(service, dataIds,
					currentOwner, newOwner);
			ActionMessage message = new ActionMessage(
					"message.batchTransferOwner.generate.success", dataIds
							.size()
							- failures, dataType.toLowerCase());
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			if (failures > 0) {
				ActionMessage msg = new ActionMessage(
						"message.batchTransferOwner.failed", failures, dataType
								.toLowerCase());
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}

			saveMessages(request, messages);
			return mapping.findForward("success");
		}
		// run in a separate thread
		//
		// We only want one BatchOwnershipTransferService per session.
		//
		BatchOwnershipTransferProcess batchProcess = (BatchOwnershipTransferProcess) session
				.getAttribute("BatchOwnershipTransferProcess");
		if (batchProcess == null) {
			this.startThreadForBatchProcess(batchProcess, session, dataIds,
					transferService, service, dataType, dataIds, currentOwner,
					newOwner);
		} else {
			if (!batchProcess.isComplete()) {
				ActionMessage msg = new ActionMessage(
						"message.batchTransferOwner.duplicateRequest");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				return mapping.findForward("input");
			} else {
				ActionMessage msg = new ActionMessage(
						"message.batchTransferOwner.previousRequest.completed");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				return mapping.findForward("input");
			}
		}
		ActionMessage msg = new ActionMessage(
				"message.batchTransferOwner.longRunning", dataIds.size(),
				dataType.toLowerCase());
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, messages);
		return mapping.findForward("input");
	}

	private void startThreadForBatchProcess(
			BatchOwnershipTransferProcess batchProcess, HttpSession session,
			List<String> dataIdsToRun,
			OwnershipTransferService transferService, BaseService dataService,
			String dataType, List<String> dataIds, String currentOwner,
			String newOwner) throws Exception {
		session.setAttribute("hasResultsWaiting", true);
		batchProcess = new BatchOwnershipTransferProcess(transferService,
				dataService, dataType, dataIds, currentOwner, newOwner);
		batchProcess.process();
		session.setAttribute("BatchOwnershipTransferProcess", batchProcess);
		// obtain the list of long running processes
		List<LongRunningProcess> longRunningProcesses = new ArrayList<LongRunningProcess>();
		if (session.getAttribute("longRunningProcesses") != null) {
			longRunningProcesses = (List<LongRunningProcess>) session
					.getAttribute("longRunningProcesses");
		}
		longRunningProcesses.add(batchProcess);
		session.setAttribute("longRunningProcesses", longRunningProcesses);
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
