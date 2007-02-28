package gov.nih.nci.calab.ui.inventory;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewAliquotDetailAction.java,v 1.1 2007-02-28 21:54:10 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.exception.CalabException;
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
import org.apache.struts.action.DynaActionForm;

public class ViewAliquotDetailAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(ViewAliquotDetailAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaActionForm theForm = (DynaActionForm) form;
		String aliquotId = (String) theForm.get("aliquotId");
		
		ActionMessages messages = new ActionMessages();
		if (session.getAttribute("aliquots") != null) {
			List<AliquotBean> aliquots = new ArrayList<AliquotBean>(
					(List<? extends AliquotBean>) session
							.getAttribute("aliquots"));
			for (AliquotBean aliquot : aliquots) {
				if (aliquot.getAliquotId().equals(aliquotId)) {
					request.setAttribute("aliquot", aliquot);
				}
			}
			forward = mapping.findForward("success");
		} else {
			logger
					.error("Session containing the searched aliquot results either is expired or doesn't exist");
			ActionMessage error = new ActionMessage(
					"error.viewAliquotDetails.noaliquots");
			messages.add("error", error);
			saveMessages(request, messages);

			throw new CalabException(
					"Session containing the searched aliquot results either is expired or doesn't exist");
		}
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}
}
