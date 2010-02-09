package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.List;
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

	public void setRemoteSearchDropdowns(HttpServletRequest request)
			throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		SortedSet<String> funcEntityTypes = InitSetup
				.getInstance()
				.getDefaultTypesByReflection(appContext,
						"defaultFunctionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		request.getSession().setAttribute("functionalizingEntityTypes",
				funcEntityTypes);

		SortedSet<String> nanoEntityTypes = InitSetup
				.getInstance()
				.getDefaultTypesByReflection(appContext,
						"defaultNanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		request.getSession().setAttribute("nanomaterialEntityTypes",
				nanoEntityTypes);
		SortedSet<String> funcTypes = InitSetup.getInstance()
				.getDefaultTypesByReflection(appContext,
						"defaultFunctionTypes",
						"gov.nih.nci.cananolab.domain.particle.Function");
		request.getSession().setAttribute("functionTypes", funcTypes);
		List<String> charTypes = InitCharacterizationSetup.getInstance()
				.getDefaultCharacterizationTypes(
						request.getSession().getServletContext());
		request.getSession().setAttribute("characterizationTypes", charTypes);
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
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
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

	public void setSamplePOCs(HttpServletRequest request,
			String sampleId) throws Exception {
		// set point of contacts
		SampleService service = new SampleServiceLocalImpl();
		List<PointOfContactBean> pocs = service
				.findPointOfContactsBySampleId(sampleId);
		request.getSession().setAttribute("samplePointOfContacts", pocs);
	}
}
