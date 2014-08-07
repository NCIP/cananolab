package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpSession;

public class ProtocolUtil {

	public static Map<String, Object> reformatLocalSearchDropdownsInSession(
			HttpSession session) {
        if (session == null) 
        	return null;
        
		Map<String, Object> typeMap = new HashMap<String, Object>();
		
		SortedSet<String> types = (SortedSet<String>) session.getAttribute("publicationCategories");
		if (types != null) 
			typeMap.put("publicationCategories", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("publicationStatuses");
		if (types != null) 
			typeMap.put("publicationStatuses", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("publicationResearchAreas");
		if (types != null) 
			typeMap.put("publicationResearchAreas", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("functionTypes");
		if (types != null) 
			typeMap.put("functionTypes", new ArrayList<String>(types));
			
		types = (SortedSet<String>) session.getAttribute("nanomaterialEntityTypes");
		if (types != null) 
			typeMap.put("nanomaterialEntityTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("functionalizingEntityTypes");
		if (types != null) 
			typeMap.put("functionalizingEntityTypes", new ArrayList<String>(types));
		
		Map<String, String> csmRoles = new HashMap<String, String>();
				
		csmRoles.put(AccessibilityBean.CSM_READ_ROLE, AccessibilityBean.R_ROLE_DISPLAY_NAME);
		csmRoles.put(AccessibilityBean.CSM_CURD_ROLE, AccessibilityBean.CURD_ROLE_DISPLAY_NAME);
		if (types != null) 
			typeMap.put("csmRoleNames", csmRoles);
		
		return typeMap;
	}

	
}
