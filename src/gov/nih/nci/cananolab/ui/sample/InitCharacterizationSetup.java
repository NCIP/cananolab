package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;

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
		ServletContext appContext = request.getSession().getServletContext();
		List<String> types = new ArrayList<String>();
		types.add(InitSetup.getInstance().getDisplayName(
				"PhysicoChemicalCharacterization", appContext));
		types.add(InitSetup.getInstance().getDisplayName(
				"InvitroCharacterization", appContext));
		types.add(InitSetup.getInstance().getDisplayName(
				"InvivoCharacterization", appContext));
		SortedSet<String> otherTypes = LookupService
				.getAllOtherObjectTypes("gov.nih.nci.cananolab.domain.characterization.OtherCharacterization");
		types.addAll(otherTypes);
		request.getSession().setAttribute("characterizationTypes", types);
		return types;
	}

	public void setCharactierizationDropDowns(HttpServletRequest request,
			String sampleId) throws Exception {
		getCharacterizationTypes(request);
		getDatumConditionValueTypes(request);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "File", "type", "otherType", true);
		// set point of contacts
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		List<PointOfContactBean> pocs = pocService
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
		String charType = InitSetup.getInstance().getDisplayName(
				ClassUtils.getShortClassName(domainChar.getClass()
						.getSuperclass().getName()),
				request.getSession().getServletContext());
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
		String charName = InitSetup.getInstance().getDisplayName(
				ClassUtils.getShortClassName(domainChar.getClass().getName()),
				request.getSession().getServletContext());
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
			HttpServletRequest request, String charName) throws Exception {
		String charClassName = InitSetup.getInstance().getClassName(charName,
				request.getSession().getServletContext());
		SortedSet<String> names = LookupService.getDefaultAndOtherLookupTypes(
				charClassName, "datumName", "otherDatumName");
		request.getSession().setAttribute("charNameDatumNames", names);
		return names;
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

	public SortedSet<String> getDatumConditionValueTypes(HttpServletRequest request)
			throws Exception {
		SortedSet<String> valueTypes = LookupService
				.getDefaultAndOtherLookupTypes("DatumCondition", "valueType",
						"otherValueType");
		request.getSession().setAttribute("datumConditionValueTypes", valueTypes);
		return valueTypes;
	}
}
