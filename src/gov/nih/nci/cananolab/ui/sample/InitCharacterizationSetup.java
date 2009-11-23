package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;

/**
 * This class sets up information required for characterization forms.
 * 
 * @author pansu
 * 
 */
public class InitCharacterizationSetup {
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

	public List<LabelValueBean> getDecoratedCharacterizationTypes(
			HttpServletRequest request) throws Exception {
		List<LabelValueBean> charTypes = new ArrayList<LabelValueBean>();
		List<String> types = getDefaultCharacterizationTypes(request);
		for (String type : types) {
			LabelValueBean bean = new LabelValueBean(type, type);
			charTypes.add(bean);
		}
		SortedSet<String> otherTypes = LookupService
				.getAllOtherObjectTypes("gov.nih.nci.cananolab.domain.characterization.OtherCharacterization");
		for (String type : otherTypes) {
			if (!types.contains(type)) {
				LabelValueBean bean = new LabelValueBean("[" + type + "]", type);
				charTypes.add(bean);
			}
		}
		request.getSession().setAttribute("decoratedCharacterizationTypes",
				charTypes);
		return charTypes;
	}

	public List<String> getDefaultCharacterizationTypes(
			HttpServletRequest request) throws Exception {
		// need to keep the types in the specified order; otherwise could have
		// used reflection
		List<String> types = new ArrayList<String>();
		types.add(ClassUtils.getDisplayName("PhysicoChemicalCharacterization"));
		types.add(ClassUtils.getDisplayName("InvitroCharacterization"));
		types.add(ClassUtils.getDisplayName("InvivoCharacterization"));
		return types;
	}

