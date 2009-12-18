package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPointOfContactManager {
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
		if (user == null) {
			return null;
		}
		PointOfContactBean poc = service.findPointOfContactById(id, user);
		poc.setPrimaryStatus(primaryStatus);
		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("sampleForm"));
		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
		sample.setThePOC(poc);
		return poc;
	}

	public PointOfContactBean resetThePointOfContact() throws Exception {
		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("sampleForm"));
		if (sampleForm == null) {
			return null;
		}
		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
		PointOfContactBean poc = new PointOfContactBean();
		sample.setThePOC(poc);
		// if primary POC already exists, the POC is secondary
		if (sample.getPrimaryPOCBean().getDomain().getId() != null) {
			poc.setPrimaryStatus(false);
		}
		return poc;
	}

	public Organization getOrganizationByName(String name) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Organization org = helper.findOrganizationByName(name, user);
		return org;
	}

	public String[] removeOrgFromVisibilityGroupsByPocId(String id,
			Boolean primaryStatus) throws Exception {
		PointOfContactBean pocBean = this.getPointOfContactById(id,
				primaryStatus);
		if (pocBean == null) {
			return null;
		}
		String orgName = pocBean.getDomain().getOrganization().getName();
		return removeOrgFromVisibilityGroupsByOrgName(orgName);
	}

	public String[] removeOrgFromVisibilityGroupsByOrgName(String orgName)
			throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		List<String> visibilityGroup = InitSecuritySetup.getInstance()
				.getAllVisibilityGroups(request);
		visibilityGroup.remove(orgName);
		return visibilityGroup.toArray(new String[0]);
	}

	public String[] resetVisibilityGroups() throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		List<String> visibilityGroup = InitSecuritySetup.getInstance()
				.getAllVisibilityGroups(request);
		return visibilityGroup.toArray(new String[0]);
	}

	public String[] resetSampleVisibilityGroups() throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		List<String> visibilityGroup = InitSecuritySetup.getInstance()
				.getAllVisibilityGroups(request);
		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("sampleForm"));
		if (sampleForm == null) {
			return null;
		}
		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
		String orgName = null;
		if (sample.getPrimaryPOCBean() != null
				&& sample.getPrimaryPOCBean().getDomain().getId() != null) {
			orgName = sample.getPrimaryPOCBean().getDomain().getOrganization()
					.getName();
			if (!StringUtils.isEmpty(orgName)) {
				visibilityGroup.remove(orgName);
			}
		}
		return visibilityGroup.toArray(new String[0]);
	}
}
