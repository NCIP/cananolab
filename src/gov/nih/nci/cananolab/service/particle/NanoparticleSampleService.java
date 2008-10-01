package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
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
	public SortedSet<Source> findAllParticleSources() throws ParticleException;

	/**
	 * 
	 * @return all particle sources visible to user
	 */
	public SortedSet<Source> findAllParticleSources(UserBean user)
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
	 * @param particleSource
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
	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList,
			String publicationKeywordsStr)
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
	 * @param particleSource
	 * @param particleName
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public SortedSet<SortableName> findOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException;

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException;

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException;

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException;

	public int getNumberOfPublicNanoparticleSamples() throws ParticleException;
	
	public void assignAssociatedPublicVisibility(AuthorizationService authService,
			ParticleBean particleSampleBean, String[] visibleGroups)
		throws Exception;
}
