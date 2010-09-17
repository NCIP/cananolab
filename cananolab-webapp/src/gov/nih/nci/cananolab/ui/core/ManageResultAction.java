/*
 The caNanoLab Software License, Version 1.5.2

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

/**
 * This class manages the results for long-running process
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ManageResultAction extends AbstractForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null && !user.isCurator()) {
			throw new NoAccessException(
					"You need to be a curator to access the page");
		}
		ActionMessages messages = new ActionMessages();
		String processType = request.getParameter("processType");
		if (processType == null) {
			processType = (String) session.getAttribute("processType");
			if (processType == null) {
				ActionMessage msg = new ActionMessage(
						"message.batchDataAvailability",
						"Please specify a process type in the request");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, messages);
				request.setAttribute("processComplete", true);
				return super.execute(mapping, form, request, response);
			}
		}
		if (processType.equals("dataAvailability")) {
			// get the batch process from the session
			return manageDataAvailability(mapping, form, request, response,
					messages);
		}
		return super.execute(mapping, form, request, response);
	}

	private ActionForward manageDataAvailability(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, ActionMessages messages)
			throws Exception {
		HttpSession session = request.getSession();
		BatchDataAvailabilityProcess batchDataAvailabilityProcess = (BatchDataAvailabilityProcess) session
				.getAttribute("batchDataAvailabilityProcess");

		if (batchDataAvailabilityProcess != null) {
			ActionMessage msg = new ActionMessage(
					"message.batchDataAvailability",
					batchDataAvailabilityProcess.getStatusMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			if (batchDataAvailabilityProcess.isWithError()) {
				saveErrors(request, messages);
			} else {
				saveMessages(request, messages);
			}
			if (batchDataAvailabilityProcess.isComplete()) {
				request.setAttribute("processComplete", true);
				//
				// Once the process is complete, remove the binding from
				// the session.
				//
				session.removeAttribute("batchDataAvailabilityProcess");
				session.removeAttribute("processType");
			}
			return super.execute(mapping, form, request, response);
		} else {
			ActionMessage msg = new ActionMessage(
					"message.batchDataAvailability",
					"No batch data availability process started");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
			request.setAttribute("processComplete", true);
			return super.execute(mapping, form, request, response);
		}
	}

	public boolean loginRequired() {
		return true;
	}
}
