package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRSampleManager {

	public DWRSampleManager() {}
	
	public String[] removeSourceVisibility(String sampleSource) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		
		try {
			SortedSet<String> visibilityGroup = 
				InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
			visibilityGroup.remove(sampleSource);
			String[] eleArray = new String[visibilityGroup.size()];
			return visibilityGroup.toArray(eleArray);
			
		} catch (Exception e) {
			System.out.println("removeSourceVisibility exception.");
			e.printStackTrace();
		}
		
		return new String[] { "" };
	}
}
