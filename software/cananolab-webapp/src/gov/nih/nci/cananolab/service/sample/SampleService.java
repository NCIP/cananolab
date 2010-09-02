package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Interface defining service methods involving samples
 *
 * @author pansu
 *
 */
public interface SampleService extends BaseService {
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

	public List<String> findSampleIdsBy(String sampleName,
			String samplePointOfContact, String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException;

	public SampleBean findSampleById(String sampleId, Boolean loadAccessInfo) throws SampleException,
			NoAccessException;

	public SampleBean findSampleByName(String sampleName)
			throws SampleException, NoAccessException;

	public int getNumberOfPublicSamples() throws SampleException;

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException;

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException;

	public SortedSet<String> getAllOrganizationNames()
			throws PointOfContactException;

	public void savePointOfContact(PointOfContactBean pointOfContactBean)
			throws PointOfContactException, NoAccessException;

	public List<String> findSampleIdsByAdvancedSearch(
			AdvancedSampleSearchBean searchBean) throws SampleException;

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean)
			throws SampleException;

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName) throws SampleException, NoAccessException,
			DuplicateEntriesException, NotExistException;

	public void deleteSample(String sampleName) throws SampleException,
			NoAccessException, NotExistException;

	public int getNumberOfPublicSampleSources() throws SampleException;

	public List<String> findOtherSampleNamesFromSamePrimaryOrganization(
			String sampleId) throws SampleException;

	public void assignAccessibility(AccessibilityBean access, Sample sample)
			throws SampleException, NoAccessException;

	public void removeAccessibility(AccessibilityBean access, Sample sample)
			throws SampleException, NoAccessException;

	public List<String> removeAccesses(Sample sample, Boolean removeLater)
	throws SampleException, NoAccessException;


	public Map<String, String> findSampleIdsByOwner(String currentOwner);

	public void transferOwner(Set<String> sampleIds, String currentOwner, String newOwner)
	throws NoAccessException, Exception;

}
