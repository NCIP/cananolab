package gov.nih.nci.calab.ui.sample;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewSampleDetailAction.java,v 1.3 2007-12-05 20:01:09 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
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
			throw new CaNanoLabException(
					"Session containing the searched sample results either is expired or doesn't exist when viewing sample details");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
