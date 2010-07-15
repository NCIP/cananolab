package gov.nih.nci.cananolab.service.curation.impl;

import java.util.List;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.CurationException;
import gov.nih.nci.cananolab.service.curation.CurationService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class CurationServiceJDBCImpl extends JdbcDaoSupport implements
		CurationService {

	public List<DataReviewStatusBean> findDataPendingReview()
			throws CurationException {
		JdbcTemplate template = this.getJdbcTemplate();
		// TODO Auto-generated method stub
		return null;
	}

	public void submitDataForReview() throws CurationException {
		// TODO Auto-generated method stub

	}

}