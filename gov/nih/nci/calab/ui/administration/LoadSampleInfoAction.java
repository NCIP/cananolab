package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares the form fields in the Create Sample page
 * 
 * @author pansu
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.ui.core.*;

public class LoadSampleInfoAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(LoadSampleInfoAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleId = (String) theForm.get("sampleId");
			String lotId = (String) theForm.get("lotId");

			ManageSampleService service=new ManageSampleService();
			//set default form values
			if (sampleId.length()==0) {
			  theForm.set("sampleId", service.getDefaultSampleId());
			}
			if (lotId.length()==0) {
			  theForm.set("lotId", service.getDefaultLotId()); 
			}
			
			//retrieve from sesssion first if available assuming these values
			//are not likely to change within the same session
			List sampleTypes = null;
			if (session.getAttribute("allSampleTypes") != null) {
				sampleTypes = (List) session.getAttribute("allSampleTypes");
			} else {
				sampleTypes=service.getAllSampleTypes();
				session.setAttribute("allSampleTypes", sampleTypes);
			}
			ContainerInfoBean containerInfo = null;
			if (session.getAttribute("containerInfo") != null) {
				containerInfo = (ContainerInfoBean) session
						.getAttribute("containerInfo");
			} else {
				containerInfo=service.getContainerInfo();
				session.setAttribute("sampleContainerInfo", containerInfo);
			}
			request.setAttribute("sampleId", sampleId);
			forward = mapping.findForward("success");
		} catch (Exception e) {
			logger.error("", e);
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
