package gov.nih.nci.cananolab.restful.helper;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.service.PublicDataCountJob;

public class InitSetupUtil {

	/**
	 * Moved from ui.core.InitSetup.setPublicCountInContext()
	 * 
	 * @return
	 */
	public PublicDataCountBean getPublicCount() {
		PublicDataCountJob job=new PublicDataCountJob();
		PublicDataCountBean dataCounts=job.getPublicDataCounts();
		return dataCounts;
	}
}
