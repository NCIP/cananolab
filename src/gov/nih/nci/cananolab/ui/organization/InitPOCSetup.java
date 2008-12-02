package gov.nih.nci.cananolab.ui.organization;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for organization forms.
 * 
 * @author tanq
 * 
 */
public class InitPOCSetup {
	private InitPOCSetup() {
	}

	public static InitPOCSetup getInstance() {
		return new InitPOCSetup();
	}

	public void setPOCDropdowns(HttpServletRequest request) throws Exception {
		
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "contactTypes",
						"POC", "contactTypes", "otherContactTypes", true);		
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().getAllNanoparticleSampleNames(
				request, user);
	}

	
	public void persistPOCDropdowns(HttpServletRequest request,
			PointOfContact poc) throws Exception {	
		/**
		 * 
		 * HttpServletRequest request, String lookupName,
			String attribute, String otherAttribute, String value
		 */
		InitSetup.getInstance().persistLookup(request, "POC", "contactTypes",
				"otherContactTypes",
				poc.getRole());
		setPOCDropdowns(request);
	}	
}
