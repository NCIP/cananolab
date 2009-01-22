package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;

import java.util.ArrayList;
import java.util.SortedSet;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
public class DWRExperimentConfigManager {
	private ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();

	public void resetExperimentConfig() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		charBean.setTheExperimentConfig(new ExperimentConfigBean());
		charBean.setTheInstrument(new Instrument());
	}

	public ExperimentConfig findExperimentConfigById(String id)
			throws ExperimentConfigException {
		return service.findExperimentConfigById(id);
	}

	public Technique findTechniqueByType(String type)
			throws ExperimentConfigException {
		return service.findTechniqueByType(type);
	}

	public String[] findInstrumentTypesByTechniqueType(String techniqueType)
			throws ExperimentConfigException, CaNanoLabException {
		SortedSet<String> types = null;
		types = LookupService.getDefaultAndOtherLookupTypes(techniqueType,
				"instrument", "otherInstrument");
		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}

	public String[] findInstrumentTypesByConfigId(String configId)
			throws ExperimentConfigException, CaNanoLabException {
		String techniqueType = null;
		SortedSet<String> types = null;
		ExperimentConfig config = findExperimentConfigById(configId);

		if (config != null && config.getTechnique() != null
				&& config.getTechnique().getType() != null) {
			techniqueType = config.getTechnique().getType();
			types = LookupService.getDefaultAndOtherLookupTypes(techniqueType,
					"instrument", "otherInstrument");
		}
		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}

	public ExperimentConfig getANewExperimentConfig()
			throws ExperimentConfigException {
		// add by Qina tempory
		ExperimentConfig config = new ExperimentConfig();
		config.setTechnique(new Technique());
		config.setInstrumentCollection(new ArrayList<Instrument>());
		return config;
	}
}
