package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.dto.admin.SitePreferenceBean;
import gov.nih.nci.cananolab.dto.admin.VisitorCountBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.admin.AdminService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Service methods for site preference and visitor counter.
 * 
 * @author houyh
 */
public class AdminServiceJDBCImpl extends JdbcDaoSupport implements AdminService {
	//ADMIN_RECORD_ID
	private static final int ADMIN_RECORD_ID = 0;
	
	//[administration] table columns.
	private static final String SITE_NAME = "site_name";
	private static final String SITE_LOGO = "site_logo";
	private static final String VISITOR_COUNT = "visitor_count";
	private static final String COUNTER_START_DATE = "counter_start_date";
	private static final String UPDATED_BY = "updated_by";
	private static final String UPDATED_DATE = "updated_date";
	
	private static final String[] SITE_PREFERENCE_COLUMNS = 
		{SITE_NAME, SITE_LOGO, UPDATED_BY, UPDATED_DATE};
	
	private static final String[] VISITOR_COUNT_COLUMNS = 
		{VISITOR_COUNT, COUNTER_START_DATE, UPDATED_BY, UPDATED_DATE};
	
	//SELECT_SITE_PREFERENCE
	private static String SELECT_SITE_PREFERENCE;
	
	//UPDATE_SITE_PREFERENCE
	private static String UPDATE_SITE_PREFERENCE;
	
	//SELECT_VISITOR_COUNT
	private static String SELECT_VISITOR_COUNT;
	
	//UPDATE_VISITOR_COUNT
	private static String UPDATE_VISITOR_COUNT;
	
	//INCREASE_VISITOR_COUNT
	private static String INCREASE_VISITOR_COUNT = 
		"update administration set visitor_count=visitor_count+1 where administration_id=" + 
		ADMIN_RECORD_ID;
	
	//SITE_MAPPER
	private static SitePreferenceMapper SITE_MAPPER = new SitePreferenceMapper();
	
	//COUNTE_MAPPER
	private static VisiterCountMapper COUNTE_MAPPER = new VisiterCountMapper();
	
	static {
		StringBuilder sbSelect = new StringBuilder(512);
		StringBuilder sbUpdate = new StringBuilder(512);
		
		// Build select/update statement for site preference.
		sbSelect.append("select ");
		sbUpdate.append("update administration set ");
		for (String column : SITE_PREFERENCE_COLUMNS) {
			sbSelect.append(column).append(',');
			sbUpdate.append(column).append("=?,");
		}
		sbSelect.deleteCharAt(sbSelect.length() - 1); //remove last ','
		sbSelect.append(" from administration where administration_id=");
		sbSelect.append(ADMIN_RECORD_ID);
		sbUpdate.deleteCharAt(sbUpdate.length() - 1); //remove last ','
		sbUpdate.append(" where administration_id=");
		sbUpdate.append(ADMIN_RECORD_ID);
		SELECT_SITE_PREFERENCE = sbSelect.toString();
		UPDATE_SITE_PREFERENCE = sbUpdate.toString();
		
		// Build select/update statement for visitor count.
		sbSelect.delete(0, sbSelect.length());
		sbUpdate.delete(0, sbUpdate.length());
		sbSelect.append("select ");
		sbUpdate.append("update administration set ");
		for (String column : VISITOR_COUNT_COLUMNS) {
			sbSelect.append(column).append(',');
			sbUpdate.append(column).append("=?,");
		}
		sbSelect.deleteCharAt(sbSelect.length() - 1); //remove last ','
		sbSelect.append(" from administration where administration_id=");
		sbSelect.append(ADMIN_RECORD_ID);
		sbUpdate.deleteCharAt(sbUpdate.length() - 1); //remove last ','
		sbUpdate.append(" where administration_id=");
		sbUpdate.append(ADMIN_RECORD_ID);
		SELECT_VISITOR_COUNT = sbSelect.toString();
		UPDATE_VISITOR_COUNT = sbUpdate.toString();
	}
	
