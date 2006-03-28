package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares data to prepopulate the view create aliquot. 
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateAliquotAction.java,v 1.10 2006-03-28 23:11:05 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.service.administration.ManageAliquotService;
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
import org.apache.struts.validator.DynaValidatorForm;

public class PreCreateAliquotAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(PreCreateAliquotAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		try {
			// TODO fill in details for sample information */
			DynaValidatorForm theForm = (DynaValidatorForm) form;

			String sampleId = (String) theForm.get("sampleId");
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

				
				ManageAliquotService manageAliquotService = new ManageAliquotService();
				int colNum = manageAliquotService
						.getDefaultAliquotMatrixColumnNumber();
				int rowNum = (int) Math.ceil((float) numAliquots / colNum);

				// calculate the first aliquot Id to use
				int firstAliquotNum = manageAliquotService.getFirstAliquotNum(
						sampleId, parentAliquotId);
				String aliquotPrefix = manageAliquotService.getAliquotPrefix(
						sampleId, parentAliquotId);
				
				//get user and date from session
				String creator=(String)session.getAttribute("creator");
				String creationDate=(String)session.getAttribute("creationDate");
                
				// create a 2-D matrix for aliquot
				List<AliquotBean[]> aliquotMatrix = createAliquotMatrix(colNum,
						rowNum, numAliquots, aliquotPrefix, firstAliquotNum,
						template, creator, creationDate);
				session.setAttribute("aliquotMatrix", aliquotMatrix);
			} else {
				session.removeAttribute("aliquotMatrix");
			}
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.preCreateAliquot");
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

	/**
	 * 
	 * @return a 2-D matrix of aliquots
	 */
	private List<AliquotBean[]> createAliquotMatrix(int colNum, int rowNum,
			int numAliquots, String aliquotPrefix, int firstAliquotId,
			AliquotBean template, String aliquotCreator, String creationDate) {

		List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>();
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
				AliquotBean aliquot = new AliquotBean(
						aliquotPrefix + aliquotId, template.getContainer(),
						template.getHowCreated(), aliquotCreator, creationDate);
				aliquotRow[j] = aliquot;
				aliquotId++;
			}
			aliquotMatrix.add(aliquotRow);
		}

		return aliquotMatrix;
	}
}
