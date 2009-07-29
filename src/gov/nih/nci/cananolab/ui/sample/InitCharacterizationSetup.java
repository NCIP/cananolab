package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicoChemicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for characterization forms.
 * 
 * @author pansu
 * 
 */
public class InitCharacterizationSetup {
	private CharacterizationService charService = new CharacterizationServiceLocalImpl();

	public static InitCharacterizationSetup getInstance() {
		return new InitCharacterizationSetup();
	}

	public List<String> getCharacterizationTypes(HttpServletRequest request)
			throws Exception {
		List<String> types = getDefaultCharacterizationTypes(request);
		SortedSet<String> otherTypes = LookupService
				.getAllOtherObjectTypes("gov.nih.nci.cananolab.domain.characterization.OtherCharacterization");
		for (String type : otherTypes) {
			if (!types.contains(type))
				types.add(type);
		}
		request.getSession().setAttribute("characterizationTypes", types);
		return types;
	}

	public List<String> getDefaultCharacterizationTypes(
			HttpServletRequest request) throws Exception {
		// need to keep the types in the specified order; otherwise could have
		// used
		// reflection
		ServletContext appContext = request.getSession().getServletContext();
		List<String> types = new ArrayList<String>();
		types.add(InitSetup.getInstance().getDisplayName(
				"PhysicoChemicalCharacterization", appContext));
		types.add(InitSetup.getInstance().getDisplayName(
				"InvitroCharacterization", appContext));
		types.add(InitSetup.getInstance().getDisplayName(
				"InvivoCharacterization", appContext));
		return types;
	}

