package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.SurfaceChemistry;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedDatumBean;
import gov.nih.nci.cananolab.dto.particle.characterization.InvitroCharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

	public void setCharactierizationDropDowns(HttpServletRequest request,
			String className) throws Exception {
		HttpSession session = request.getSession();
		SortedSet<String> charSources = charService
				.findAllCharacterizationSources();
		session.setAttribute("characterizationSources", charSources);
		SortedSet<String> derivedDatumNames = InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "derivedDatumNames",
						className, "derivedDatumName", "otherDerivedDatumName",
						true);
		Map<String, SortedSet<String>> unitMap = new HashMap<String, SortedSet<String>>();
		for (String name : derivedDatumNames) {
			SortedSet<String> units = InitSetup.getInstance()
					.getDefaultAndOtherLookupTypes(request,
							"derivedDatumUnits", name, "unit", "otherUnit",
							true);
			unitMap.put(name, units);
		}
		session.setAttribute("unitMap", unitMap);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"derivedDatumValueTypes", "DerivedDatum", "valueType",
				"otherValueType", true);

		List<Instrument> instruments = charService.findAllInstruments();
		SortedSet<String> manufacturers = new TreeSet<String>();
		SortedSet<String> instrumentTypes = new TreeSet<String>();
		for (Instrument instrument : instruments) {
			instrumentTypes.add(instrument.getType());
			manufacturers.add(instrument.getManufacturer());
		}
		session.setAttribute("manufacturers", manufacturers);
		session.setAttribute("instrumentTypes", instrumentTypes);
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
			HttpServletRequest request, PhysicalCharacterizationBean charBean)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "Shape", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "PhysicalState", "type",
				"otherType", charBean.getShape().getType());
		InitSetup.getInstance().persistLookup(request, "Solubility", "solvent",
				"otherSolvent", charBean.getShape().getType());
		for (SurfaceChemistry chem : charBean.getSurfaceBean()
				.getSurfaceChemistryList()) {
			InitSetup.getInstance().persistLookup(request, "SurfaceChemistry",
					"molecularFormulaType", "otherMolecularFormulaType",
					chem.getMolecularFormulaType());
		}
		setPhysicalCharacterizationDropdowns(request);
	}

	public void persistInvitroCharacterizationDropdowns(
			HttpServletRequest request, InvitroCharacterizationBean charBean)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
				"cellDeathMethod", "otherCellDeathMethod",
				charBean.getCellViability().getCellDeathMethod());
		InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
				"cellDeathMethod", "otherCellDeathMethod",
				charBean.getCaspase3Activation().getCellDeathMethod());
		InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
				"cellLine", "otherCellLine",
				charBean.getCellViability().getCellLine());
		InitSetup.getInstance().persistLookup(request, "Cytotoxicity",
				"cellLine", "otherCellLine",
				charBean.getCaspase3Activation().getCellLine());
		setInvitroCharacterizationDropdowns(request);
	}

	public void persistCharacterizationDropdowns(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		for (DerivedBioAssayDataBean bioassay : charBean
				.getDerivedBioAssayDataList()) {
			if (bioassay.getLabFileBean() != null) {
				InitSetup.getInstance().persistLookup(request, "LabFile",
						"type", "otherType",
						bioassay.getLabFileBean().getDomainFile().getType());
			}
			for (DerivedDatumBean datum : bioassay.getDatumList()) {
				InitSetup.getInstance().persistLookup(request,
						datum.getDomainDerivedDatum().getName(), "unit",
						"otherUnit",
						datum.getDomainDerivedDatum().getValueUnit());
				InitSetup.getInstance().persistLookup(request, "DerivedDatum",
						"valueType", "otherValueType",
						datum.getDomainDerivedDatum().getValueType());
				InitSetup.getInstance().persistLookup(request,
						charBean.getClassName(), "derivedDatumName",
						"otherDerivedDatumName",
						datum.getDomainDerivedDatum().getName());
			}
		}
		setCharactierizationDropDowns(request, charBean.getClassName());
	}
}