	/**
	 * Get Site Preference, including site name and logo file name. 
	 * 
	 * @throws Exception
	 */
	public SitePreferenceBean getSitePreference() throws Exception {
		SitePreferenceBean result = null;
		JdbcTemplate selectSite = this.getJdbcTemplate();
		result = (SitePreferenceBean) selectSite.queryForObject(
				SELECT_SITE_PREFERENCE, SITE_MAPPER);
		return result;
	}

	/**
	 * Save Site Preference, including site name and logo file name. 
	 * 
	 * @param sitePreference
	 * @param user
	 * @return number of rows updated
	 * @throws Exception
	 */
	public int updateSitePreference(SitePreferenceBean sitePreference, UserBean user) throws NoAccessException {
		if (user == null || !user.isAdmin()) {
			throw new NoAccessException();
		}
		JdbcTemplate saveSite = this.getJdbcTemplate();
		Object[] args = new Object[] {sitePreference.getSiteName(),
			sitePreference.getSiteLogoFilename(),
			sitePreference.getUpdatedBy(), sitePreference.getUpdatedDate()};
		int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR,
			Types.VARCHAR, Types.DATE };
		
		return saveSite.update(UPDATE_SITE_PREFERENCE, args, argTypes);
	}

	/**
	 * Get Visitor Count
	 * 
	 * @throws Exception
	 */
	public VisitorCountBean getVisitorCount() throws Exception {
		VisitorCountBean result = null;
		JdbcTemplate selectSite = this.getJdbcTemplate();
		result = (VisitorCountBean) selectSite.queryForObject(
				SELECT_VISITOR_COUNT, COUNTE_MAPPER);
		
		return result;
	}

	/**
	 * Increase Visitor Count
	 * 
	 * @return number of rows updated
	 * @throws Exception
	 */
	public int increaseVisitorCount() throws NoAccessException {
		JdbcTemplate increaseCounter = this.getJdbcTemplate();
		return increaseCounter.update(INCREASE_VISITOR_COUNT);
	}

	/**
	 * Update Visitor Count
	 * 
	 * @param newCount
	 * @param user
	 * @return number of rows updated
	 * @throws Exception
	 */
	public int updatedVisitorCount(VisitorCountBean newCount, UserBean user) throws NoAccessException {
		if (user == null || !user.isAdmin()) {
			throw new NoAccessException();
		}
		JdbcTemplate saveSite = this.getJdbcTemplate();
		Object[] args = new Object[] {newCount.getVisitorCount(),
				newCount.getCounterStartDate(),
				newCount.getUpdatedBy(), newCount.getUpdatedDate()};
		int[] argTypes = new int[] {Types.INTEGER, Types.DATE,
			Types.VARCHAR, Types.DATE };
		
		return saveSite.update(UPDATE_VISITOR_COUNT, args, argTypes);
	}

	/**
	 * Mapper class for converting one row to one SitePreferenceBean.
	 * 
	 * @author houyh
	 */
	private static final class SitePreferenceMapper implements RowMapper {
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	SitePreferenceBean sitePreference = new SitePreferenceBean();
	    	sitePreference.setSiteName(rs.getString(SITE_NAME));
	    	sitePreference.setSiteLogoFilename(rs.getString(SITE_LOGO));
	    	sitePreference.setUpdatedBy(rs.getString(UPDATED_BY));
	    	sitePreference.setUpdatedDate(rs.getDate(UPDATED_DATE));
	        return sitePreference;
	    }
	}

	/**
	 * Mapper class for converting one row to one VisitorCountBean.
	 * 
	 * @author houyh
	 */
	private static final class VisiterCountMapper implements RowMapper {
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	VisitorCountBean count = new VisitorCountBean();
	    	count.setVisitorCount(rs.getInt(VISITOR_COUNT));
	    	count.setCounterStartDate(rs.getDate(COUNTER_START_DATE));
	    	count.setUpdatedBy(rs.getString(UPDATED_BY));
	    	count.setUpdatedDate(rs.getDate(UPDATED_DATE));
	        return count;
	    }
	}
}
