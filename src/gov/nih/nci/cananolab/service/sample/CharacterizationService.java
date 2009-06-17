package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface defining service methods involving characterizations
 *
 * @author pansu, tanq
 *
 */
public interface CharacterizationService {

	public void saveCharacterization(Sample particleSample,
			CharacterizationBean achar, UserBean user) throws Exception;

	public CharacterizationBean findCharacterizationById(String charId,
			UserBean user) throws CharacterizationException, NoAccessException;

	public void deleteCharacterization(Characterization chara, UserBean user)
			throws CharacterizationException, NoAccessException;

	public List<CharacterizationBean> findCharsBySampleId(String sampleId,
			UserBean user) throws CharacterizationException, NoAccessException;

	public void saveFinding(FindingBean findingBean, UserBean user)
			throws CharacterizationException, NoAccessException;

	public FindingBean findFindingById(String findingId, UserBean user)
			throws CharacterizationException, NoAccessException;

	public void deleteFinding(Finding finding, UserBean user)
			throws CharacterizationException, NoAccessException;

	public void saveExperimentConfig(ExperimentConfigBean experimentConfigBean,
			UserBean user) throws ExperimentConfigException, NoAccessException;

	public void deleteExperimentConfig(ExperimentConfig experimentConfig,
			UserBean user) throws ExperimentConfigException, NoAccessException;

	public List<Technique> findAllTechniques() throws ExperimentConfigException;

	public List<String> getAllManufacturers() throws ExperimentConfigException;

	public Technique findTechniqueByType(String type)
			throws ExperimentConfigException;

	public Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException;

	/**
	 * Export sample characterization summary report as Excel spread sheet.
	 *
	 * @param summaryBean
	 *            CharacterizationSummaryViewBean
	 * @param out
	 *            OutputStream
	 * @throws CharacterizationException
	 *             if error occurred.
	 */
	public void exportSummary(CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, OutputStream out)
			throws CharacterizationException;

	/**
	 * Copy and save a characterization from one sample to other samples
	 *
	 * @param charBean
	 * @param oldSample
	 * @param newSamples
	 * @param copyData
	 * @param user
	 * @throws CharacterizationException
	 * @throws NoAccessException
	 */
	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			Sample oldSample, Sample[] newSamples, boolean copyData,
			UserBean user) throws CharacterizationException, NoAccessException;
}