package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class set up drop-downs for experiment config submission page
 *
 * @author pansu, tanq
 *
 */
public class InitExperimentConfigSetup {

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
