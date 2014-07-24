package gov.nih.nci.cananolab.restful.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

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
		
		return typeMap;
	}

	
}
