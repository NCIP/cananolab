package gov.nih.nci.calab.ui.inventory;

/**
 * This class prepares data to prepopulate the view create aliquot. 
 * 
 * @author pansu
 */

/* CVS $Id: PreCreateAliquotAction.java,v 1.1 2006-06-30 20:56:09 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.service.inventory.ManageAliquotService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class PreCreateAliquotAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		boolean fromAliquot = ((String) theForm.get("fromAliquot")).equals("true") ? true
				: false;
		String sampleName = (String) theForm.get("sampleName");
		String parentAliquotName = (String) theForm.get("parentAliquotName");
		String parentName=(fromAliquot)?parentAliquotName:sampleName;

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
					fromAliquot, parentName);
			String aliquotPrefix = manageAliquotService.getAliquotPrefix(parentName);

			// get user and date from session
			String creator = (String) session.getAttribute("creator");			

			// create a 2-D matrix for aliquot
			List<AliquotBean[]> aliquotMatrix = createAliquotMatrix(colNum,
					rowNum, numAliquots, aliquotPrefix, firstAliquotNum,
					template, creator);
			session.setAttribute("aliquotMatrix", aliquotMatrix);
		} else {
			session.removeAttribute("aliquotMatrix");
		}
		forward = mapping.findForward("success");

		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	/**
	 * 
	 * @return a 2-D matrix of aliquots
	 */
	private List<AliquotBean[]> createAliquotMatrix(int colNum, int rowNum,
			int numAliquots, String aliquotPrefix, int firstAliquotName,
			AliquotBean template, String aliquotCreator) {

		List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>();
		int aliquotName = firstAliquotName;
		for (int i = 0; i < rowNum; i++) {
			// calculate number of columsn per row
			int cols = colNum;
			if (numAliquots % colNum < colNum && numAliquots % colNum > 0
					&& i == rowNum - 1) {
				cols = numAliquots % colNum;
			}
			AliquotBean[] aliquotRow = new AliquotBean[colNum];

			for (int j = 0; j < cols; j++) {
				AliquotBean aliquot = new AliquotBean(aliquotPrefix
						+ aliquotName, template.getContainer(), template
						.getHowCreated(), aliquotCreator, new Date());
				aliquotRow[j] = aliquot;
				aliquotName++;
			}
			aliquotMatrix.add(aliquotRow);
		}

		return aliquotMatrix;
	}
}
