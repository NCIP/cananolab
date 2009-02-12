package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.Collection;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for organization forms.
 * 
 * @author tanq
 * 
 */
public class InitPOCSetup {
	private PointOfContactServiceLocalImpl pocService = new PointOfContactServiceLocalImpl();

	private InitPOCSetup() {
	}

	public static InitPOCSetup getInstance() {
		return new InitPOCSetup();
	}

	public void setPOCDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "contactRoles",
						"PointOfContact", "role", "otherRole", true);		
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getAllOrganizationNames(request, user);
	}
	
	public void persistPOCDropdowns(HttpServletRequest request,
			PointOfContact primaryPointOfContact, 
			Collection<PointOfContact> otherPointOfContactCollection ) throws Exception {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String user = userBean.getLoginName();
		if (primaryPointOfContact!=null) {
			pocService.saveOrganization(primaryPointOfContact.getOrganization(), user);
		}
		InitSetup.getInstance().persistLookup(request, "PointOfContact", "role",
				"otherRole",
				(primaryPointOfContact.getRole()));
		if (otherPointOfContactCollection!=null) {
			for (PointOfContact otherPoc: otherPointOfContactCollection) {
				if (otherPoc!=null) {
					pocService.saveOrganization(otherPoc.getOrganization(), user);
				}
				InitSetup.getInstance().persistLookup(request, "PointOfContact", "role",
						"otherRole",
						(otherPoc.getRole()));
			}
		}		
		setPOCDropdowns(request);
	}
	
	public SortedSet<String> getAllOrganizationNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> organizationNames = pocService
				.getAllOrganizationNames(user);
		request.getSession().setAttribute("allOrganizationNames", organizationNames);
		return organizationNames;
	}
}
