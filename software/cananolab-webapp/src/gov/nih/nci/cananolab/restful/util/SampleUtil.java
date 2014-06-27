package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;

import java.util.ArrayList;
import java.util.HashMap;
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

}
