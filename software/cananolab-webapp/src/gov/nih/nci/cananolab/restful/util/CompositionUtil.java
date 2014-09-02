package gov.nih.nci.cananolab.restful.util;

import edu.emory.mathcs.backport.java.util.Arrays;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.util.SampleConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class CompositionUtil {
	public static Map<String, Object> reformatLocalSearchDropdownsInSession(
			HttpSession session) {
        if (session == null)                      
        	return null;
        ServletContext appContext = session.getServletContext();
		Map<String, Object> typeMap = new HashMap<String, Object>();
		
		SortedSet<String> types = (SortedSet<String>) session.getAttribute("nanomaterialEntityTypes");
					
		if (types != null) 
			typeMap.put("nanomaterialEntityTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("composingElementTypes");
		if (types != null) 
			typeMap.put("composingElementTypes", new ArrayList<String>(types));
		
		List<String> list = (List<String>) session.getAttribute("emulsionComposingElementTypes");
		if (list != null) 
			typeMap.put("emulsionComposingElementTypes", list);
		
		types = (SortedSet<String>) session.getAttribute("composingElementUnits");
		if (types != null) 
			typeMap.put("composingElementUnits", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("molecularFormulaTypes");
		if (types != null) 
			typeMap.put("molecularFormulaTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("functionTypes");
		if (types != null) 
			typeMap.put("functionTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("modalityTypes");
		if (types != null) 
			typeMap.put("modalityTypes", new ArrayList<String>(types));
		
		types = (SortedSet<String>) session.getAttribute("fileTypes");
		if (types != null) 
			typeMap.put("fileTypes", new ArrayList<String>(types));
		
		list = (List<String>) session.getAttribute("otherSampleNames");
		if (list != null) 
			typeMap.put("otherSampleNames", list);
		
		List<String> pubChemSources = Arrays.asList(SampleConstants.PUBCHEM_DS_LIST);  
		if(pubChemSources != null)
			typeMap.put("pubChemDataSources", pubChemSources);
		
		types = (SortedSet<String>) session.getAttribute("dimensionUnits");
		if (types != null) 
			typeMap.put("dimensionUnits", new ArrayList<String>(types));         
		
		types = (SortedSet<String>) session.getAttribute("biopolymerTypes");
		if (types != null) 
			typeMap.put("biopolymerTypes", new ArrayList<String>(types));
				
		types = (SortedSet<String>) appContext.getAttribute("wallTypes");
		if (types != null) 
			typeMap.put("wallTypes", new ArrayList<String>(types));
		return typeMap;
	}
}
