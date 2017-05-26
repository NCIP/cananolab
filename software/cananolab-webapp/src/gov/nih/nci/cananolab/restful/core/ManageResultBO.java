package gov.nih.nci.cananolab.restful.core;

import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.admin.impl.BatchOwnershipTransferProcess;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("manageResultBO")
public class ManageResultBO
{
	public List<LongRunningProcess> execute(HttpServletRequest request) throws Exception 
	{
		HttpSession session = request.getSession();
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		if (userDetails != null && !userDetails.isCurator()) {
			throw new NoAccessException("You need to be a curator to access the page");
		}
		// obtain the list of long running processes
		List<LongRunningProcess> processes = (List<LongRunningProcess>) session.getAttribute("longRunningProcesses");
		if (processes == null || processes.isEmpty()) {
			// hide the results tab
			session.setAttribute("hasResultsWaiting", false);
			//return super.execute(mapping, form, request, response);
		} else {
			// store the previous state for JSP display
			request.setAttribute("previousLongRunningProcesses", processes);
			List<LongRunningProcess> updatedProcesses = new ArrayList<LongRunningProcess>(processes);
			int i = 0;
			// remove completed process
			for (LongRunningProcess process : processes)
			{
				if (process.isComplete()) {
					updatedProcesses.remove(i);
					if (process instanceof BatchDataAvailabilityProcess) {
						session.removeAttribute("BatchDataAvailabilityProcess");
					} else if (process instanceof BatchOwnershipTransferProcess) {
						session.removeAttribute("BatchOwnershipTransferProcess");
					}
				}
				i++;
			}
			session.setAttribute("longRunningProcesses", updatedProcesses);
			//	return super.execute(mapping, form, request, response);
		}
		return processes;
	}
	
}
