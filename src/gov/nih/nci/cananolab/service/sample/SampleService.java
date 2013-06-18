/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.SampleBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.search.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.search.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
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

	public SortedSet<String> findAllSampleNames(UserBean user)
			throws SampleException;

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

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(String sampleName, 
			AdvancedSampleSearchBean searchBean, UserBean user)
			throws SampleException;
}
