package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for canano forms.
 *
 * @author pansu, cais
 *
 */
public class InitSampleSetup {
	private InitSampleSetup() {
	}

	public static InitSampleSetup getInstance() {
		return new InitSampleSetup();
	}

	public void setLocalSearchDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByReflection(request,
				"defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);
		InitSetup
				.getInstance()
				.getDefaultAndOtherTypesByReflection(
						request,
						"defaultNanomaterialEntityTypes",
						"nanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
						true);
		InitSetup
				.getInstance()
				.getDefaultAndOtherTypesByReflection(
						request,
						"defaultFunctionalizingEntityTypes",
						"functionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
						"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
						true);
		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
	}

	public List<String> getOtherSampleNames(HttpServletRequest request,
			String sampleId) throws Exception {
		SampleService service = getServiceFromSession(request);
		List<String> names = service
				.findOtherSampleNamesFromSamePrimaryOrganization(sampleId);
		request.getSession().setAttribute("otherSampleNames", names);
		return names;
	}

	public void setSharedDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"fileTypes", "file", "type", "otherType", true);
	}

	public SortedSet<String> getAllOrganizationNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SampleService service = getServiceFromSession(request);
		SortedSet<String> organizationNames = service.getAllOrganizationNames();
		request.getSession().setAttribute("allOrganizationNames",
				organizationNames);
		return organizationNames;
	}

	public void setPOCDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"contactRoles", "point of contact", "role", "otherRole", true);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getAllOrganizationNames(request, user);
	}

	public void persistPOCDropdowns(HttpServletRequest request,
			SampleBean sampleBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "point of contact",
				"role", "otherRole",
				sampleBean.getThePOC().getDomain().getRole());
		setPOCDropdowns(request);
	}

	private SampleService getServiceFromSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if (request.getSession().getAttribute("sampleService") != null) {
			return (SampleService) request.getSession().getAttribute(
					"sampleService");
		} else {
			return new SampleServiceLocalImpl(securityService);
		}
	}
}
