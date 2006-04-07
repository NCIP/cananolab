package gov.nih.nci.calab.ui.administration;

/**
 * This class prepares data to prepopulate the view edit aliquot. 
 * 
 * @author pansu
 */

/* CVS $Id: PreEditAliquotAction.java,v 1.3 2006-04-07 15:29:37 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

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

public class PreEditAliquotAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(PreEditAliquotAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ActionMessages messages = new ActionMessages();
		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
			int colNum = Integer.parseInt((String) theForm.get("colNum"));
			if (session.getAttribute("aliquotMatrix") != null) {
				List aliquotMatrx = (List) session
						.getAttribute("aliquotMatrix");
				AliquotBean aliquot = ((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum];
				theForm.set("aliquot", aliquot);
				forward = mapping.findForward("success");
			} else {
				logger
						.error("Session containing the aliquot matrix either is expired or doesn't exist");
				ActionMessage error = new ActionMessage(
						"errors.editAliquot.nomatrix");
				messages.add("error", error);
				saveMessages(request, messages);
				forward = mapping.getInputForward();
			}

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.preEditAliquot");
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error("Caught exception when showing edit aliquot page", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