	public void setCharactierizationDropDowns(HttpServletRequest request,
			String sampleId) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		getCharacterizationTypes(request);
		getDatumConditionValueTypes(request);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "file", "type", "otherType", true);
		// set point of contacts
		SampleService service = new SampleServiceLocalImpl();
		List<PointOfContactBean> pocs = service
				.findPointOfContactsBySampleId(sampleId);
		request.getSession().setAttribute("samplePointOfContacts", pocs);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void setCharacterizationDropdowns(HttpServletRequest request)
			throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		setExperimentConfigDropDowns(request);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);

		// solubility
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"solventTypes", "solubility", "solvent", "otherSolvent", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"concentrationUnits", "sample concentration", "unit",
				"otherUnit", true);
		// shape
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"shapeTypes", "shape", "type", "otherType", true);

		// physical state
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"physicalStateTypes", "physical state", "type", "otherType",
				true);

		// enzyme induction
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"enzymeNames", "enzyme induction", "enzyme", "otherEnzyme",
				true);
	}

	public void persistCharacterizationDropdowns(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "shape", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "physical state",
				"type", "otherType", charBean.getPhysicalState().getType());
		InitSetup.getInstance().persistLookup(request, "solubility", "solvent",
				"otherSolvent", charBean.getSolubility().getSolvent());
		InitSetup.getInstance().persistLookup(request, "sample concentration",
				"unit", "otherUnit",
				charBean.getSolubility().getCriticalConcentrationUnit());
		InitSetup.getInstance().persistLookup(request, "dimension", "unit",
				"otherUnit", charBean.getShape().getMaxDimensionUnit());
		InitSetup.getInstance().persistLookup(request, "dimension", "unit",
				"otherUnit", charBean.getShape().getMinDimensionUnit());
		InitSetup.getInstance().persistLookup(request, "enzyme induction",
				"enzyme", "otherEnzyme",
				charBean.getEnzymeInduction().getEnzyme());

		if (charBean.getTheFinding().getDomain().getDatumCollection() != null) {
			for (Datum datum : charBean.getTheFinding().getDomain()
					.getDatumCollection()) {
				InitSetup.getInstance().persistLookup(request,
						charBean.getCharacterizationName(), "datumName",
						"otherDatumName", datum.getName());
				InitSetup.getInstance().persistLookup(request, datum.getName(),
						"unit", "otherUnit", datum.getValueUnit());
				InitSetup.getInstance().persistLookup(request,
						"datum and condition", "valueType", "otherValueType",
						datum.getValueType());
				for (Condition condition : datum.getConditionCollection()) {
					InitSetup.getInstance().persistLookup(request, "condition",
							"name", "otherName", condition.getName());
					InitSetup.getInstance().persistLookup(request,
							condition.getName(), "unit", "otherUnit",
							condition.getValueUnit());
					InitSetup.getInstance().persistLookup(request,
							"datum and condition", "valueType",
							"otherValueType", condition.getValueType());
				}
			}
		}

		InitSetup.getInstance()
				.persistLookup(
						request,
						"file",
						"type",
						"otherType",
						charBean.getTheFinding().getTheFile().getDomainFile()
								.getType());

		persistExperimentConfigDropdowns(request, charBean
				.getTheExperimentConfig());
		setCharacterizationDropdowns(request);
	}

	public SortedSet<String> getCharNamesByCharType(HttpServletRequest request,
			String charType) throws Exception {
		if (StringUtils.isEmpty(charType)) {
			return null;
		}
		SortedSet<String> charNames = new TreeSet<String>();
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		String shortClassNameForCharType = ClassUtils
				.getShortClassNameFromDisplayName(charType);
		Class clazz = ClassUtils.getFullClass(shortClassNameForCharType);
		String fullCharTypeClass = null;
		if (clazz != null) {
			String fullCharTypeClassName = clazz.getName();
			List<String> charClassNames = ClassUtils
					.getChildClassNames(fullCharTypeClassName);
			for (String charClass : charClassNames) {
				if (!charClass.startsWith("Other")) {
					String shortClassName = ClassUtils
							.getShortClassName(charClass);
					charNames.add(ClassUtils.getDisplayName(shortClassName));
				}
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

	public List<LabelValueBean> getDecoratedCharNamesByCharType(
			HttpServletRequest request, String charType) throws Exception {
		if (StringUtils.isEmpty(charType)) {
			return null;
		}
		SortedSet<String> charNames = new TreeSet<String>();
		List<LabelValueBean> charNameBeans = new ArrayList<LabelValueBean>();
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		String shortClassNameForCharType = ClassUtils
				.getShortClassNameFromDisplayName(charType);
		Class clazz = ClassUtils.getFullClass(shortClassNameForCharType);
		if (clazz != null) {
			String fullCharTypeClassName = clazz.getName();
			List<String> charClassNames = ClassUtils
					.getChildClassNames(fullCharTypeClassName);
			for (String charClass : charClassNames) {
				if (!charClass.startsWith("Other")) {
					String shortClassName = ClassUtils
							.getShortClassName(charClass);
					String charName = ClassUtils.getDisplayName(shortClassName);
					LabelValueBean bean = new LabelValueBean(charName, charName);
					charNames.add(charName);
					charNameBeans.add(bean);
				}
			}
		}
		List<String> otherCharNames = helper
				.findOtherCharacterizationByAssayCategory(charType);
		if (!otherCharNames.isEmpty()) {
			for (String name : otherCharNames) {
				if (!charNames.contains(name)) {
					LabelValueBean bean = new LabelValueBean(name, name);
					charNameBeans.add(bean);
				}
			}
		}
		return charNameBeans;
	}

	public SortedSet<String> getAssayTypesByCharName(
			HttpServletRequest request, String charName) throws Exception {
		SortedSet<String> assayTypes = LookupService
				.getDefaultAndOtherLookupTypes(charName, "assayType",
						"otherAssayType");
		request.getSession().setAttribute("charNameAssays", assayTypes);
		return assayTypes;
	}

	public SortedSet<String> getDatumNamesByCharName(
			HttpServletRequest request, String charType, String charName,
			String assayType) throws Exception {
		SortedSet<String> allDatumNames = LookupService
				.getDefaultAndOtherLookupTypes(charName, "datumName",
						"otherDatumName");
		// if assayType is empty, use charName to look up datums, as well as
		// look up all assay types and use assay type to look up datum
		if (StringUtils.isEmpty(assayType)) {
			SortedSet<String> assayTypes = LookupService
					.getDefaultAndOtherLookupTypes(charName, "assayType",
							"otherAssayType");
			if (assayTypes != null && !assayTypes.isEmpty()) {
				for (String type : assayTypes) {
					SortedSet<String> names = LookupService
							.getDefaultAndOtherLookupTypes(type, "datumName",
									"otherDatumName");
					for (String name : names) {
						allDatumNames.add(name);
					}
				}
			}
		} else {
			allDatumNames.addAll(LookupService.getDefaultAndOtherLookupTypes(
					assayType, "datumName", "otherDatumName"));
		}
		request.getSession().setAttribute("charNameDatumNames", allDatumNames);
		return allDatumNames;
	}

	public SortedSet<String> getConditions(HttpServletRequest request)
			throws Exception {
		SortedSet<String> conditions = LookupService
				.getDefaultAndOtherLookupTypes("condition", "name", "otherName");
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
				.getDefaultAndOtherLookupTypes("datum and condition",
						"valueType", "otherValueType");
		request.getSession().setAttribute("datumConditionValueTypes",
				valueTypes);
		return valueTypes;
	}

	public String getDetailPage(String charType, String charName) {
		String charClassName = ClassUtils
				.getShortClassNameFromDisplayName(charName);
		String includePage = null;
		if (charType.equals(Constants.PHYSICOCHEMICAL_CHARACTERIZATION)) {
			includePage = "/sample/characterization/physical/body"
					+ charClassName + "Info.jsp";
		} else if (charType.equals(Constants.INVITRO_CHARACTERIZATION)) {
			includePage = "/sample/characterization/invitro/body"
					+ charClassName + "Info.jsp";
		}
		return includePage;
	}

	public void getInstrumentsForTechnique(HttpServletRequest request,
			String technique) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"techniqueInstruments", technique, "instrument",
				"otherInstrument", true);
	}

	public void setExperimentConfigDropDowns(HttpServletRequest request)
			throws Exception {
		// instrument manufacturers and techniques
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"manufacturers", "instrument", "manufacturer",
				"otherManufacturer", true);

		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"techniqueTypes", "technique", "type", "otherType", true);
	}

	public void persistExperimentConfigDropdowns(HttpServletRequest request,
			ExperimentConfigBean configBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "technique", "type",
				"otherType", configBean.getDomain().getTechnique().getType());
		for (Instrument instrument : configBean.getInstruments()) {
			InitSetup.getInstance().persistLookup(request,
					configBean.getDomain().getTechnique().getType(),
					"instrument", "otherInstrument", instrument.getType());
			InitSetup.getInstance().persistLookup(request, "instrument",
					"manufacturer", "otherManufacturer",
					instrument.getManufacturer());
		}
		setExperimentConfigDropDowns(request);
	}
}
