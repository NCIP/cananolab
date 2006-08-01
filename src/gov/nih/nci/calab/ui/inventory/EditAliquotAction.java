package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user edited aliquot data. 
 * 
 * @author pansu
 */

/* CVS $Id: EditAliquotAction.java,v 1.2 2006-08-01 13:25:27 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EditAliquotAction extends AbstractDispatchAction {
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AliquotBean aliquot = (AliquotBean) theForm.get("aliquot");
		aliquot.setCreationDate(new Date());
		int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
		int colNum = Integer.parseInt((String) theForm.get("colNum"));
		if (session.getAttribute("aliquotMatrix") != null) {
			List aliquotMatrx = (List) session.getAttribute("aliquotMatrix");
			// replace the aliquot in the matrix and reset the session variable
			((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum] = aliquot;
			forward = mapping.findForward("success");
		} else {
			throw new CalabException(
					"Session containing the aliquot matrix either is expired or doesn't exist");			
		}
		return forward;
	}
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
		int colNum = Integer.parseInt((String) theForm.get("colNum"));
		if (session.getAttribute("aliquotMatrix") != null) {
			List aliquotMatrx = (List) session.getAttribute("aliquotMatrix");
			AliquotBean aliquot = ((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum];
			theForm.set("aliquot", aliquot);
			forward = mapping.getInputForward();
		} else {
			throw new CalabException(
					"Session containing the aliquot matrix either is expired or doesn't exist");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
