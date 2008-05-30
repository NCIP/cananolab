package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRSampleManager {

	Logger logger = Logger.getLogger(DWRSampleManager.class);
	private NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
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
	
	public String[] getNanoparticleEntityTypes(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if ("local".equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
		    if (isLocal){
		    	types = InitCompositionSetup.getInstance().getNanoparticleEntityTypes(request);
			}else{
				types = InitCompositionSetup.getInstance().getDefaultNanoparticleEntityTypes(request);
			}
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting nanoparticle entity types: \n", e);
			e.printStackTrace();
		}	
		return new String[] { "" };
	}
	
	public String[] getFunctionalizingEntityTypes(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if ("local".equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
		    if (isLocal){
		    	types = InitCompositionSetup.getInstance().getFunctionalizingEntityTypes(request);
			}else{
				types = InitCompositionSetup.getInstance().getDefaultFunctionalizingEntityTypes(request);
			}
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting functionalizing entity types: \n", e);
			e.printStackTrace();
		}	
		return new String[] { "" };
	}
	
	public String[] getFunctionTypes(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if ("local".equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
		    if (isLocal){
		    	types = InitCompositionSetup.getInstance().getFunctionTypes(request);
			}else{
				types = InitCompositionSetup.getInstance().getDefaultFunctionTypes(request);
			}
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting function types: \n", e);
			e.printStackTrace();
		}	
		return new String[] { "" };
	}
}
