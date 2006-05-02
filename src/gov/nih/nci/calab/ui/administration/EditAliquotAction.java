package gov.nih.nci.calab.ui.administration;

/**
 * This class saves user edited aliquot data. 
 * 
 * @author pansu
 */

/* CVS $Id: EditAliquotAction.java,v 1.6 2006-05-02 22:27:17 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

public class EditAliquotAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
		AliquotBean aliquot = (AliquotBean) theForm.get("aliquot");
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

	public boolean loginRequired() {
		return true;
	}
}
