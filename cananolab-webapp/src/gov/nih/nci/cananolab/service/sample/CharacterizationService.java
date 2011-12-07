package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

/**
 * Interface defining service methods involving characterizations
 *
 * @author pansu, tanq
 *
 */
public interface CharacterizationService extends BaseService {

	public void saveCharacterization(SampleBean sampleBean,
			CharacterizationBean achar) throws Exception;

	public CharacterizationBean findCharacterizationById(String charId)
			throws CharacterizationException, NoAccessException;

	public void deleteCharacterization(Characterization chara)
			throws CharacterizationException, NoAccessException;

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId) throws CharacterizationException,
			NoAccessException;

	public void saveFinding(FindingBean findingBean)
			throws CharacterizationException, NoAccessException;

	public FindingBean findFindingById(String findingId)
			throws CharacterizationException, NoAccessException;

	public void deleteFinding(Finding finding)
			throws CharacterizationException, NoAccessException;

	public void saveExperimentConfig(ExperimentConfigBean experimentConfigBean)
			throws ExperimentConfigException, NoAccessException;

	public void deleteExperimentConfig(ExperimentConfig experimentConfig)
			throws ExperimentConfigException, NoAccessException;

	/**
	 * Copy and save a characterization from one sample to other samples
	 *
	 * @param charBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @param copyData
	 * @throws CharacterizationException
	 * @throws NoAccessException
	 */
	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData) throws CharacterizationException,
			NoAccessException;

	public int getNumberOfPublicCharacterizations(
			String characterizationClassName) throws CharacterizationException;

	public List<String> findOtherCharacterizationByAssayCategory(
			String assayCategory) throws CharacterizationException;

	public void assignAccesses(Characterization achar, ExperimentConfig config)
			throws CharacterizationException, NoAccessException;

	public void removeAccesses(Characterization achar, ExperimentConfig config)
			throws CharacterizationException, NoAccessException;

	public void assignAccesses(Characterization achar, Finding finding)
			throws CharacterizationException, NoAccessException;

	public void removeAccesses(Characterization achar, Finding finding)
			throws CharacterizationException, NoAccessException;

	public void removeAccesses(Characterization achar)
			throws CharacterizationException, NoAccessException;

}