package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

public class CommonServiceHelper {

	public List<String> findSharedProtocols(String loginName) {
		List<String> data = new ArrayList<String>();
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
		String query = "SELECT DISTINCT p.protocol_pk_id "
				+ "FROM csm_user_group_role_pg ugrp, "
				+ "protocol p, "
				+ "csm_protection_group pg, "
				+ "csm_user u, "
				+ "csm_role r "
				+ "WHERE     ugrp.protection_group_id = pg.protection_group_id "
				+ "AND ugrp.role_id = r.role_id "
				+ "AND p.protocol_pk_id = pg.protection_group_name "
				+ "AND ugrp.user_id = u.user_id " + "AND u.login_name = '"
				+ loginName + "' "
				+ "AND r.role_name IN ('"
				+ AccessibilityBean.CSM_READ_ROLE
				+ "', '"
				+ AccessibilityBean.CSM_CUR_ROLE
				+ "', '"
				+ AccessibilityBean.CSM_CURD_ROLE + "')";
		
		String[] columns = new String[] { "protocol_pk_id" };
		Object[] columnTypes = new Object[] { Hibernate.STRING };
		List results = appService.directSQL(query, columns, columnTypes);
		for (Object obj : results) {
			if (obj != null) {
				data.add(((String) obj));
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		return data;
	}

	public List<String> findSharedPublications(String loginName) {
		List<String> data = new ArrayList<String>();
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
		String query = "SELECT DISTINCT p.publication_pk_id "
				+ "FROM csm_user_group_role_pg ugrp, "
				+ "publication p, "
				+ "csm_protection_group pg, "
				+ "csm_user u, "
				+ "csm_role r "
				+ "WHERE     ugrp.protection_group_id = pg.protection_group_id "
				+ "AND ugrp.role_id = r.role_id "
				+ "AND p.publication_pk_id = pg.protection_group_name "
				+ "AND ugrp.user_id = u.user_id " + "AND u.login_name = '"
				+ loginName + "' "
				+ "AND r.role_name IN ('"
				+ AccessibilityBean.CSM_READ_ROLE
				+ "', '"
				+ AccessibilityBean.CSM_CUR_ROLE
				+ "', '"
				+ AccessibilityBean.CSM_CURD_ROLE + "')";
		
		String[] columns = new String[] { "publication_pk_id" };
		Object[] columnTypes = new Object[] { Hibernate.STRING };
		List results = appService.directSQL(query, columns, columnTypes);
		for (Object obj : results) {
			if (obj != null) {
				data.add(((String) obj));
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		return data;
	}
	
	public List<String> findSharedSampleIds(String loginName) 
			throws Exception {
		List<String> data = new ArrayList<String>();

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String query = "SELECT DISTINCT s.sample_pk_id "
				+ "FROM csm_user_group_role_pg ugrp, "
				+ "sample s, "
				+ "csm_protection_group pg, "
				+ "csm_user u, "
				+ "csm_role r "
				+ "WHERE ugrp.protection_group_id = pg.protection_group_id "
				+ "AND ugrp.role_id = r.role_id "
				+ "AND s.sample_pk_id = pg.protection_group_name "
				+ "AND ugrp.user_id = u.user_id " + "AND u.login_name = '"
				+ loginName + "' ";

		String[] columns = new String[] { "sample_pk_id" };
		Object[] columnTypes = new Object[] { Hibernate.STRING };
		List results = appService.directSQL(query, columns, columnTypes);
		for (Object obj : results) {
			if (obj != null) {
				data.add(((String) obj));
			}
		}

		return data;
	}
}
