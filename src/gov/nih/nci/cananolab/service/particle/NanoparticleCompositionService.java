package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;

import java.util.SortedSet;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public interface NanoparticleCompositionService {
	public void saveNanoparticleEntity(NanoparticleSample particleSample,
			NanoparticleEntity entity) throws ParticleCompositionException;

	public NanoparticleEntityBean findNanoparticleEntityById(String entityId)
			throws ParticleCompositionException;

	public NanoparticleEntityBean findNanoparticleEntityById(String entityId,
			String entityClassName) throws ParticleCompositionException;

	public void saveFunctionalizingEntity(NanoparticleSample particleSample,
			FunctionalizingEntity entity) throws ParticleCompositionException;

	public void saveChemicalAssociation(NanoparticleSample particleSample,
			ChemicalAssociation assoc) throws ParticleCompositionException;

	public void saveCompositionFile(NanoparticleSample particleSample,
			File file, byte[] fileData) throws ParticleCompositionException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws ParticleCompositionException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, String entityClassName)
			throws ParticleCompositionException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws ParticleCompositionException;

	public ChemicalAssociationBean findChemicalAssociationById(
			String particleId, String assocId, String assocClassName)
			throws ParticleCompositionException;

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherFunctionalizingEntityTypes()
			throws ParticleCompositionException;

	/**
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherFunctionTypes()
			throws ParticleCompositionException;

	/**
	 * Return user-defined target types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherTargetTypes()
			throws ParticleCompositionException;

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherNanoparticleEntityTypes()
			throws ParticleCompositionException;

	/**
	 * Return user-defined chemical association types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherChemicalAssociationTypes()
			throws ParticleCompositionException;

	public void retrieveVisibility(NanoparticleEntityBean entity, UserBean user)
			throws ParticleCompositionException;

	public void retrieveVisibility(FunctionalizingEntityBean entity,
			UserBean user) throws ParticleCompositionException;

	public void retrieveVisibility(ChemicalAssociationBean assoc, UserBean user)
			throws ParticleCompositionException;

	public void deleteNanoparticleEntity(NanoparticleEntity entity)
			throws ParticleCompositionException;

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws ParticleCompositionException;

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws ParticleCompositionException;

	public void deleteCompositionFile(NanoparticleSample particleSample,
			File file) throws ParticleCompositionException;

	// check if any composing elements of the nanoparticle entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanoparticleEntityBean entityBean)
			throws ParticleCompositionException;

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			ComposingElementBean composingElementBean)
			throws ParticleCompositionException;

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			FunctionalizingEntityBean entityBean)
			throws ParticleCompositionException;

	public SampleComposition findCompositionByParticleSampleId(String particleId)
			throws ParticleCompositionException;

	public void assignChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation) throws Exception;

	public void assignNanoparicleEntityPublicVisibility(
			AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity) throws Exception;

	public void assignFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity) throws Exception;

	public void removeNanoparticleEntityPublicVisibility(
			AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity) throws Exception;

	public void removeFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity) throws Exception;

	public void removeChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation) throws Exception;

	public void assignPublicVisibility(AuthorizationService authService,
			SampleComposition composition) throws Exception;
}