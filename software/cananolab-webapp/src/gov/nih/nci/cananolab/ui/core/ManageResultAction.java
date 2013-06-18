/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.core;

/**
 * This class manages the results for long-running process
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.admin.impl.BatchOwnershipTransferProcess;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;
import gov.nih.nci.cananolab.service.security.UserBean;

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
			request.setAttribute("previousLongRunningProcesses", processes);
			List<LongRunningProcess> updatedProcesses = new ArrayList<LongRunningProcess>(
					processes);
			int i = 0;
			// remove completed process
			for (LongRunningProcess process : processes) {
				if (process.isComplete()) {
					updatedProcesses.remove(i);
					if (process instanceof BatchDataAvailabilityProcess) {
						session.removeAttribute("BatchDataAvailabilityProcess");
					} else if (process instanceof BatchOwnershipTransferProcess) {
						session
								.removeAttribute("BatchOwnershipTransferProcess");
					}
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
