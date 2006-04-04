package gov.nih.nci.calab.ui.administration;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.10 2006-04-04 15:33:45 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

public class CreateSampleAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(CreateSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		try {
			// TODO fill in details for sample information */
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleIdPrefix = (String) theForm.get("sampleIdPrefix");
			String sampleType = (String) theForm.get("sampleType");
			String sampleSOP = (String) theForm.get("sampleSOP");
			String sampleDescription = (String) theForm
					.get("sampleDescription");
			String sampleSource = (String) theForm.get("sampleSource");
			String sourceSampleId = (String) theForm.get("sourceSampleId");
			String dateReceived = (String) theForm.get("dateReceived");
			String solubility = (String) theForm.get("solubility");
			String lotId = (String) theForm.get("lotId");
			String lotDescription = (String) theForm.get("lotDescription");
			String numContainers = (String) theForm.get("numberOfContainers");
			String generalComments = (String) theForm.get("generalComments");
			ManageSampleService manageSampleService = new ManageSampleService();
			String sampleId = manageSampleService.getSampleId(sampleIdPrefix,
					lotId);

			// get user and date information from session
			String sampleSubmitter = (String)session.getAttribute("creator");
			String accessionDate=(String)session.getAttribute("creationDate");
			
			ContainerBean[] containers = (ContainerBean[]) theForm
					.get("containers");

			SampleBean sample = new SampleBean(sampleId, sampleType, sampleSOP,
					sampleDescription, sampleSource, sourceSampleId,
					dateReceived, solubility, lotId, lotDescription,
					numContainers, generalComments, sampleSubmitter,
					accessionDate, containers);

			request.setAttribute("sample", sample);

			manageSampleService.saveSample(sample, containers, generalComments);
            //set a flag to indicate that new sample have been created so session can 
			//be refreshed in initSession.do
			session.setAttribute("newSampleCreated", "yes");
			
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.createSample");
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
