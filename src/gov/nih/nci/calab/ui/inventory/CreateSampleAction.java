package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.6 2006-11-15 16:45:15 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.service.inventory.ManageSampleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class CreateSampleAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleNamePrefix = (String) theForm.get("sampleNamePrefix");
		String preconfiguredPrefix = PropertyReader.getProperty(
				CalabConstants.CALAB_PROPERTY, "samplePrefix");
		if (!sampleNamePrefix.startsWith(preconfiguredPrefix)) {

			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"error.createSample.SampleIDFormat", preconfiguredPrefix);
			msgs.add("error", msg);
			saveMessages(request, msgs);

			forward = mapping.findForward("input");

			return forward;
		}
		String sampleType = (String) theForm.get("sampleType");
//		String otherSampleType = (String) theForm.get("otherSampleType");
		String sampleSOP = (String) theForm.get("sampleSOP");
		String sampleDescription = (String) theForm.get("sampleDescription");
		String sampleSource = (String) theForm.get("sampleSource");
		String sourceSampleId = (String) theForm.get("sourceSampleId");
		String dateReceivedStr = (String) theForm.get("dateReceived");
		Date dateReceived = StringUtils.convertToDate(dateReceivedStr,
				CalabConstants.ACCEPT_DATE_FORMAT);

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
		//update container name to be full container name
		String containerPrefix=manageSampleService.getContainerPrefix(sampleNamePrefix, lotId);
		for (ContainerBean container: containers) {
			container.setContainerName(containerPrefix+"-"+container.getContainerName());
		}
		Date creationDate = new Date();
//		SampleBean sample = new SampleBean(sampleNamePrefix, sampleName,
//				sampleType, otherSampleType, sampleSOP, sampleDescription,
//				sampleSource, sourceSampleId, dateReceived, solubility, lotId,
//				lotDescription, numContainers, generalComments,
//				sampleSubmitter, creationDate, containers);
		SampleBean sample = new SampleBean(sampleNamePrefix, sampleName,
				sampleType, sampleSOP, sampleDescription,
				sampleSource, sourceSampleId, dateReceived, solubility, lotId,
				lotDescription, numContainers, generalComments,
				sampleSubmitter, creationDate, containers);
		request.setAttribute("sample", sample);
		manageSampleService.saveSample(sample, containers);
		
		//create a new user group if the source is not already a group
		UserService userService=new UserService(CalabConstants.CSM_APP_NAME);
		userService.createAGroup(sample.getSampleSource());
		
		// set a flag to indicate that new sample have been created so session
		// can be refreshed in initSession.do
		session.setAttribute("newSampleCreated", "yes");

		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		int numContainers = Integer.parseInt((String) theForm
				.get("numberOfContainers"));

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
		return mapping.getInputForward();
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);

		InitSessionSetup.getInstance().setAllSampleTypes(session);
		InitSessionSetup.getInstance().setAllSampleSOPs(session);
		InitSessionSetup.getInstance().setAllSampleContainerTypes(session);
		InitSessionSetup.getInstance().setAllSampleContainerInfo(session);
		InitSessionSetup.getInstance().setCurrentUser(session);
		
		ManageSampleService mangeSampleService = new ManageSampleService();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.getMap().clear();
		
		theForm.set("lotId", mangeSampleService.getDefaultLotId());
		theForm.set("numberOfContainers", "1");
		theForm.set("sampleNamePrefix", mangeSampleService
				.getDefaultSampleNamePrefix());
		theForm.set("configuredSampleNamePrefix", PropertyReader.getProperty(
				CalabConstants.CALAB_PROPERTY, "samplePrefix"));
		ContainerBean[] containers=new ContainerBean[] {new ContainerBean()};
		theForm.set("containers", containers);	
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
