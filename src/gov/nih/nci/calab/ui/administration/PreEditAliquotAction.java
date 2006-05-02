package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares data to prepopulate the view edit aliquot. 
 * 
 * @author pansu
 */

/* CVS $Id: PreEditAliquotAction.java,v 1.4 2006-05-02 22:27:17 pansu Exp $ */

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

public class PreEditAliquotAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
		int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
		int colNum = Integer.parseInt((String) theForm.get("colNum"));
		if (session.getAttribute("aliquotMatrix") != null) {
			List aliquotMatrx = (List) session.getAttribute("aliquotMatrix");
			AliquotBean aliquot = ((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum];
			theForm.set("aliquot", aliquot);
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
