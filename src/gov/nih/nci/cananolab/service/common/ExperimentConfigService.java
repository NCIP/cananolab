package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;

import java.util.List;

/**
 *
 * @author pansu
 *
 */
public interface ExperimentConfigService {
	public void saveExperimentConfig(ExperimentConfig experimentConfig)
			throws ExperimentConfigException;

	public void deleteExperimentConfig(ExperimentConfig experimentConfig)
			throws ExperimentConfigException;

	public List<Technique> findAllTechniques() throws ExperimentConfigException;

	public List<String> getAllManufacturers() throws ExperimentConfigException;

	public ExperimentConfig findExperimentConfigById(String id)
			throws ExperimentConfigException;

	public Technique findTechniqueByType(String type)
			throws ExperimentConfigException;

	public Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException;
}
