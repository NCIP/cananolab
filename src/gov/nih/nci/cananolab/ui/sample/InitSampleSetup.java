package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.SortedSet;

import javax.servlet.ServletContext;
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

	private SampleService sampleService = new SampleServiceLocalImpl();

	public static InitSampleSetup getInstance() {
		return new InitSampleSetup();
	}

	public void setLocalSearchDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getReflectionDefaultAndOtherLookupTypes(
				request, "defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);
		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultNanomaterialEntityTypes",
						"nanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
						true);
		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultFunctionalizingEntityTypes",
						"functionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
						"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
						true);
		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
	}

	public void setRemoteSearchDropdowns(HttpServletRequest request)
			throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultFunctionalizingEntityTypes",
				"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultNanomaterialEntityTypes",
				"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultFunctionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function");

		InitCharacterizationSetup.getInstance()
				.getDefaultCharacterizationTypes(request);
	}

	public SortedSet<String> getAllSampleNames(HttpServletRequest request,
			UserBean user) throws Exception {
		SortedSet<String> sampleNames = sampleService.findAllSampleNames(user);
		request.getSession().setAttribute("allSampleNames", sampleNames);
		return sampleNames;
	}

	public SortedSet<SortableName> getOtherSampleNames(
			HttpServletRequest request, String sampleId) throws Exception {
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SortedSet<SortableName> names = sampleService
				.findOtherSamplesFromSamePointOfContact(sampleId, user);
		request.getSession().setAttribute("otherSampleNames", names);
		return names;
	}

	public void setSharedDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "file", "type", "otherType", true);
	}

	public SortedSet<String> getAllOrganizationNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> organizationNames = sampleService
				.getAllOrganizationNames(user);
		request.getSession().setAttribute("allOrganizationNames",
				organizationNames);
		return organizationNames;
	}

	public void setPOCDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"contactRoles", "point of contact", "role", "otherRole", true);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getAllOrganizationNames(request, user);
	}

	public void persistPOCDropdowns(HttpServletRequest request, Sample sample)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "point of contact",
				"role", "otherRole",
				sample.getPrimaryPointOfContact().getRole());
		if (sample.getOtherPointOfContactCollection() != null) {
			for (PointOfContact otherPoc : sample
					.getOtherPointOfContactCollection()) {
				InitSetup.getInstance().persistLookup(request,
						"point of contact", "role", "otherRole",
						(otherPoc.getRole()));
			}
		}
		setPOCDropdowns(request);
	}
}
