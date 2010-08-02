package gov.nih.nci.cananolab.service.curation.impl;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.CurationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class CurationServiceJDBCImpl extends JdbcDaoSupport implements
		CurationService {
	private static final String REVIEW_STATUS_TABLE = "canano.data_review_status";
	private static final String REVIEW_STATUS_TABLE_DATA_ID_COL = "data_id";
	private static final String REVIEW_STATUS_TABLE_DATA_NAME_COL = "data_name";
	private static final String REVIEW_STATUS_TABLE_DATA_TYPE_COL = "data_type";
	private static final String REVIEW_STATUS_TABLE_STATUS_COL = "status";
	private static final String REVIEW_STATUS_TABLE_SUBMITTED_BY_COL = "submitted_by";
	private static final String REVIEW_STATUS_TABLE_SUBMITTED_DATE_COL = "submitted_date";
	private static final String REVIEW_STATUS_TABLE_ALL_COLS = REVIEW_STATUS_TABLE_DATA_ID_COL
			+ ", "
			+ REVIEW_STATUS_TABLE_DATA_NAME_COL
			+ ", "
			+ REVIEW_STATUS_TABLE_DATA_TYPE_COL
			+ ", "
			+ REVIEW_STATUS_TABLE_STATUS_COL
			+ ", "
			+ REVIEW_STATUS_TABLE_SUBMITTED_BY_COL
			+ ", "
			+ REVIEW_STATUS_TABLE_SUBMITTED_DATE_COL;

	private ParameterizedRowMapper<DataReviewStatusBean> dataReviewStatusRowMapper;

	public ParameterizedRowMapper<DataReviewStatusBean> getDataReviewStatusRowMapper()
			throws SQLException {
		dataReviewStatusRowMapper = new ParameterizedRowMapper<DataReviewStatusBean>() {
			public DataReviewStatusBean mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				DataReviewStatusBean dataStatusBean = new DataReviewStatusBean();
				dataStatusBean.setDataId(rs
						.getString(REVIEW_STATUS_TABLE_DATA_ID_COL));
				dataStatusBean.setDataName(rs
						.getString(REVIEW_STATUS_TABLE_DATA_NAME_COL));
				dataStatusBean.setDataType(rs
						.getString(REVIEW_STATUS_TABLE_DATA_TYPE_COL));
				dataStatusBean.setReviewStatus(rs
						.getString(REVIEW_STATUS_TABLE_STATUS_COL));
				dataStatusBean.setSubmittedBy(rs
						.getString(REVIEW_STATUS_TABLE_SUBMITTED_BY_COL));
				dataStatusBean.setSubmittedDate(rs
						.getDate(REVIEW_STATUS_TABLE_SUBMITTED_DATE_COL));
				return dataStatusBean;
			}
		};
		return dataReviewStatusRowMapper;
	}

	public List<DataReviewStatusBean> findDataPendingReview(
			SecurityService securityService) throws CurationException,
			NoAccessException {
		List<DataReviewStatusBean> pendingDataList = null;
		if (!securityService.getUserBean().isCurator()) {
			throw new NoAccessException();
		}
		try {
			JdbcTemplate template = this.getJdbcTemplate();
			String sql = "select " + REVIEW_STATUS_TABLE_ALL_COLS + " from "
					+ REVIEW_STATUS_TABLE + " where "
					+ REVIEW_STATUS_TABLE_STATUS_COL + "=?";
			Object[] args = { DataReviewStatusBean.PENDING_STATUS };
			pendingDataList = template.query(sql, args,
					getDataReviewStatusRowMapper());
		} catch (Exception e) {
			String error = "Error retrieving data review status: ";
			throw new CurationException(error, e);
		}
		return pendingDataList;
	}

	public DataReviewStatusBean findDataReviewStatusBeanByDataId(String dataId,
			SecurityService securityService) throws CurationException,
			NoAccessException {
		DataReviewStatusBean dataReviewStatusBean = null;
		try {
			if (!securityService.checkCreatePermission(dataId)) {
				throw new NoAccessException();
			}
			JdbcTemplate template = this.getJdbcTemplate();
			String sql = "select " + REVIEW_STATUS_TABLE_ALL_COLS + " from "
					+ REVIEW_STATUS_TABLE + " where data_id=?";
			Object[] args = { dataId };
			List result = template.query(sql, args,
					getDataReviewStatusRowMapper());
			for (int i = 0; i < result.size(); i++) {
				dataReviewStatusBean = (DataReviewStatusBean) result.get(0);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error finding existing pending for review data by Id";
			throw new CurationException(error, e);
		}
		return dataReviewStatusBean;
	}

	private int insertDataReviewStatusBean(
			DataReviewStatusBean dataReviewStatusBean) {
		String sql = "insert into " + REVIEW_STATUS_TABLE + "("
				+ REVIEW_STATUS_TABLE_ALL_COLS + ") values(?,?,?,?,?,?)";
		Object[] args = { dataReviewStatusBean.getDataId(),
				dataReviewStatusBean.getDataName(),
				dataReviewStatusBean.getDataType(),
				dataReviewStatusBean.getReviewStatus(),
				dataReviewStatusBean.getSubmittedBy(),
				dataReviewStatusBean.getSubmittedDate() };
		int status = this.getJdbcTemplate().update(sql, args);
		return status;
	}

	private int updateDataReviewStatusBean(
			DataReviewStatusBean dataReviewStatusBean) {
		String sql = "update " + REVIEW_STATUS_TABLE + " set "
				+ REVIEW_STATUS_TABLE_DATA_NAME_COL + "= ?, "
				+ REVIEW_STATUS_TABLE_STATUS_COL + "=? where "
				+ REVIEW_STATUS_TABLE_DATA_ID_COL + "= ?";

		Object[] args = { dataReviewStatusBean.getDataName(),
				dataReviewStatusBean.getReviewStatus(),
				dataReviewStatusBean.getDataId() };
		int status = this.getJdbcTemplate().update(sql, args);
		return status;
	}

	public void submitDataForReview(DataReviewStatusBean dataReviewStatusBean,
			SecurityService securityService) throws CurationException,
			NoAccessException {
		try {
			if (!securityService.checkCreatePermission(dataReviewStatusBean
					.getDataId())) {
				throw new NoAccessException();
			}
			DataReviewStatusBean existingBean = findDataReviewStatusBeanByDataId(
					dataReviewStatusBean.getDataId(), securityService);
			if (existingBean != null) {
				this.updateDataReviewStatusBean(dataReviewStatusBean);
			} else {
				this.insertDataReviewStatusBean(dataReviewStatusBean);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in submitting data for curator review";
			throw new CurationException(error, e);
		}
	}
}