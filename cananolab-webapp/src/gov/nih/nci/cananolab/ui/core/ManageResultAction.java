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
import gov.nih.nci.cananolab.service.common.LongRunningProcess;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
		// obtain the list of long running processes
		List<LongRunningProcess> processes = (List<LongRunningProcess>) session
				.getAttribute("longRunningProcesses");
		if (processes == null || processes.isEmpty()) {
			// hide the results tab
			session.setAttribute("hasResultsWaiting", false);
			return super.execute(mapping, form, request, response);
		} else {
			// store the previous state for JSP display
			session.setAttribute("previousLongRunningProcesses", processes);
			List<LongRunningProcess> updatedProcesses = new ArrayList<LongRunningProcess>(
					processes);
			int i = 0;
			// remove completed process
			for (LongRunningProcess process : processes) {
				if (process.isComplete()) {
					updatedProcesses.remove(i);
				}
				i++;
			}
			session.setAttribute("longRunningProcesses", updatedProcesses);
			return super.execute(mapping, form, request, response);
		}
	}

	public boolean loginRequired() {
		return true;
	}
}
