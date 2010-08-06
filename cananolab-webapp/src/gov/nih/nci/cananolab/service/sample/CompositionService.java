package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

/**
 * Service methods involving composition.
 *
 * @author pansu
 *
 */
public interface CompositionService extends BaseService {
	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean) throws CompositionException,
			NoAccessException;

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId)
			throws CompositionException, NoAccessException;

	public void saveFunctionalizingEntity(SampleBean sampleBean,
			FunctionalizingEntityBean entityBean) throws CompositionException,
			NoAccessException;

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean) throws CompositionException,
			NoAccessException;

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean)
			throws CompositionException, NoAccessException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws CompositionException, NoAccessException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws CompositionException, NoAccessException;

	public void deleteNanomaterialEntity(NanomaterialEntity entity)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public void deleteCompositionFile(SampleComposition comp, File file)
			throws CompositionException, NoAccessException;

	public void deleteComposition(SampleComposition comp)
			throws ChemicalAssociationViolationException, CompositionException,
			NoAccessException;

	public CompositionBean findCompositionBySampleId(String sampleId)
			throws CompositionException, NoAccessException;

	/**
	 * Copy and save a nanomaterial entity from one sample to other samples
	 *
	 * @param entityBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans) throws CompositionException,
			NoAccessException;

	/**
	 * Copy and save a functionalizing entity from one sample to other samples
	 *
	 * @param entityBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans) throws CompositionException,
			NoAccessException;

	public void assignAccessibility(ComposingElement composingElement)
			throws CompositionException, NoAccessException;

	public void assignFileAccessibility(SampleComposition comp, File file)
			throws CompositionException, NoAccessException;

	public void assignAccessibility(Function function)
			throws CompositionException, NoAccessException;

	public void removeAccessibility(NanomaterialEntity entity, ComposingElement composingElement)
			throws CompositionException, NoAccessException;

	public void removeAccessibility(NanomaterialEntity nanomaterialEntity)
			throws CompositionException, NoAccessException;

	public void removeAccessibility(FunctionalizingEntity functionalizingEntity)
			throws CompositionException, NoAccessException;

	public void removeAccessibility(ChemicalAssociation chemicalAssociation)
			throws CompositionException, NoAccessException;

	public void removeAccessibility(FunctionalizingEntity entity, Function function)
			throws CompositionException, NoAccessException;

	public void removeFileAccessibility(SampleComposition comp, File file)
			throws CompositionException, NoAccessException;

}