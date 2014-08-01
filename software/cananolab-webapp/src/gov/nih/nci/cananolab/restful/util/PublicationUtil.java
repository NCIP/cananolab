package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class PublicationUtil {

	public static Map<String, List<String>> reformatLocalSearchDropdownsInSession(
			HttpSession session) {
        if (session == null) 
        	return null;
        
		Map<String, List<String>> typeMap = new HashMap<String, List<String>>();
		
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
		
		List<String> csmRoles = new ArrayList<String>();
		csmRoles.add(AccessibilityBean.R_ROLE_DISPLAY_NAME);
		csmRoles.add(AccessibilityBean.CURD_ROLE_DISPLAY_NAME);
		if (types != null) 
			typeMap.put("csmRoleNames", csmRoles);
		
		return typeMap;
	}

	
}
