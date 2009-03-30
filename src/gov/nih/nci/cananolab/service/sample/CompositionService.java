package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;

/**
 * Service methods involving composition.
 *
 * @author pansu
 *
 */
public interface CompositionService {
	public void saveNanomaterialEntity(Sample particleSample,
			NanomaterialEntity entity) throws CompositionException;

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId)
			throws CompositionException;

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			String entityClassName) throws CompositionException;

	public void saveFunctionalizingEntity(Sample particleSample,
			FunctionalizingEntity entity) throws CompositionException;

	public void saveChemicalAssociation(Sample particleSample,
			ChemicalAssociation assoc) throws CompositionException;

	public void saveCompositionFile(Sample particleSample,
			File file, byte[] fileData) throws CompositionException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws CompositionException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, String entityClassName)
			throws CompositionException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws CompositionException;

	public ChemicalAssociationBean findChemicalAssociationById(
			String sampleId, String assocId, String assocClassName)
			throws CompositionException;

	public void retrieveVisibility(NanomaterialEntityBean entity, UserBean user)
			throws CompositionException;

	public void retrieveVisibility(FunctionalizingEntityBean entity,
			UserBean user) throws CompositionException;

	public void retrieveVisibility(ChemicalAssociationBean assoc, UserBean user)
			throws CompositionException;

	public void deleteNanomaterialEntity(NanomaterialEntity entity)
			throws CompositionException;

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws CompositionException;

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws CompositionException;

	public void deleteCompositionFile(Sample particleSample,
			File file) throws CompositionException;

	// check if any composing elements of the nanomaterial entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanomaterialEntityBean entityBean)
			throws CompositionException;

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			ComposingElementBean composingElementBean)
			throws CompositionException;

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			FunctionalizingEntityBean entityBean)
			throws CompositionException;

	public CompositionBean findCompositionBySampleId(String sampleId)
			throws CompositionException;

	public void assignChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation) throws Exception;

	public void assignNanoparicleEntityPublicVisibility(
			AuthorizationService authService,
			NanomaterialEntity nanomaterialEntity) throws Exception;

	public void assignFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity) throws Exception;

	public void removeNanomaterialEntityPublicVisibility(
			AuthorizationService authService,
			NanomaterialEntity nanomaterialEntity) throws Exception;

	public void removeFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity) throws Exception;

	public void removeChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation) throws Exception;

	public void assignPublicVisibility(AuthorizationService authService,
			SampleComposition composition) throws Exception;
}