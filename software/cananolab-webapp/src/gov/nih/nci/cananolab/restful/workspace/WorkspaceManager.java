/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.workspace;

/**
 * Action class for Transfer Ownership section.
 *
 * @author lethai, pansu
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceItem;
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
import java.util.Date;
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

public class WorkspaceManager {
	private static final int CUT_OFF_NUM_DATA = 30;

//	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return mapping.findForward("input");
//	}

	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request)
			throws Exception {
		
		SimpleWorkspaceBean workspaceBean = new SimpleWorkspaceBean();
		
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		for (int i = 0; i < 6; i++) {
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName("Sample " + i);
			item.setId(2030493 + i);
			item.setCreatedDate(new Date());
			item.setSubmisstionStatus("In Progress");
			List<String> actions = new ArrayList<String>();
			actions.add("View");
			actions.add("Edit");
			actions.add("Delete");
			item.setActions(actions);
			item.setAccess("Read Write Delete (Owner)");
			
			items.add(item);
		}
		
		workspaceBean.setSamples(items);
		
		items = new ArrayList<SimpleWorkspaceItem>();
		for (int i = 0; i < 5; i++) {
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName("Protocol " + i);
			item.setId(495959 + i);
			item.setCreatedDate(new Date());
			item.setSubmisstionStatus("Submitted for Public Access");
			List<String> actions = new ArrayList<String>();
			actions.add("View");
			actions.add("Edit");
			actions.add("Delete");
			item.setActions(actions);
			item.setAccess("Read Write Delete (Owner)");
			
			items.add(item);
		}
		
		workspaceBean.setProtocols(items);
		
		items = new ArrayList<SimpleWorkspaceItem>();
		for (int i = 0; i < 4; i++) {
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName("Publication " + i);
			item.setId(495959 + i);
			item.setCreatedDate(new Date());
			item.setSubmisstionStatus("In Review");
			List<String> actions = new ArrayList<String>();
			actions.add("View");
			actions.add("Edit");
			actions.add("Delete");
			item.setActions(actions);
			item.setAccess("Read Write Delete (Owner)");
			
			item.setPubMedId("1868677" + i);
			
			items.add(item);
		}
		
		workspaceBean.setPublications(items);
		
		
		return workspaceBean;

//		SecurityService securityService = getSecurityService(request);
//		
//		
//		
//		String currentOwner = (String) theForm.get("currentOwner");
//		String newOwner = (String) theForm.get("newOwner");
//		
//		
//		
//		ActionMessages messages = new ActionMessages();
//		// validate owner names
//		if (!securityService.isUserValid(currentOwner)) {
//			ActionMessage message = new ActionMessage(
//					"message.transferOwner.invalid.currentOwner");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
//			saveErrors(request, messages);
//			return mapping.findForward("input");
//		}
//		if (!securityService.isUserValid(newOwner)) {
//			ActionMessage message = new ActionMessage(
//					"message.transferOwner.invalid.newOwner");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
//			saveErrors(request, messages);
//			return mapping.findForward("input");
//		}
//		HttpSession session = request.getSession();
//
//		// String[] dataTypes = new String[0];
//		// if (theForm != null) {
//		// dataTypes = (String[]) theForm
//		// .get("dataType");
//		//
//		// }
//
//		String dataType = theForm.getString("dataType");
//		OwnershipTransferService transferService = new OwnershipTransferServiceImpl();
//		List<String> dataIds = null;
//		BaseService service = null;
//		if (dataType
//				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_SAMPLE)) {
//			service = new SampleServiceLocalImpl(securityService);
//			dataIds = ((SampleService) service)
//					.findSampleIdsByOwner(currentOwner);
//		} else if (dataType
//				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PROTOCOL)) {
//			service = new ProtocolServiceLocalImpl(securityService);
//			dataIds = ((ProtocolService) service)
//					.findProtocolIdsByOwner(currentOwner);
//		} else if (dataType
//				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PUBLICATION)) {
//			service = new PublicationServiceLocalImpl(securityService);
//			dataIds = ((PublicationService) service)
//					.findPublicationIdsByOwner(currentOwner);
//		} else if (dataType
//				.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_GROUP)) {
//			service = new CommunityServiceLocalImpl(securityService);
//			dataIds = ((CommunityService) service)
//					.findCollaborationGroupIdsByOwner(currentOwner);
//		} else {
//			ActionMessage message = new ActionMessage(
//					"message.transferOwner.invalid.type");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
//			saveErrors(request, messages);
//			return mapping.findForward("input");
//		}
//		if (dataIds == null || dataIds.isEmpty()) {
//			ActionMessage message = new ActionMessage(
//					"message.transferOwner.empty.data");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
//			saveMessages(request, messages);
//			return mapping.findForward("input");
//		}
//		if (dataIds.size() < CUT_OFF_NUM_DATA) {
//			int failures = transferService.transferOwner(service, dataIds,
//					currentOwner, newOwner);
//			ActionMessage message = new ActionMessage(
//					"message.batchTransferOwner.generate.success", dataIds
//							.size()
//							- failures, dataType.toLowerCase());
//			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
//			saveMessages(request, messages);
//			if (failures > 0) {
//				ActionMessage msg = new ActionMessage(
//						"message.batchTransferOwner.failed", failures, dataType
//								.toLowerCase());
//				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			}
//
//			saveMessages(request, messages);
//			return mapping.findForward("success");
//		}
//		// run in a separate thread
//		//
//		// We only want one BatchOwnershipTransferService per session.
//		//
//		BatchOwnershipTransferProcess batchProcess = (BatchOwnershipTransferProcess) session
//				.getAttribute("BatchOwnershipTransferProcess");
//		if (batchProcess == null) {
//			this.startThreadForBatchProcess(batchProcess, session, dataIds,
//					transferService, service, dataType, dataIds, currentOwner,
//					newOwner);
//		} else {
//			if (!batchProcess.isComplete()) {
//				ActionMessage msg = new ActionMessage(
//						"message.batchTransferOwner.duplicateRequest");
//				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveMessages(request, messages);
//				return mapping.findForward("input");
//			} else {
//				ActionMessage msg = new ActionMessage(
//						"message.batchTransferOwner.previousRequest.completed");
//				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveMessages(request, messages);
//				return mapping.findForward("input");
//			}
//		}
//		ActionMessage msg = new ActionMessage(
//				"message.batchTransferOwner.longRunning", dataIds.size(),
//				dataType.toLowerCase());
//		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//		saveMessages(request, messages);
//		return mapping.findForward("input");
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
