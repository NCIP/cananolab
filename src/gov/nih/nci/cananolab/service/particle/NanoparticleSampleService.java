package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.List;
import java.util.SortedSet;

/**
 * Interface defining service methods involving nanoparticle samples
 *
 * @author pansu
 *
 */
public interface NanoparticleSampleService {
	/**
	 *
	 * @return all particle sources
	 */
	public SortedSet<PointOfContact> findPointOfContacts()
			throws ParticleException;

	/**
	 * Persist a new nanoparticle sample or update an existing nanoparticle
	 * sample
	 *
	 * @param particleSample
	 * @throws ParticleException,
	 *             DuplicateEntriesException
	 */
	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws ParticleException, DuplicateEntriesException;

	/**
	 *
	 * @param particlePointOfContact
	 * @param nanoparticleEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws ParticleException
	 */
	public List<ParticleBean> findNanoparticleSamplesBy(
			String particlePointOfContact,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws ParticleException;

	public ParticleBean findNanoparticleSampleById(String particleId)
			throws ParticleException;

	public ParticleBean findFullNanoparticleSampleById(String particleId)
			throws Exception;

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws ParticleException;

	/**
	 * Get other particles from the given particle source
	 *
	 * @param particlePointOfContact
	 * @param particleName
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public SortedSet<SortableName> findOtherParticles(
			String particleOrganization, String particleName, UserBean user)
			throws ParticleException;

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException;

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException;

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException;

	public int getNumberOfPublicNanoparticleSamples() throws ParticleException;

	public void assignVisibility(ParticleBean particleSampleBean)
			throws Exception;

	public List<ParticleBean> getUserAccessibleParticles(
			List<ParticleBean> particles, UserBean user)
			throws ParticleException;

	public SortedSet<String> findParticleNamesByPublicationId(String publicationId)
			throws ParticleException;

	public SortedSet<String> findAllParticleNames() throws ParticleException;

}
