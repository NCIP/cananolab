package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
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
	 * @throws SampleException,
	 *             DuplicateEntriesException
	 */
	public void saveSample(Sample sample) throws SampleException,
			DuplicateEntriesException;

	/**
	 *
	 * @param samplePointOfContact
	 * @param nanomaterialEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws SampleException
	 */
	public List<SampleBean> findSamplesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException;

	public SampleBean findSampleById(String sampleId) throws SampleException;

	public SampleBean findFullSampleById(String sampleId) throws Exception;

	public Sample findSampleByName(String sampleName) throws SampleException;

	public void retrieveVisibility(SampleBean sampleBean, UserBean user)
			throws SampleException;

	public void deleteAnnotationById(String className, Long dataId)
			throws SampleException;

	public SortedSet<String> findAllSampleNames(UserBean user)
			throws SampleException;

	public int getNumberOfPublicSamples() throws SampleException;

	public void assignVisibility(SampleBean sampleBean) throws Exception;

	public List<SampleBean> getUserAccessibleSamples(
			List<SampleBean> particles, UserBean user) throws SampleException;

	public SortedSet<String> findAllSampleNames() throws SampleException;

	public SortedSet<SortableName> findOtherSamplesFromSamePointOfContact(
			String sampleId) throws SampleException;
}
