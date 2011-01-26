package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.common.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPointOfContactManager {
	private PointOfContactServiceLocalImpl service;

	private PointOfContactServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		SecurityService securityService = (SecurityService) wctx.getSession()
				.getAttribute("securityService");

		service = new PointOfContactServiceLocalImpl(securityService);
		return service;
	}

	public PointOfContactBean getPointOfContactById(String id,
			Boolean primaryStatus, String parent) throws Exception {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		PointOfContactBean poc = getService().findPointOfContactById(id);
		poc.setPrimaryStatus(primaryStatus);
		if (parent.equals("sample")) {
			DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
					.get().getSession().getAttribute("sampleForm"));
			SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
			sample.setThePOC(poc);
		} else if (parent.equals("study")) {
			DynaValidatorForm studyForm = (DynaValidatorForm) (WebContextFactory
					.get().getSession().getAttribute("studyForm"));
			StudyBean study = (StudyBean) studyForm.get("studyBean");
			study.setThePOC(poc);
		}
		return poc;
	}

	public PointOfContactBean resetThePointOfContact(String parent)
			throws Exception {
		PointOfContactBean poc = new PointOfContactBean();
		if (parent.equals("sample")) {
			DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
					.get().getSession().getAttribute("sampleForm"));
			if (sampleForm == null) {
				return null;
			}
			SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
			sample.setThePOC(poc);
			// if primary POC already exists, the POC is secondary
			if (sample.getPrimaryPOCBean().getDomain().getId() != null) {
				poc.setPrimaryStatus(false);
			}
		} else if (parent.equals("study")) {
			DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
					.get().getSession().getAttribute("sampleForm"));
			if (sampleForm == null) {
				return null;
			}
			SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
			sample.setThePOC(poc);
			// if primary POC already exists, the POC is secondary
			if (sample.getPrimaryPOCBean().getDomain().getId() != null) {
				poc.setPrimaryStatus(false);
			}
		}
		return poc;
	}

	public Organization getOrganizationByName(String name) throws Exception {
		Organization org = ((PointOfContactServiceLocalImpl) getService())
				.getHelper().findOrganizationByName(name);
		return org;
	}

	public PointOfContactBean getPointOfContactByNameAndOrg(String firstName,
			String lastName, String orgName) throws Exception {
		PointOfContact poc = ((PointOfContactServiceLocalImpl) getService())
				.getHelper().findPointOfContactByNameAndOrg(firstName,
						lastName, orgName);
		if (poc != null) {
			return new PointOfContactBean(poc);
		} else {
			return null;
		}
	}
}
