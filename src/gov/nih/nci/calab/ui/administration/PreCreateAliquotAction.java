package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares data to prepopulate the view create aliquot. 
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateAliquotAction.java,v 1.4 2006-03-20 20:47:39 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.service.administration.ManageAliquotService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
import java.util.List;

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

public class PreCreateAliquotAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(PreCreateAliquotAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		try {
			// TODO fill in details for sample information */
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;

			// retrieve from sesssion first if available assuming these values
			// are not likely to change within the same session. If changed, the
			// session should be updated.
			LookupService lookupService = new LookupService();
			ManageAliquotService manageAliquotService = new ManageAliquotService();

			if (session.getAttribute("allSampleIds") == null) {
				List sampleIds = lookupService.getAllSampleIds();
				session.setAttribute("allSampleIds", sampleIds);
			}
			if (session.getAttribute("allLotIds") == null) {
				List lotIds = lookupService.getAllLotIds();
				session.setAttribute("allLotIds", lotIds);
			}
			if (session.getAttribute("allAliquotIds") == null) {
				List aliquotIds = lookupService.getAliquots();
				session.setAttribute("allAliquotIds", aliquotIds);
			}
			if (session.getAttribute("aliquotContainerInfo") == null) {
				ContainerInfoBean containerInfo = lookupService
						.getAliquotContainerInfo();
				session.setAttribute("aliquotContainerInfo", containerInfo);
			}
			if (session.getAttribute("aliquotCreateMethods") == null) {
				List methods = manageAliquotService.getAliquotCreateMethods();
				session.setAttribute("aliquotCreateMethods", methods);
			}
			String sampleId = (String) theForm.get("sampleId");
			String lotId = (String) theForm.get("lotId");
			String parentAliquotId = (String) theForm.get("parentAliquotId");
			String numberOfAliquots = (String) theForm.get("numberOfAliquots");
			int numAliquots;
			if (numberOfAliquots.length() == 0) {
				numAliquots = 0;
			} else {
				numAliquots = Integer.parseInt(numberOfAliquots);
			}

			AliquotBean template = (AliquotBean) theForm.get("template");

			// calculate number of rows in the matrix
			if (numAliquots > 0) {
				int colNum = manageAliquotService
						.getDefaultAliquotMatrixColumnNumber();
				int rowNum = (int) Math.ceil((float) numAliquots / colNum);

				// calculate the first aliquot Id to use
				int firstAliquotNum = manageAliquotService.getFirstAliquotNum(
						sampleId, lotId, parentAliquotId);
				String aliquotPrefix = manageAliquotService.getAliquotPrefix(
						sampleId, lotId, parentAliquotId);
				// create a 2-D matrix for aliquot
				List<AliquotBean[]> aliquotMatrix = createAliquotMatrix(colNum,
						rowNum, numAliquots, aliquotPrefix, firstAliquotNum, template);
				session.setAttribute("aliquotMatrix", aliquotMatrix);
			}
			else {
				session.removeAttribute("aliquotMatrix");
			}
			// TODO fill in details to save the data through some service
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessages errors=new ActionMessages();
			ActionMessage error=new ActionMessage("error.preCreateAliquot");
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error("Caught exception loading creating aliquot page", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}

	private int getFirstAliquotId(String sampleId, String lotId,
			String parentAliquotId) {
		int firstId = 0;
		return firstId;
	}

	/**
	 * 
	 * @return a 2-D matrix of aliquots
	 */
	private List<AliquotBean[]> createAliquotMatrix(int colNum, int rowNum,
			int numAliquots, String aliquotPrefix, int firstAliquotId, AliquotBean template) {

		List<AliquotBean[]> aliquotMatrix = new ArrayList();
		int aliquotId = firstAliquotId;
		for (int i = 0; i < rowNum; i++) {
			// calculate number of columsn per row
			int cols = colNum;
			if (numAliquots % colNum < colNum && numAliquots % colNum > 0
					&& i == rowNum - 1) {
				cols = numAliquots % colNum;
			}
			AliquotBean[] aliquotRow = new AliquotBean[colNum];

			for (int j = 0; j < cols; j++) {
				AliquotBean aliquot = new AliquotBean(aliquotPrefix+aliquotId, template
						.getContainer(), template.getHowCreated());
				aliquotRow[j] = aliquot;
				aliquotId++;
			}
			aliquotMatrix.add(aliquotRow);
		}

		return aliquotMatrix;
	}
}
