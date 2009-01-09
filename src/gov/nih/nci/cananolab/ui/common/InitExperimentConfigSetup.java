package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class set up drop-downs for experiment config submission page
 *
 * @author pansu, tanq
 *
 */
public class InitExperimentConfigSetup {
	public void setTechniqueInstrumentDropDowns(HttpServletRequest request,
			String technique) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"techniqueInstruments", technique, "instrument",
				"otherInstrument", true);

	}
}
