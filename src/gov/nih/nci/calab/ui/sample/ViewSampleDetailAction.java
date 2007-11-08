package gov.nih.nci.calab.ui.sample;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewSampleDetailAction.java,v 1.2 2007-11-08 20:41:11 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ViewSampleDetailAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(ViewSampleDetailAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ActionMessages messages = new ActionMessages();
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
			logger
					.error("Session containing the searched sample results either is expired or doesn't exist");
			ActionMessage error = new ActionMessage(
					"error.viewSampleDetails.nosamples");
			messages.add("error", error);
			saveMessages(request, messages);
			forward = mapping.getInputForward();
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
