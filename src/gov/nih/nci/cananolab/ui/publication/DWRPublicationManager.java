package gov.nih.nci.cananolab.ui.publication;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPublicationManager {

	Logger logger = Logger.getLogger(DWRPublicationManager.class);
	public DWRPublicationManager() {}



	public String[] getPublicationCategories(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if (Constants.LOCAL_SITE.equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal){
				types = InitSetup.getInstance()
					.getDefaultAndOtherLookupTypes(request, "publicationCategories",
						"Publication", "category", "otherCategory", true);
			}else{
				types = LookupService.findLookupValues("Publication", "category");
			}
		    types.add("");
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getPublicationStatuses(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if (Constants.LOCAL_SITE.equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal){
				types = InitSetup.getInstance()
					.getDefaultAndOtherLookupTypes(request, "publicationStatuses",
						"Publication", "status", "otherStatus", true);
			}else{
				types = LookupService.findLookupValues("Publication", "status");
			}
		    types.add("");
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication statuses: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}
}