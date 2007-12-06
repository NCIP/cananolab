package gov.nih.nci.calab.ui.sample;

/**
 * This class prepares data to show sample detail page after sample search. 
 * 
 * @author pansu
 */

/* CVS $Id: ViewAliquotDetailAction.java,v 1.4 2007-12-06 09:01:44 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.SampleException;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ViewAliquotDetailAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaActionForm theForm = (DynaActionForm) form;
		String aliquotId = (String) theForm.get("aliquotId");
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
			throw new SampleException(
					"Session containing the searched aliquot results either is expired or doesn't exist when viewing aliquot details");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
