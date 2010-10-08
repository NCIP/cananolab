/*
 The caNanoLab Software License, Version 1.5.2

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.curation;

/**
 * This class handles batch data availability request in a separate thread
 *
 * @author lethai, pansu
 */

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
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
import org.apache.struts.action.DynaActionForm;

public class BatchDataAvailabilityAction extends AbstractDispatchAction {
	private DataAvailabilityService dataAvailabilityService;
	private static final int CUT_OFF_NUM_SAMPLES = 30;

	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		theForm.set("option", BatchDataAvailabilityProcess.BATCH_OPTION1);
		return mapping.findForward("input");
	}

	// option1 - generate all: update existing one and generate new ones.
	// option2 - re-generate for samples with existing data availability
	// option3 - delete existing data availability
	public ActionForward generate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		String option = theForm.getString("option");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		ActionMessages messages = new ActionMessages();
		SecurityService securityService = new SecurityService(
				AccessibilityBean.CSM_APP_NAME, user);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);

		List<String> sampleIdsToRun = new ArrayList<String>();
		if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)) {
			// find all sampleIds
			sampleIdsToRun = sampleService.findSampleIdsBy("", "", null, null,
					null, null, null, null, null, null, null);
		} else {
			// find samplesIds with existing data availability
			sampleIdsToRun = dataAvailabilityService
					.findSampleIdsWithDataAvailability(securityService);
		}
		// empty sampleIds
		if (sampleIdsToRun.isEmpty()) {
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)
					|| option
							.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.empty.metrics");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			} else {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.nosamples");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}
			saveMessages(request, messages);
			return mapping.findForward("input");
		}
		// check how many samples to run, if less than the CUT_OFF_NUM_SAMPLES,
		// we don't have to run batch in a separate thread
		if (sampleIdsToRun.size() < CUT_OFF_NUM_SAMPLES) {
			int i = 0;
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)
					|| option
							.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)) {
				i = dataAvailabilityService.saveBatchDataAvailability(
						sampleIdsToRun, securityService);
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.generate.success",
						sampleIdsToRun.size());
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
				i = dataAvailabilityService.deleteBatchDataAvailability(
						sampleIdsToRun, securityService);
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.delete.success",
						sampleIdsToRun.size());
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}
			if (i > 0) {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.failed", i);
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}

			saveMessages(request, messages);
			return mapping.findForward("input");
		}

		// run in a separate thread
		//
		// We only want one BatchDataAvailabilityProcess per session.
		//
		BatchDataAvailabilityProcess batchProcess = (BatchDataAvailabilityProcess) session
				.getAttribute("BatchDataAvailabilityProcess");
		if (batchProcess == null) {
			this.startThreadForBatchProcess(batchProcess, session,
					sampleIdsToRun, dataAvailabilityService, securityService,
					option, user);
		} else {
			if (!batchProcess.isComplete()) {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.duplicateRequest");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				return mapping.findForward("input");
			} else {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.previousRequest.completed");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				return mapping.findForward("input");
			}
		}
		String optionMessage = "generate";
		if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
			optionMessage = "delete";
		}
		ActionMessage msg = new ActionMessage(
				"message.batchDataAvailability.longRunning", sampleIdsToRun
						.size(), optionMessage);
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, messages);
		return mapping.findForward("input");
	}

	private void startThreadForBatchProcess(
			BatchDataAvailabilityProcess batchProcess, HttpSession session,
			List<String> sampleIdsToRun,
			DataAvailabilityService dataAvailabilityService,
			SecurityService securityService, String option, UserBean user)
			throws Exception {
		session.setAttribute("hasResultsWaiting", true);
		batchProcess = new BatchDataAvailabilityProcess(
				dataAvailabilityService, securityService, sampleIdsToRun,
				option, user);
		batchProcess.process();
		session.setAttribute("BatchDataAvailabilityProcess", batchProcess);
		// obtain the list of long running processes
		List<LongRunningProcess> longRunningProcesses = new ArrayList<LongRunningProcess>();
		if (session.getAttribute("longRunningProcesses") != null) {
			longRunningProcesses = (List<LongRunningProcess>) session
					.getAttribute("longRunningProcesses");
		}
		longRunningProcesses.add(batchProcess);
		session.setAttribute("longRunningProcesses", longRunningProcesses);
	}
}
