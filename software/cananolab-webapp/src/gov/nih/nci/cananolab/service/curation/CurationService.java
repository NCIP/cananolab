package gov.nih.nci.cananolab.service.curation;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.CurationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.List;

public interface CurationService {
	public void submitDataForReview(DataReviewStatusBean dataReviewStatusBean,
			SecurityService securityService) throws CurationException,
			NoAccessException;

	public List<DataReviewStatusBean> findDataPendingReview(
			SecurityService securityService) throws CurationException,
			NoAccessException;

	public DataReviewStatusBean findDataReviewStatusBeanByDataId(String dataId,
			SecurityService securityService) throws CurationException,
			NoAccessException;
}