package gov.nih.nci.cananolab.service.curation.impl;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.exception.CurationException;
import gov.nih.nci.cananolab.service.curation.CurationService;

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
	private static final String PENDING_STATUS = "pending";

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

	public List<DataReviewStatusBean> findDataPendingReview()
			throws CurationException {
		List<DataReviewStatusBean> pendingDataList = null;
		try {
			JdbcTemplate template = this.getJdbcTemplate();
			String sql = "select " + REVIEW_STATUS_TABLE_ALL_COLS + " from "
					+ REVIEW_STATUS_TABLE + " where "
					+ REVIEW_STATUS_TABLE_STATUS_COL + "=?";
			Object[] args = { PENDING_STATUS };
			pendingDataList = template.query(sql, args,
					getDataReviewStatusRowMapper());
		} catch (Exception e) {
			String error = "Error retrieving data review status: ";
			throw new CurationException(error, e);
		}
		return pendingDataList;
	}

	private DataReviewStatusBean findDataReviewStatusBeanByDataId(String dataId)
			throws SQLException {
		DataReviewStatusBean dataReviewStatusBean = null;

		JdbcTemplate template = this.getJdbcTemplate();
		String sql = "select " + REVIEW_STATUS_TABLE_ALL_COLS + "from "
				+ REVIEW_STATUS_TABLE + " where status=?";
		Object[] args = { PENDING_STATUS };
		List result = template.query(sql, args, getDataReviewStatusRowMapper());
		for (int i = 0; i < result.size(); i++) {
			dataReviewStatusBean = (DataReviewStatusBean) result.get(0);
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

	public void submitDataForReview(DataReviewStatusBean dataReviewStatusBean)
			throws CurationException {
		try {
			DataReviewStatusBean existingBean = findDataReviewStatusBeanByDataId(dataReviewStatusBean
					.getDataId());
			if (existingBean != null) {
				this.updateDataReviewStatusBean(dataReviewStatusBean);
			} else {
				this.insertDataReviewStatusBean(dataReviewStatusBean);
			}
		} catch (Exception e) {
			String error = "Error in submitting data for curator review";
			throw new CurationException(error, e);
		}
	}
}