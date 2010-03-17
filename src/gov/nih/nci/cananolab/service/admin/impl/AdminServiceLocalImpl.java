package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.dto.admin.SitePreferenceBean;
import gov.nih.nci.cananolab.dto.admin.VisitorCountBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.admin.AdminService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.List;

import org.hibernate.Hibernate;

/**
 * Service methods involving site preference and visitor counter.
 */
public class AdminServiceLocalImpl implements AdminService {
	//ADMIN_RECORD_ID
	private static final int ADMIN_RECORD_ID = 0;
	
	//SITE_NAME
	private static final String SITE_NAME = "site_name";
	
	//SITE_LOGO
	private static final String SITE_LOGO = "site_logo";
	
	//VISITOR_COUNT
	private static final String VISITOR_COUNT = "visitor_count";
	
	//COUNTER_START_DATE
	private static final String COUNTER_START_DATE = "counter_start_date";
	
	//SELECT_SITE_DATA
	private static String SELECT_SITE_DATA = 
		"select " + SITE_NAME + ',' + SITE_LOGO +
		" from administration where administration_id=" + 
		ADMIN_RECORD_ID;
	
	//SELECT_COUNTER_DATA
	private static String SELECT_COUNTER_DATA = 
		"select " + VISITOR_COUNT + ',' + COUNTER_START_DATE +
		" from administration where administration_id=" + 
		ADMIN_RECORD_ID;
	
	//UPDATE_SITE_PREFERENCE
	private static String UPDATE_SITE_PREFERENCE = 
		"update administration set site_name=?, site_logo=? where administration_id=" + 
		ADMIN_RECORD_ID;
	
	//INCREASE_VISITOR_COUNT
	private static String INCREASE_VISITOR_COUNT = 
		"update administration set visitor_count=visitor_count+1 where administration_id=" + 
		ADMIN_RECORD_ID;
	
	//UPDATE_VISITOR_COUNT
	private static String UPDATE_VISITOR_COUNT = 
		"update administration set visitor_count=?, count_start_date=? where administration_id=" + 
		ADMIN_RECORD_ID;
	
	public SitePreferenceBean getSitePreference(UserBean user) throws Exception {
		if (user == null || !user.isAdmin()) {
			throw new NoAccessException();
		}
		SitePreferenceBean result = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String[] columns = new String[] {SITE_NAME, SITE_LOGO};
		Object[] columnTypes = new Object[] {Hibernate.STRING, Hibernate.STRING};
		List<String> siteData = appService.directSQL(SELECT_SITE_DATA, columns, columnTypes);
		if (siteData != null && !siteData.isEmpty()) {
			result = new SitePreferenceBean();
			Object data = siteData.get(0);
		}
		return result;
	}

	/**
	 * Save Site Preference, including site name and logo file name. 
	 * @param user
	 * 
	 * @throws Exception 
	 */
	public int updateSitePreference(SitePreferenceBean sitePreference, UserBean user) throws Exception {
		if (user == null || !user.isAdmin()) {
			throw new NoAccessException();
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		return 1;
	}

	/**
	 * Increase Visitor Count
	 * 
	 * @throws NoAccessException
	 */
	public int increaseVisitorCount() throws NoAccessException {
		return 1;
	}

	/**
	 * Update Visitor Count
	 * @param user
	 * 
	 * @throws NoAccessException
	 */
	public int updatedVisitorCount(VisitorCountBean newCount, UserBean user) throws NoAccessException {
		if (user == null || !user.isAdmin()) {
			throw new NoAccessException();
		}
		return 1;
	}

	public VisitorCountBean getVisitorCount() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
