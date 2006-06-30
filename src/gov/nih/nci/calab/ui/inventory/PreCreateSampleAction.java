package gov.nih.nci.calab.ui.inventory;

/**
 * This class prepares the form fields in the Create Sample page
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateSampleAction.java,v 1.1 2006-06-30 20:56:09 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.service.inventory.ManageSampleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

public class PreCreateSampleAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
		String sampleNamePrefix = (String) theForm.get("sampleNamePrefix");
		String lotId = (String) theForm.get("lotId");
		int numContainers = Integer.parseInt((String) theForm
				.get("numberOfContainers"));

		ManageSampleService mangeSampleService = new ManageSampleService();
		// set default form values
		if (sampleNamePrefix.length() == 0) {
			theForm.set("sampleNamePrefix", mangeSampleService
					.getDefaultSampleNamePrefix());
		}
		theForm.set("configuredSampleNamePrefix", PropertyReader.getProperty(CalabConstants.CALAB_PROPERTY,"samplePrefix"));
		
		if (lotId.length() == 0) {
			theForm.set("lotId", mangeSampleService.getDefaultLotId());
		}
		ContainerBean[] origContainers = (ContainerBean[]) theForm
				.get("containers");
		ContainerBean[] containers = new ContainerBean[numContainers];

		// reuse containers from the previous request
		// set other containers to have values from the first container
		if (origContainers.length < numContainers) {
			for (int i = 0; i < origContainers.length; i++) {
				containers[i] = new ContainerBean(origContainers[i]);
			}
			for (int i = origContainers.length; i < numContainers; i++) {
				if (origContainers.length > 0) {
					containers[i] = new ContainerBean(origContainers[0]);
				}
				// if no containers from previous request, set them new
				else {
					containers[i] = new ContainerBean();
				}
			}

		} else {
			for (int i = 0; i < numContainers; i++) {
				containers[i] = new ContainerBean(origContainers[i]);
			}
		}
		theForm.set("containers", containers);
		forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
