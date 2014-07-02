package gov.nih.nci.cananolab.restful.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpSession;


public class SampleUtil {
	
	public static Map<String, List<String>> reformatLocalSearchDropdownsInSession(HttpSession session) {
		
		if (session == null) return null;
		
		Map<String, List<String>> typeMap = new HashMap<String, List<String>>();
		
		//Attrs set from InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		SortedSet<String> types = (SortedSet<String>) session.getAttribute("functionTypes");
		if (types != null) 
			typeMap.put("functionTypes", new ArrayList<String>(types));
			
		types = (SortedSet<String>) session.getAttribute("nanomaterialEntityTypes");
		if (types != null) 
			typeMap.put("nanomaterialEntityTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("functionalizingEntityTypes");
		if (types != null) 
			typeMap.put("functionalizingEntityTypes", new ArrayList<String>(types));
		
		List<String> characterTyeps = (List<String>) session.getAttribute("characterizationTypes");
		if (types != null) 
			typeMap.put("characterizationTypes", new ArrayList<String>(characterTyeps));
		
		return typeMap;
	}
	
	public static String[] getDefaultListFromSessionByType(String sessionKey, HttpSession session) {
		
		if (session == null)
			return new String[1];
		
		SortedSet<String> types = (SortedSet<String>) session.getAttribute(sessionKey);
		if (types == null || types.size() ==0)
			return new String[1];
		
		String[] defaultTypes = new String[types.size()];
		
		Iterator ite = types.iterator();
		int idx = 0;
		while (ite.hasNext()) {
			defaultTypes[idx++] = (String)ite.next();
		}
		
		return defaultTypes;
	}

}
