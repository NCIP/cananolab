package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;

import java.util.List;

/**
 * Interface defining service methods involving characterizations
 * 
 * @author pansu, tanq
 * 
 */
public interface CharacterizationService {

	public void saveCharacterization(SampleBean sampleBean,
			CharacterizationBean achar, UserBean user) throws Exception;

	public CharacterizationBean findCharacterizationById(String charId,
			UserBean user) throws CharacterizationException, NoAccessException;

	public List<String> deleteCharacterization(Characterization chara,
			UserBean user, Boolean removeVisibility)
			throws CharacterizationException, NoAccessException;

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId, UserBean user) throws CharacterizationException,
			NoAccessException;

	public void saveFinding(FindingBean findingBean, UserBean user)
			throws CharacterizationException, NoAccessException;

	public FindingBean findFindingById(String findingId, UserBean user)
			throws CharacterizationException, NoAccessException;

	public List<String> deleteFinding(Finding finding, UserBean user,
			Boolean removeVisibility) throws CharacterizationException,
			NoAccessException;

	public void saveExperimentConfig(ExperimentConfigBean experimentConfigBean,
			UserBean user) throws ExperimentConfigException, NoAccessException;

	public List<String> deleteExperimentConfig(
			ExperimentConfig experimentConfig, UserBean user,
			Boolean removeVisibility) throws ExperimentConfigException,
			NoAccessException;

	/**
	 * Copy and save a characterization from one sample to other samples
	 * 
	 * @param charBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @param copyData
	 * @param user
	 * @throws CharacterizationException
	 * @throws NoAccessException
	 */
	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData, UserBean user) throws CharacterizationException,
			NoAccessException;

	public int getNumberOfPublicCharacterizations()
			throws CharacterizationException;

	public int getNumberOfPublicPhysicoChemicalCharacterizations()
			throws CharacterizationException;

	public int getNumberOfPublicInvitroCharacterizations()
			throws CharacterizationException;
}