package gov.nih.nci.cananolab.service.curation;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.CurationException;

import java.util.List;

public interface CurationService {
	public void submitDataForReview() throws CurationException;

	public List<DataReviewStatusBean> findDataPendingReview()
			throws CurationException;
}