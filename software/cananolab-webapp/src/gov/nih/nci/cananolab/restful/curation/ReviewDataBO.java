package gov.nih.nci.cananolab.restful.curation;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.ReviewDataForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class ReviewDataBO extends AbstractDispatchBO{
	private CurationService curationService;

	public CurationService getCurationService() {
		return curationService;
	}

	public void setCurationService(CurationService curationService) {
		this.curationService = curationService;
	}

	public List<DataReviewStatusBean> setupNew(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<DataReviewStatusBean> dataPendingReview = curationService
				.findDataPendingReview(securityService);
		request.setAttribute("dataPendingReview", dataPendingReview);
		//return mapping.findForward("setup");
		return dataPendingReview;
	}

	public void input(ReviewDataForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	return mapping.findForward("setup");
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		return user.isCurator();
	}
}
