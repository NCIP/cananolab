package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This class set up drop-downs for experiment config submission page
 *
 * @author pansu, tanq
 *
 */
public class InitExperimentConfigSetup {
	private ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();

	private InitExperimentConfigSetup() {
	}

	public static InitExperimentConfigSetup getInstance() {
		return new InitExperimentConfigSetup();
	}

	public void getInstrumentsForTechnique(HttpServletRequest request,
			String technique) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"techniqueInstruments", technique, "instrument",
				"otherInstrument", true);
	}

	public void setExperimentConfigDropDowns(HttpServletRequest request)
			throws Exception {
		List<Technique> techniques = service.findAllTechniques();
		request.getSession().setAttribute("allTechniques", techniques);

		List<String> manufacturers = service.getAllManufacturers();
		request.getSession().setAttribute("allManufacturers", manufacturers);
	}
}
