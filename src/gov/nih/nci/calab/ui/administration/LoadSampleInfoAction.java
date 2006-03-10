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

import gov.nih.nci.calab.ui.core.*;

public class LoadSampleInfoAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(CreateSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String sampleId = (String) theForm.get("sampleId");
			String lotId = (String) theForm.get("lotId");
			Integer numberOfContainers=(Integer)theForm.get("numberOfContainers");
			//TODO fill in details for sample information */
			
			//set default form values
			if (sampleId==null) {
			  theForm.set("sampleId", "NCL-6-1");// tmp code to be replaced
			}
			if (lotId==null) {
			  theForm.set("lotId", "1"); //tmp code to be replaced
			}
			if (numberOfContainers==null) {
			  theForm.set("numberOfContainers", 1);
			}		
			
			List sampleTypes = null;
			if (session.getAttribute("allSampleTypes") != null) {
				sampleTypes = (List) session.getAttribute("allSampleTypes");
			} else {
				// tmp code to be replaced
				sampleTypes = new ArrayList();
				sampleTypes.add("Dentrimer");
				sampleTypes.add("Polymer");
				// end of tmp code
				session.setAttribute("allSampleTypes", sampleTypes);
			}
			ContainerInfoBean containerInfo = null;
			if (session.getAttribute("containerInfo") != null) {
				containerInfo = (ContainerInfoBean) session
						.getAttribute("containerInfo");
			} else {
				// tmp code to be replaced
				List containerTypes = new ArrayList();
				containerTypes.add("Tube");
				containerTypes.add("Vial");
				containerTypes.add("Other");

				List quantityUnits = new ArrayList();
				quantityUnits.add("g");
				quantityUnits.add("mg");

				List concentrationUnits = new ArrayList();
				concentrationUnits.add("g/ml");
				concentrationUnits.add("mg/ml");

				List volumeUnits = new ArrayList();
				volumeUnits.add("ml");
				volumeUnits.add("ul");

				List rooms = new ArrayList();
				rooms.add("250");
				rooms.add("117");

				List freezers = new ArrayList();
				freezers.add("F1");
				freezers.add("F2");

				containerInfo = new ContainerInfoBean(containerTypes,
						quantityUnits, concentrationUnits, volumeUnits, rooms,
						freezers);
				// end of tmp code
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
