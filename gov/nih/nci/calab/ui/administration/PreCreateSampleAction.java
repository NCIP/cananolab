package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares the form fields in the Create Sample page
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateSampleAction.java,v 1.2 2006-03-16 21:53:41 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

public class PreCreateSampleAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(PreCreateSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleId = (String) theForm.get("sampleId");
			String lotId = (String) theForm.get("lotId");
			int numContainers=Integer.parseInt((String)theForm.get("numberOfContainers"));
			
			ManageSampleService mangeSampleService=new ManageSampleService();
			LookupService lookupService=new LookupService();
			//set default form values
			if (sampleId.length()==0) {
			  theForm.set("sampleId", mangeSampleService.getDefaultSampleId());
			}
			if (lotId.length()==0) {
			  theForm.set("lotId", mangeSampleService.getDefaultLotId()); 
			}
			ContainerBean[] containers=new ContainerBean[numContainers];
			for (int i=0; i<numContainers; i++) {
				containers[i]=new ContainerBean();
			}
			theForm.set("containers", containers);
			
			//retrieve from sesssion first if available assuming these values
			//are not likely to change within the same session
			
			if (session.getAttribute("allSampleTypes") == null) {
			    List sampleTypes=lookupService.getAllSampleTypes();
				session.setAttribute("allSampleTypes", sampleTypes);
			}
			if (session.getAttribute("allSampleSOPs") == null) {
			    List sampleSOPs =mangeSampleService.getAllSampleSOPs();
				session.setAttribute("allSampleSOPs", sampleSOPs);
			}
			if (session.getAttribute("sampleContainerInfo") == null) {
				ContainerInfoBean containerInfo=lookupService.getSampleContainerInfo();
				session.setAttribute("sampleContainerInfo", containerInfo);
			}
			request.setAttribute("sampleId", sampleId);
			forward = mapping.findForward("success");
		} catch (Exception e) {
			logger.error("Caught exceptions when loading create sample page", e);
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
