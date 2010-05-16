package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;

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
	 *
	 * @throws SampleException
	 *             , DuplicateEntriesException
	 */
	public void saveSample(SampleBean sampleBean) throws SampleException,
			DuplicateEntriesException, NoAccessException;

	public List<String> findSampleNamesBy(String sampleName,
			String samplePointOfContact, String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException;

	public SampleBean findSampleById(String sampleId) throws SampleException,
			NoAccessException;

	public SampleBean findSampleByName(String sampleName)
			throws SampleException, NoAccessException;

	public int getNumberOfPublicSamples() throws SampleException;

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException, NoAccessException;

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException;

	public SortedSet<String> getAllOrganizationNames()
			throws PointOfContactException;

	public void savePointOfContact(PointOfContactBean pointOfContactBean)
			throws PointOfContactException, NoAccessException;

	public List<String> findSampleNamesByAdvancedSearch(
			AdvancedSampleSearchBean searchBean) throws SampleException;

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean)
			throws SampleException;

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName) throws SampleException, NoAccessException,
			DuplicateEntriesException, NotExistException;

	public List<String> deleteSample(String sampleName, Boolean removeVisibility)
			throws SampleException, NoAccessException, NotExistException;

	public int getNumberOfPublicSampleSources() throws SampleException;

	public List<String> findOtherSampleNamesFromSamePointOfContact(
			String sampleId) throws SampleException;


}
