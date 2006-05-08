package gov.nih.nci.calab.ui.administration;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.14 2006-05-08 18:58:23 zengje Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

public class CreateSampleAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		// TODO fill in details for sample information */
		DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
		String sampleNamePrefix = (String) theForm.get("sampleNamePrefix");
		String preconfiguredPrefix = PropertyReader.getProperty(CalabConstants.CALAB_PROPERTY,"samplePrefix");
		if (!sampleNamePrefix.equals(preconfiguredPrefix)) {
			
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.createSample.SampleIDFormat", preconfiguredPrefix);
			msgs.add("error", msg);
			saveMessages(request, msgs);
			
			forward = mapping.findForward("input");

			return forward;
		}
		String sampleType = (String) theForm.get("sampleType");
		String sampleSOP = (String) theForm.get("sampleSOP");
		String sampleDescription = (String) theForm.get("sampleDescription");
		String sampleSource = (String) theForm.get("sampleSource");
		String sourceSampleId = (String) theForm.get("sourceSampleId");
		String dateReceived = (String) theForm.get("dateReceived");
		String solubility = (String) theForm.get("solubility");
		String lotId = (String) theForm.get("lotId");
		String lotDescription = (String) theForm.get("lotDescription");
		String numContainers = (String) theForm.get("numberOfContainers");
		String generalComments = (String) theForm.get("generalComments");
		ManageSampleService manageSampleService = new ManageSampleService();
		String sampleName = manageSampleService.getSampleName(sampleNamePrefix,
				lotId);

		// get user and date information from session
		String sampleSubmitter = (String) session.getAttribute("creator");
		String accessionDate = (String) session.getAttribute("creationDate");

		ContainerBean[] containers = (ContainerBean[]) theForm
				.get("containers");

		SampleBean sample = new SampleBean(sampleNamePrefix, sampleName,
				sampleType, sampleSOP, sampleDescription, sampleSource,
				sourceSampleId, dateReceived, solubility, lotId,
				lotDescription, numContainers, generalComments,
				sampleSubmitter, accessionDate, containers);

		request.setAttribute("sample", sample);

		manageSampleService.saveSample(sample, containers);
		// set a flag to indicate that new sample have been created so session
		// can be refreshed in initSession.do
		session.setAttribute("newSampleCreated", "yes");

		forward = mapping.findForward("success");

		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
	
	private boolean validateSamplePrefix(String samplePrefix) {
		if (samplePrefix.equals(PropertyReader.getProperty(CalabConstants.CALAB_PROPERTY,"samplePrefix"))) {
			return true;
		}else {
			return false;
		}
	}
	
}
