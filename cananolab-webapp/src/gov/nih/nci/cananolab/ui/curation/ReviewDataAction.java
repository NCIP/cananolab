package gov.nih.nci.cananolab.ui.curation;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class ReviewDataAction extends AbstractDispatchAction {
	private CurationService curationService;

	public CurationService getCurationService() {
		return curationService;
	}

	public void setCurationService(CurationService curationService) {
		this.curationService = curationService;
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<DataReviewStatusBean> dataPendingReview = curationService
				.findDataPendingReview();
		request.setAttribute("dataPendingReview", dataPendingReview);
		return mapping.findForward("setup");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		return mapping.findForward("setup");
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_CURATION);
	}
}
