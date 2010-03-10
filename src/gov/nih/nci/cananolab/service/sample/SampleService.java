package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.List;
import java.util.SortedSet;

/**
 * Interface defining service methods involving samples
 * 
 * @author pansu
 * 
 */
public interface SampleService {
	/**
	 * Persist a new sample or update an existing sample
	 * 
	 * @param sample
	 * @throws SampleException
	 *             , DuplicateEntriesException
	 */
	public void saveSample(SampleBean sampleBean, UserBean user)
			throws SampleException, DuplicateEntriesException,
			NoAccessException;

	public List<String> findSampleNamesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList,
			UserBean user) throws SampleException;

	public SampleBean findSampleById(String sampleId, UserBean user)
			throws SampleException, NoAccessException;

	public SampleBean findSampleByName(String sampleName, UserBean user)
			throws SampleException, NoAccessException;

	public int getNumberOfPublicSamples() throws SampleException;

	public SortedSet<SortableName> findOtherSamplesFromSamePointOfContact(
			String sampleId, UserBean user) throws SampleException,
			NoAccessException;

	public PointOfContactBean findPointOfContactById(String pocId, UserBean user)
			throws PointOfContactException, NoAccessException;

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException;

	public SortedSet<String> getAllOrganizationNames(UserBean user)
			throws PointOfContactException;

	public void savePointOfContact(PointOfContactBean pointOfContactBean,
			UserBean user) throws PointOfContactException, NoAccessException;

	public List<String> findSampleNamesByAdvancedSearch(
			AdvancedSampleSearchBean searchBean, UserBean user)
			throws SampleException;

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean,
			UserBean user) throws SampleException;

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName, UserBean user) throws SampleException,
			NoAccessException, DuplicateEntriesException, NotExistException;

	public void deleteSample(String sampleName, UserBean user)
			throws SampleException, NoAccessException, NotExistException;

}
