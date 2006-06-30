package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.1 2006-06-30 20:56:09 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.service.inventory.ManageSampleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.Date;

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
		if (!sampleNamePrefix.startsWith(preconfiguredPrefix)) {
			
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.createSample.SampleIDFormat", preconfiguredPrefix);
			msgs.add("error", msg);
			saveMessages(request, msgs);
			
			forward = mapping.findForward("input");

			return forward;
		}
		String sampleType = (String) theForm.get("sampleType");
		String otherSampleType = (String)theForm.get("otherSampleType");
		String sampleSOP = (String) theForm.get("sampleSOP");
		String sampleDescription = (String) theForm.get("sampleDescription");
		String sampleSource = (String) theForm.get("sampleSource");
		String sourceSampleId = (String) theForm.get("sourceSampleId");
		String dateReceivedStr = (String) theForm.get("dateReceived");
		Date dateReceived=StringUtils.convertToDate(dateReceivedStr, CalabConstants.ACCEPT_DATE_FORMAT);
		
		String solubility = (String) theForm.get("solubility");
		String lotId = (String) theForm.get("lotId");
		String lotDescription = (String) theForm.get("lotDescription");
		String numContainers = (String) theForm.get("numberOfContainers");
		String generalComments = (String) theForm.get("generalComments");
		ManageSampleService manageSampleService = new ManageSampleService();
		String sampleName = manageSampleService.getSampleName(sampleNamePrefix,
				lotId);

		// get user information from session
		String sampleSubmitter = (String) session.getAttribute("creator");
		ContainerBean[] containers = (ContainerBean[]) theForm
				.get("containers");
		Date creationDate=new Date();
		SampleBean sample = new SampleBean(sampleNamePrefix, sampleName,
				sampleType, otherSampleType, sampleSOP, sampleDescription, sampleSource,
				sourceSampleId, dateReceived, solubility, lotId,
				lotDescription, numContainers, generalComments,
				sampleSubmitter, creationDate, containers);

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
}
