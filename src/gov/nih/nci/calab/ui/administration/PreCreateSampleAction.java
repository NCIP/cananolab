package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares the form fields in the Create Sample page
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateSampleAction.java,v 1.11 2006-05-02 22:27:17 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.service.administration.ManageSampleService;
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
				StorageLocation loc = new StorageLocation(origContainers[i]
						.getStorageLocation().getLab(), origContainers[i]
						.getStorageLocation().getRoom(), origContainers[i]
						.getStorageLocation().getFreezer(), origContainers[i]
						.getStorageLocation().getShelf(), origContainers[i]
						.getStorageLocation().getRack(), origContainers[i]
						.getStorageLocation().getBox());

				containers[i] = new ContainerBean(origContainers[i]
						.getContainerType(), origContainers[i]
						.getOtherContainerType(), origContainers[i]
						.getQuantity(), origContainers[i].getQuantityUnit(),
						origContainers[i].getConcentration(), origContainers[i]
								.getConcentrationUnit(), origContainers[i]
								.getVolume(),
						origContainers[i].getVolumeUnit(), origContainers[i]
								.getSolvent(), origContainers[i]
								.getSafetyPrecaution(), origContainers[i]
								.getStorageCondition(), loc, origContainers[i]
								.getContainerComments());

			}
			for (int i = origContainers.length; i < numContainers; i++) {
				if (origContainers.length > 0) {
					StorageLocation loc = new StorageLocation(origContainers[0]
							.getStorageLocation().getLab(), origContainers[0]
							.getStorageLocation().getRoom(), origContainers[0]
							.getStorageLocation().getFreezer(),
							origContainers[0].getStorageLocation().getShelf(),
							origContainers[0].getStorageLocation().getRack(),
							origContainers[0].getStorageLocation().getBox());

					containers[i] = new ContainerBean(origContainers[0]
							.getContainerType(), origContainers[0]
							.getOtherContainerType(), origContainers[0]
							.getQuantity(),
							origContainers[0].getQuantityUnit(),
							origContainers[0].getConcentration(),
							origContainers[0].getConcentrationUnit(),
							origContainers[0].getVolume(), origContainers[0]
									.getVolumeUnit(), origContainers[0]
									.getSolvent(), origContainers[0]
									.getSafetyPrecaution(), origContainers[0]
									.getStorageCondition(), loc,
							origContainers[0].getContainerComments());
				}
				// if no containers from previous request, set them new
				else {
					containers[i] = new ContainerBean();
				}
			}

		} else {
			for (int i = 0; i < numContainers; i++) {
				StorageLocation loc = new StorageLocation(origContainers[i]
						.getStorageLocation().getLab(), origContainers[i]
						.getStorageLocation().getRoom(), origContainers[i]
						.getStorageLocation().getFreezer(), origContainers[i]
						.getStorageLocation().getShelf(), origContainers[i]
						.getStorageLocation().getRack(), origContainers[i]
						.getStorageLocation().getBox());

				containers[i] = new ContainerBean(origContainers[i]
						.getContainerType(), origContainers[i]
						.getOtherContainerType(), origContainers[i]
						.getQuantity(), origContainers[i].getQuantityUnit(),
						origContainers[i].getConcentration(), origContainers[i]
								.getConcentrationUnit(), origContainers[i]
								.getVolume(),
						origContainers[i].getVolumeUnit(), origContainers[i]
								.getSolvent(), origContainers[i]
								.getSafetyPrecaution(), origContainers[i]
								.getStorageCondition(), loc, origContainers[i]
								.getContainerComments());
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