	public void setCharactierizationDropDowns(HttpServletRequest request,
			String sampleId) throws Exception {
		getCharacterizationTypes(request);
		getDatumConditionValueTypes(request);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "File", "type", "otherType", true);
		// set point of contacts
		SampleService service = new SampleServiceLocalImpl();
		List<PointOfContactBean> pocs = service
				.findPointOfContactsBySampleId(sampleId);
		request.getSession().setAttribute("samplePointOfContacts", pocs);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void setCharacterizationDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);

		// solubility
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"solventTypes", "Solubility", "solvent", "otherSolvent", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"concentrationUnits", "Sample Concentration", "unit",
				"otherUnit", true);
		// shape
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"shapeTypes", "Shape", "type", "otherType", true);

		// physical state
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"physicalStateTypes", "PhysicalState", "type", "otherType",
				true);

		// enzyme induction
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "enzymeNames",
						"EnzymeInduction", "enzyme", "otherEnzyme", true);
	}

	// TODO::
	public void persistCharacterizationDropdowns(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "Shape", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "PhysicalState", "type",
				"otherType", charBean.getPhysicalState().getType());
		InitSetup.getInstance().persistLookup(request, "Solubility", "solvent",
				"otherSolvent", charBean.getSolubility().getSolvent());
		InitSetup.getInstance().persistLookup(request, "Sample Concentration",
				"unit", "otherUnit",
				charBean.getSolubility().getCriticalConcentrationUnit());
		InitSetup.getInstance().persistLookup(request, "dimension", "unit",
				"otherUnit", charBean.getShape().getMaxDimensionUnit());
		InitSetup.getInstance().persistLookup(request, "dimension", "unit",
				"otherUnit", charBean.getShape().getMinDimensionUnit());
		InitSetup.getInstance().persistLookup(request, "EnzymeInduction",
				"enzyme", "otherEnzyme",
				charBean.getEnzymeInduction().getEnzyme());
		setCharacterizationDropdowns(request);

		// for (DerivedBioAssayDataBean bioassay : charBean
		// .getDerivedBioAssayDataList()) {
		// if (bioassay.getFileBean() != null) {
		// InitSetup.getInstance().persistLookup(request, "File", "type",
		// "otherType",
		// bioassay.getFileBean().getDomainFile().getType());
		// }
		// for (DerivedDatumBean datum : bioassay.getDatumList()) {
		// InitSetup.getInstance().persistLookup(request,
		// datum.getDomainDerivedDatum().getName(), "unit",
		// "otherUnit",
		// datum.getDomainDerivedDatum().getValueUnit());
		// InitSetup.getInstance().persistLookup(request, "DerivedDatum",
		// "valueType", "otherValueType",
		// datum.getDomainDerivedDatum().getValueType());
		// InitSetup.getInstance().persistLookup(request,
		// charBean.getClassName(), "derivedDatumName",
		// "otherDerivedDatumName",
		// datum.getDomainDerivedDatum().getName());
		// }
		// }
		// setCharactierizationDropDowns(request,
		// charBean.getDomainChar().getId().toString());
	}

	/**
	 * Set characterization type of an existing characterization bean
	 * 
	 * @param request
	 * @param charBean
	 * @throws Exception
	 */
	public void setCharacterizationType(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		Characterization domainChar = charBean.getDomainChar();
		String charType = null;
		if (domainChar instanceof OtherCharacterization) {
			charType = ((OtherCharacterization) domainChar).getAssayCategory();
		} else {
			charType = InitSetup.getInstance().getDisplayName(
					ClassUtils.getShortClassName(domainChar.getClass()
							.getSuperclass().getName()),
					request.getSession().getServletContext());
		}
		charBean.setCharacterizationType(charType);
	}

	/**
	 * Set the display name for an existing characterization bean
	 * 
	 * @param request
	 * @param charBean
	 * @throws Exception
	 */
	public void setCharacterizationName(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		Characterization domainChar = charBean.getDomainChar();
		String charName = null;
		if (domainChar instanceof OtherCharacterization) {
			charName = ((OtherCharacterization) domainChar).getName();
		} else {
			charName = InitSetup.getInstance().getDisplayName(
					charBean.getClassName(),
					request.getSession().getServletContext());
		}
		charBean.setCharacterizationName(charName);
	}

	public SortedSet<String> getCharNamesByCharType(HttpServletRequest request,
			String charType) throws Exception {
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		ServletContext appContext = request.getSession().getServletContext();
		String fullCharTypeClass = InitSetup.getInstance().getFullClassName(
				charType, appContext);
		SortedSet<String> charNames = new TreeSet<String>();
		List<String> charClassNames = ClassUtils
				.getChildClassNames(fullCharTypeClass);
		for (String charClass : charClassNames) {
			if (!charClass.startsWith("Other")) {
				String shortClassName = ClassUtils.getShortClassName(charClass);
				charNames.add(InitSetup.getInstance().getDisplayName(
						shortClassName, appContext));
			}
		}

		List<String> otherCharNames = helper
				.findOtherCharacterizationByAssayCategory(charType);
		if (!otherCharNames.isEmpty()) {
			charNames.addAll(otherCharNames);
		}
		request.getSession().setAttribute("charTypeChars", charNames);
		return charNames;
	}

	public SortedSet<String> getAssayTypesByCharName(
			HttpServletRequest request, String charName) throws Exception {
		String charClassName = InitSetup.getInstance().getClassName(charName,
				request.getSession().getServletContext());
		SortedSet<String> assayTypes = LookupService
				.getDefaultAndOtherLookupTypes(charClassName, "assayType",
						"otherAssayType");
		request.getSession().setAttribute("charNameAssays", assayTypes);
		return assayTypes;
	}

	public SortedSet<String> getDatumNamesByCharName(
			HttpServletRequest request, String charType, String charName)
			throws Exception {
		String charClassName = InitSetup.getInstance().getClassName(charName,
				request.getSession().getServletContext());
		SortedSet<String> allDatumNames = new TreeSet<String>();
		// physico-chemical characterizions don't have assay types
		if (charType
				.equalsIgnoreCase(Constants.PHYSICOCHEMICAL_CHARACTERIZATION)) {
			allDatumNames = LookupService.getDefaultAndOtherLookupTypes(
					charClassName, "datumName", "otherDatumName");
		} else {
			SortedSet<String> assayTypes = LookupService
					.getDefaultAndOtherLookupTypes(charClassName, "assayType",
							"otherAssayType");

			if (assayTypes != null && !assayTypes.isEmpty()) {
				for (String type : assayTypes) {
					SortedSet<String> names = LookupService
							.getDefaultAndOtherLookupTypes(type, "datumName",
									"otherDatumName");
					for (String name : names) {
						allDatumNames.add(type + ":" + name);
					}
				}
			}
		}
		request.getSession().setAttribute("charNameDatumNames", allDatumNames);
		return allDatumNames;
	}

	public SortedSet<String> getConditions(HttpServletRequest request)
			throws Exception {
		SortedSet<String> conditions = LookupService
				.getDefaultAndOtherLookupTypes("Condition", "name", "otherName");
		request.getSession().setAttribute("datumConditions", conditions);
		return conditions;
	}

	public SortedSet<String> getValueUnits(HttpServletRequest request,
			String valueName) throws Exception {
		SortedSet<String> units = LookupService.getDefaultAndOtherLookupTypes(
				valueName, "unit", "otherUnit");
		request.getSession().setAttribute("valueUnits", units);
		return units;
	}

	public SortedSet<String> getDatumConditionValueTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> valueTypes = LookupService
				.getDefaultAndOtherLookupTypes("DatumCondition", "valueType",
						"otherValueType");
		request.getSession().setAttribute("datumConditionValueTypes",
				valueTypes);
		return valueTypes;
	}

	public String getDetailPage(Characterization domain) {
		String includePage = null;
		String shortClassName = ClassUtils.getShortClassName(domain.getClass()
				.getName());
		if (domain instanceof PhysicoChemicalCharacterization) {
			includePage = "/sample/characterization/physical/body"
					+ shortClassName + "Info.jsp";
		} else if (domain instanceof InvitroCharacterization) {
			includePage = "/sample/characterization/invitro/body"
					+ shortClassName + "Info.jsp";
		}
		return includePage;
	}

	public String getDetailPage(ServletContext appContext, String charName)
			throws Exception {
		String charClassName = InitSetup.getInstance().getClassName(charName,
				appContext);
		Class clazz = ClassUtils.getFullClass(charClassName);
		Characterization domain = (Characterization) clazz.newInstance();
		String includePage = getDetailPage(domain);
		return includePage;
	}
}
