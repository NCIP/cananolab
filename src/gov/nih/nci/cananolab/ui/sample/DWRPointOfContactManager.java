package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPointOfContactManager {

	private Logger logger = Logger.getLogger(DWRPointOfContactManager.class);
	private SampleService service = new SampleServiceLocalImpl();
	private SampleServiceHelper helper = new SampleServiceHelper();

	public DWRPointOfContactManager() {
	}

	public PointOfContactBean getPointOfContactById(String id,
			Boolean primaryStatus) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContactBean poc = service.findPointOfContactById(id, user);
		poc.setPrimaryStatus(primaryStatus);
		return poc;
	}

	public PointOfContactBean resetThePointOfContact() {
		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("sampleForm"));
		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
		PointOfContactBean poc = new PointOfContactBean();
		sample.setThePOC(poc);
		//if primary POC already exists, the POC is secondar
		if (sample.getPrimaryPOCBean().getDomain().getId()!=null) {
			poc.setPrimaryStatus(false);
		}
		return poc;
	}

	public PointOfContactBean getPointOfContactByNameAndOrg(String firstName,
			String lastName, String orgName) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContact poc=helper.findPointOfContactByNameAndOrg(firstName, lastName, orgName, user);
		PointOfContactBean pocBean = new PointOfContactBean(poc);
		return pocBean;
	}

	public Organization getOrganizationByName(String name) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Organization org = helper.findOrganizationByName(name, user);
		return org;
	}

	/* remove organization associated with the POC from the visiblity group */
	public String[] removeOrgVisibility(String pocId) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		try {
			List<String> visibilityGroup = InitSecuritySetup.getInstance()
					.getAllVisibilityGroups(request);
			if (!pocId.equalsIgnoreCase("other")) {
				String sampleOrg = service.findPointOfContactById(pocId, user)
						.getDomain().getOrganization().getName();
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
