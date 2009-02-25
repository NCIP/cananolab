package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceLocalImpl;
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
	private NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();

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
			String particleId) throws Exception {
		getCharacterizationTypes(request);
		// set point of contacts
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		List<PointOfContactBean> pocs = pocService
				.findPointOfContactsByParticleId(particleId);
		request.getSession().setAttribute("particlePointOfContacts", pocs);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void setPhysicalCharacterizationDropdowns(HttpServletRequest request)
			throws Exception {
		// solubility
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"solventTypes", "Solubility", "solvent", "otherSolvent", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"concentrationUnits", "SampleContainer", "concentrationUnit",
				"otherConcentrationUnit", true);
		// shape
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"shapeTypes", "Shape", "type", "otherType", true);

		// physical state
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"physicalStateTypes", "PhysicalState", "type", "otherType",
				true);

		// surface chemistry
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"scMolecularFormulaTypes", "SurfaceChemistry",
				"molecularFormulaType", "otherMolecularFormulaType", true);
	}

	public void setInvitroCharacterizationDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"cellLines", "Cytotoxicity", "cellLine", "otherCellLine", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"cellDeathMethods", "Cytotoxicity", "cellDeathMethod",
				"otherCellDeathMethod", true);
	}

	public void persistPhysicalCharacterizationDropdowns(
			HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "Shape", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "PhysicalState", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "Solubility", "solvent",
				"otherSolvent", charBean.getShape().getType());
		setPhysicalCharacterizationDropdowns(request);
	}

	public void persistInvitroCharacterizationDropdowns(
			HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		// TODO fix in vitro chara
		//
		// InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
		// "cellDeathMethod", "otherCellDeathMethod",
		// charBean.getCellDeathMethod());
		// InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
		// "cellDeathMethod", "otherCellDeathMethod",
		// charBean.getCaspase3Activation().getCellDeathMethod());
		// InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
		// "cellLine", "otherCellLine",
		// charBean.getCellViability().getCellLine());
		// InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
		// "cellLine", "otherCellLine",
		// charBean.getCaspase3Activation().getCellLine());
		setInvitroCharacterizationDropdowns(request);
	}

	// TODO::
	public void persistCharacterizationDropdowns(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
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
		setCharactierizationDropDowns(request, charBean.getClassName());
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
		NanoparticleCharacterizationServiceHelper helper = new NanoparticleCharacterizationServiceHelper();
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
		return charNames;
	}

	public SortedSet<String> getAssayEndpointsByCharName(
			HttpServletRequest request, String charName) throws Exception {
		String charClassName = InitSetup.getInstance().getClassName(charName,
				request.getSession().getServletContext());
		SortedSet<String> assayTypes = LookupService
				.getDefaultAndOtherLookupTypes(charClassName, "assayType",
						"otherAssayType");
		return assayTypes;
	}
}
