package gov.nih.nci.calab.ui.sample;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewSampleDetailAction.java,v 1.5 2008-01-03 21:24:48 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ViewSampleDetailAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaActionForm theForm = (DynaActionForm) form;

		String containerId = (String) theForm.get("containerId");

		if (session.getAttribute("sampleContainers") != null) {
			List<ContainerBean> sampleContainers = new ArrayList<ContainerBean>(
					(List<? extends ContainerBean>) session
							.getAttribute("sampleContainers"));
			SampleBean sample = null;
			for (ContainerBean container : sampleContainers) {
				if (container.getContainerId().equals(containerId)) {
					sample = container.getSample();
					request.setAttribute("sample", sample);
					break;
				}
			}
			forward = mapping.findForward("success");
		} else {
			throw new InvalidSessionException(
					"Session containing the searched sample results either is expired.  Please log in again");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
