package gov.nih.nci.calab.ui.search;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewSampleDetailAction.java,v 1.7 2006-06-30 21:06:22 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

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
		boolean isAliquot = false;
		if (theForm.get("isAliquot") != null) {
			isAliquot = (Boolean) theForm.get("isAliquot");
		}
		// if no aliquot information show sample details and its containers

		if (!isAliquot) {
//			String sampleId = (String) theForm.get("sampleId");
//			
//			int containerNum = Integer.parseInt((String) theForm
//					.get("containerNum"));
//			if (session.getAttribute("sampleContainers") != null) {
//				List<ContainerBean> sampleContainers = new ArrayList<ContainerBean>(
//						(List<? extends ContainerBean>) session
//								.getAttribute("sampleContainers"));
//				SampleBean sample = null;
//				for (ContainerBean container : sampleContainers) {
//					if (container.getSample().getSampleId().equals(sampleId)
//							&& container.getContainerNumber() == containerNum) {
//						sample = container.getSample();
//						request.setAttribute("sample", sample);
//						request.setAttribute("containerNum", containerNum);
//						break;
//					}
//				}

			String containerId=(String)theForm.get("containerId");
			if (session.getAttribute("sampleContainers") != null) {
			List<ContainerBean> sampleContainers = new ArrayList<ContainerBean>(
					(List<? extends ContainerBean>) session
							.getAttribute("sampleContainers"));
			SampleBean sample = null;
			for (ContainerBean container : sampleContainers) {
				if (container.getContainerId().equals(containerId)) {
					sample = container.getSample();
					request.setAttribute("sample", sample);
					request.setAttribute("containerNum", container.getContainerNumber());
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
		}
		// show aliquot and its container detail
		else {
			int aliquotNum = Integer.parseInt((String) theForm
					.get("aliquotNum"));
			if (session.getAttribute("aliquots") != null) {
				List aliquots = (List) session.getAttribute("aliquots");
				AliquotBean aliquot = ((AliquotBean) aliquots.get(aliquotNum));
				request.setAttribute("aliquot", aliquot);
				request.setAttribute("aliquotNum", aliquotNum);
				forward = mapping.findForward("success");
			} else {
				throw new CalabException(
						"Session containing the searched sample aliquot results either is expired or doesn't exist");
			}
		}
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}
}
