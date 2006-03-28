package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares the form fields in the Create Sample page
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateSampleAction.java,v 1.8 2006-03-28 23:03:05 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

public class PreCreateSampleAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(PreCreateSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		
		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleIdPrefix = (String) theForm.get("sampleIdPrefix");
			String lotId = (String) theForm.get("lotId");
			int numContainers = Integer.parseInt((String) theForm
					.get("numberOfContainers"));

			ManageSampleService mangeSampleService = new ManageSampleService();
			// set default form values
			if (sampleIdPrefix.length() == 0) {
				theForm.set("sampleIdPrefix", mangeSampleService
						.getDefaultSampleIdPrefix());
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
							.getStorageLocation().getFreezer(),
							origContainers[i].getStorageLocation().getShelf(),
							origContainers[i].getStorageLocation().getRack(),
							origContainers[i].getStorageLocation().getBox());

					containers[i] = new ContainerBean(origContainers[i]
							.getContainerType(), origContainers[i]
							.getOtherContainerType(), origContainers[i]
							.getQuantity(),
							origContainers[i].getQuantityUnit(),
							origContainers[i].getConcentration(),
							origContainers[i].getConcentrationUnit(),
							origContainers[i].getVolume(), origContainers[i]
									.getVolumeUnit(), origContainers[i]
									.getSolvent(), origContainers[i]
									.getSafetyPrecaution(), origContainers[i]
									.getStorageCondition(), loc,
							origContainers[i].getContainerComments());

				}
				for (int i = origContainers.length; i < numContainers; i++) {
					if (origContainers.length > 0) {
						StorageLocation loc = new StorageLocation(
								origContainers[0].getStorageLocation().getLab(),
								origContainers[0].getStorageLocation()
										.getRoom(), origContainers[0]
										.getStorageLocation().getFreezer(),
								origContainers[0].getStorageLocation()
										.getShelf(), origContainers[0]
										.getStorageLocation().getRack(),
								origContainers[0].getStorageLocation().getBox());

						containers[i] = new ContainerBean(origContainers[0]
								.getContainerType(), origContainers[0]
								.getOtherContainerType(), origContainers[0]
								.getQuantity(), origContainers[0]
								.getQuantityUnit(), origContainers[0]
								.getConcentration(), origContainers[0]
								.getConcentrationUnit(), origContainers[0]
								.getVolume(),
								origContainers[0].getVolumeUnit(),
								origContainers[0].getSolvent(),
								origContainers[0].getSafetyPrecaution(),
								origContainers[0].getStorageCondition(), loc,
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
							.getStorageLocation().getFreezer(),
							origContainers[i].getStorageLocation().getShelf(),
							origContainers[i].getStorageLocation().getRack(),
							origContainers[i].getStorageLocation().getBox());

					containers[i] = new ContainerBean(origContainers[i]
							.getContainerType(), origContainers[i]
							.getOtherContainerType(), origContainers[i]
							.getQuantity(),
							origContainers[i].getQuantityUnit(),
							origContainers[i].getConcentration(),
							origContainers[i].getConcentrationUnit(),
							origContainers[i].getVolume(), origContainers[i]
									.getVolumeUnit(), origContainers[i]
									.getSolvent(), origContainers[i]
									.getSafetyPrecaution(), origContainers[i]
									.getStorageCondition(), loc,
							origContainers[i].getContainerComments());
				}
			}
			theForm.set("containers", containers);

			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.preCreateSample");
			errors.add("error", error);
			saveMessages(request, errors);
			logger
					.error("Caught exceptions when loading create sample page",
							e);
			forward = mapping.findForward("failure");
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}
}
