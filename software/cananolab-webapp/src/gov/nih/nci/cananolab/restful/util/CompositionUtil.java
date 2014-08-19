package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpSession;

public class CompositionUtil {
	public static Map<String, Object> reformatLocalSearchDropdownsInSession(
			HttpSession session) {
        if (session == null) 
        	return null;
        
		Map<String, Object> typeMap = new HashMap<String, Object>();
		
		SortedSet<String> types = (SortedSet<String>) session.getAttribute("publicationCategories");
					
		types = (SortedSet<String>) session.getAttribute("nanomaterialEntityTypes");
		if (types != null) 
			typeMap.put("nanomaterialEntityTypes", new ArrayList<String>(types));
		
		return typeMap;
	}
}
