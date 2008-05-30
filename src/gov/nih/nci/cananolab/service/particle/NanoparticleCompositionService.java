package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.exception.ParticleException;

import java.util.Collection;
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

	public void saveFunctionalizingEntity(NanoparticleSample particleSample,
			FunctionalizingEntity entity) throws ParticleCompositionException;

	public void saveChemicalAssociation(NanoparticleSample particleSample,
			ChemicalAssociation assoc) throws ParticleCompositionException;

	public void saveCompositionFile(NanoparticleSample particleSample,
			LabFile file, byte[] fileData) throws ParticleCompositionException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws ParticleCompositionException;

	public ChemicalAssociationBean findChemicalAssocationById(String assocId)
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
			LabFile file) throws ParticleCompositionException;

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

}