package gov.nih.nci.cananolab.restful.util;

import javax.servlet.ServletContext;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.service.PublicDataCountJob;

public class InitSetupUtil {

	/**
	 * Moved from ui.core.InitSetup.setPublicCountInContext()
	 * 
	 * @return
	 */
	public PublicDataCountBean getPublicCountxx(ServletContext appContext) {
		PublicDataCountJob job=new PublicDataCountJob();
		PublicDataCountBean dataCounts=job.getPublicDataCounts();
		appContext.setAttribute("publicCounts", dataCounts);
		return dataCounts;
	}
}
