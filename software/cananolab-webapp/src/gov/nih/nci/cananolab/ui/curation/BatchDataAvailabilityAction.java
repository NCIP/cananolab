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

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

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
	// option3 - delete data availability for all samples
	public ActionForward generate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		String option = theForm.getString("option");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		ActionMessages messages = new ActionMessages();
		//
		// We only want one BatchDataAvailabilityProcess per session.
		//
		BatchDataAvailabilityProcess batchProcess = (BatchDataAvailabilityProcess) session
				.getAttribute("batchDataAvailabilityProcess");
		if (batchProcess == null) {
			batchProcess = new BatchDataAvailabilityProcess(
					dataAvailabilityService, option, user);
			session.setAttribute("batchDataAvailabilityProcess", batchProcess);
			session.setAttribute("processType", "dataAvailability");
			batchProcess.process();
			session.setAttribute("hasResultsWaiting", true);
		} else {
			if (!batchProcess.isComplete()) {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability.duplicateRequest");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				return mapping.findForward("input");
			}
		}
		return mapping.findForward("batchDataAvailabilityResults");
	}
}
