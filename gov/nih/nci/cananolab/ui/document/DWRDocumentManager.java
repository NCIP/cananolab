package gov.nih.nci.cananolab.ui.document;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import java.util.SortedSet;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.directwebremoting.impl.DefaultWebContextBuilder;


/**
 * This class create AJAX functions for documents
 * 
 * @author tanq
 * 
 */

public class DWRDocumentManager {

	Logger logger = Logger.getLogger(DWRDocumentManager.class);
	public DWRDocumentManager() {}
	
	public String[] getReportCategories(String searchLocations) {
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
}