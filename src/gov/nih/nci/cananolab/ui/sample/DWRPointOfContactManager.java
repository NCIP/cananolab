package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPointOfContactManager {

	private Logger logger = Logger.getLogger(DWRPointOfContactManager.class);
	private PointOfContactService service = new PointOfContactServiceLocalImpl();

	public DWRPointOfContactManager() {
	}

	public PointOfContactBean getPointOfContactById(String id) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContactBean poc = service.findPointOfContactById(id, user);
		return poc;
	}

	public PointOfContactBean resetThePointOfContact() {
		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("sampleForm"));
		SampleBean sample = (SampleBean) sampleForm
				.get("sampleBean");
		PointOfContactBean poc = new PointOfContactBean();
		sample.setThePOC(poc);
		return poc;
	}

	/* remove organization associated with the POC from the visiblity group */
	public String[] removeOrgVisibility(String pocId) {

		PointOfContactService pocService = new PointOfContactServiceLocalImpl();

		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		try {
			List<String> visibilityGroup = InitSecuritySetup.getInstance()
					.getAllVisibilityGroups(request);
			if (!pocId.equalsIgnoreCase("other")) {
				String sampleOrg = pocService.findPointOfContactById(pocId,
						user).getDomain().getOrganization().getName();
				visibilityGroup.remove(sampleOrg);
			}
			String[] eleArray = new String[visibilityGroup.size()];
			return visibilityGroup.toArray(eleArray);

		} catch (Exception e) {
			System.out.println("removeOrgVisibility exception.");
			e.printStackTrace();
		}

		return new String[] { "" };
	}

	/* remove organization name from the visiblity group */
	public String[] removeOrgNameVisibility(String orgName) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			List<String> visibilityGroup = InitSecuritySetup.getInstance()
					.getAllVisibilityGroups(request);
			if (!orgName.equalsIgnoreCase("other")) {
				visibilityGroup.remove(orgName);
			}
			String[] eleArray = new String[visibilityGroup.size()];
			return visibilityGroup.toArray(eleArray);

		} catch (Exception e) {
			System.out.println("removeOrgNameVisibility exception.");
			e.printStackTrace();
		}
		return new String[] { "" };
	}

}
