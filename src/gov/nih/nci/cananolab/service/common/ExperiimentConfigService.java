package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;

import java.util.List;

/**
 *
 * @author pansu
 *
 */
public interface ExperiimentConfigService {
	public void saveExperimentConfig(ExperimentConfig experimentConfig)
			throws ExperimentConfigException;

	public List<Technique> findAllTechniques() throws ExperimentConfigException;
}
