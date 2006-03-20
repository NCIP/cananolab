package gov.nih.nci.calab.ui.administration;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.7 2006-03-20 21:52:03 pansu Exp $ */

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.ui.core.*;

public class CreateSampleAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(CreateSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		try {
			// TODO fill in details for sample information */
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleId = (String) theForm.get("sampleId");
			String sampleType = (String) theForm.get("sampleType");
			String sampleSOP = (String) theForm.get("sampleSOP");
			String sampleDescription = (String) theForm
					.get("sampleDescription");
			String vendor = (String) theForm.get("vendor");
			String vendorSampleId = (String) theForm.get("vendorSampleId");
			String dateReceived = (String) theForm.get("dateReceived");
			String solubility = (String) theForm.get("solubility");
			String lotId = (String) theForm.get("lotId");
			String lotDescription = (String) theForm.get("lotDescription");
			String numContainers = (String) theForm.get("numberOfContainers");
			String generalComments = (String) theForm.get("generalComments");
			SampleBean sample = new SampleBean(sampleId, sampleType, sampleSOP,
					sampleDescription, vendor, vendorSampleId, dateReceived,
					solubility, lotId, lotDescription, numContainers,
					generalComments);
			ContainerBean[] containers=(ContainerBean[])theForm.get("containers");
			request.setAttribute("sample", sample);
			request.setAttribute("containers", containers);
			
			ManageSampleService manageSampleService=new ManageSampleService();
			manageSampleService.saveSample(sample, containers, generalComments);
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessages errors=new ActionMessages();
			ActionMessage error=new ActionMessage("error.createSample");
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error("Caught exception when creating a sample", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}
}
