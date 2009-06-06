package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.sample.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.SampleConstants;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;

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

		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
	}

	public SortedSet<PointOfContactBean> getAllPointOfContacts(
			HttpServletRequest request) throws Exception {
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		SortedSet<PointOfContact> pointOfContacts = pocService
				.findAllPointOfContacts();
		SortedSet<PointOfContactBean> pointOfContactBeans = null;
		if (pointOfContacts != null && pointOfContacts.size() > 0) {
			pointOfContactBeans = new TreeSet<PointOfContactBean>(
					new Comparators.SamplePointOfContactBeanComparator());
			for (PointOfContact poc : pointOfContacts) {
				pointOfContactBeans.add(new PointOfContactBean(poc));
			}
		}
		request.getSession().setAttribute("allPointOfContacts",
				pointOfContactBeans);
		return pointOfContactBeans;
	}

	public SortedSet<String> getAllSampleNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> sampleNames = sampleService
				.findAllSampleNames(user);
		request.getSession().setAttribute("allUserSampleNames", sampleNames);
		return sampleNames;
	}

	public SortedSet<String> getAllSampleNames(HttpServletRequest request)
			throws Exception {
		SortedSet<String> sampleNames = sampleService.findAllSampleNames();
		request.getSession().setAttribute("allSampleNames", sampleNames);
		return sampleNames;
	}

	public SortedSet<SortableName> getOtherSampleNames(
			HttpServletRequest request, String sampleId) throws Exception {
		SortedSet<SortableName> names = sampleService
				.findOtherSamplesFromSamePointOfContact(sampleId);
		request.getSession().setAttribute("otherSampleNames", names);
		return names;
	}

	public void setSharedDropdowns(HttpServletRequest request) throws Exception {
		// set static boolean yes or no and characterization source choices
		ServletContext appContext = request.getSession().getServletContext();
		LabelValueBean trueBean = new LabelValueBean();
		trueBean.setLabel(Constants.BOOLEAN_YES);
		trueBean.setValue("true");
		LabelValueBean falseBean = new LabelValueBean();
		falseBean.setLabel(Constants.BOOLEAN_NO);
		falseBean.setValue("false");
		LabelValueBean[] booleanBeans = new LabelValueBean[2];
		booleanBeans[0] = trueBean;
		booleanBeans[1] = falseBean;

		appContext.setAttribute("booleanChoices", booleanBeans);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "File", "type", "otherType", true);

		// For PubChem data sources drop-down list.
		appContext.setAttribute("pubChemDataSources", SampleConstants.PUBCHEM_DS_LIST);
	}
}
