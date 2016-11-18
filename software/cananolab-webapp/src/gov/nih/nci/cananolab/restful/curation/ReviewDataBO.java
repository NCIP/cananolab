package gov.nih.nci.cananolab.restful.curation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.ui.form.ReviewDataForm;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("reviewDataBO")
public class ReviewDataBO extends AbstractDispatchBO
{
	@Autowired
	private CurationService curationServiceDAO;

	public List<DataReviewStatusBean> setupNew(HttpServletRequest request) throws Exception
	{
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<DataReviewStatusBean> dataPendingReview = curationServiceDAO.findDataPendingReview();
		request.setAttribute("dataPendingReview", dataPendingReview);
		//return mapping.findForward("setup");
		return dataPendingReview;
	}

	public void input(ReviewDataForm form, HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	return mapping.findForward("setup");
	}
	
}
