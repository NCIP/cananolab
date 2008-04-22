package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;

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
	private NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();

	public static InitCharacterizationSetup getInstance() {
		return new InitCharacterizationSetup();
	}

	public void setCharactierizationDropDowns(HttpServletRequest request,
			String className) throws CaNanoLabException {
		HttpSession session = request.getSession();
		SortedSet<String> charSources = charService
				.getAllCharacterizationSources();
		session.setAttribute("characterizationSources", charSources);
		SortedSet<String> names = LookupService.getLookupValues(className,
				"derivedDatumName");
		session.setAttribute("derivedDatumNames", names);

		Map<String, SortedSet<String>> unitMap = new HashMap<String, SortedSet<String>>();
		for (String name : names) {
			SortedSet<String> units = LookupService.getLookupValues(name,
					"unit");
			unitMap.put(name, units);
		}
		session.setAttribute("unitMap", unitMap);

		SortedSet<String> valueTypes = LookupService.getLookupValues(
				"DerivedDatum", "valueType");
		session.setAttribute("derivedDatumValueTypes", valueTypes);

		List<Instrument> instruments = charService.getAllInstruments();
		SortedSet<String> manufacturers = new TreeSet<String>();
		SortedSet<String> instrumentTypes = new TreeSet<String>();
		for (Instrument instrument : instruments) {
			instrumentTypes.add(instrument.getType());
			manufacturers.add(instrument.getManufacturer());
		}
		session.setAttribute("manufacturers", manufacturers);
		session.setAttribute("instrumentTypes", instrumentTypes);
	}

	public void setPhysicalCharacterizationDropdowns(
			HttpServletRequest request, String className)
			throws CaNanoLabException {
		HttpSession session = request.getSession();

		// solubility
		SortedSet<String> solventTypes = LookupService.getLookupValues(
				"Solubility", "solvent");
		session.setAttribute("solventTypes", solventTypes);
		SortedSet<String> concentrationUnits = LookupService.getLookupValues(
				"SampleContainer", "concentrationUnit");
		session.setAttribute("concentrationUnits", concentrationUnits);
		// shape
		SortedSet<String> shapeTypes = LookupService.getLookupValues("Shape",
				"type");
		session.setAttribute("shapeTypes", shapeTypes);
		// physical state
		SortedSet<String> physicalStateTypes = LookupService.getLookupValues(
				"PhysicalState", "type");
		session.setAttribute("physicalStateTypes", physicalStateTypes);

		// surface chemistry
		SortedSet<String> molecularFormulaTypes = LookupService
				.getLookupValues("SurfaceChemistry", "molecularFormulaType");
		session.setAttribute("molecularFormulaTypes", molecularFormulaTypes);
	}
}
