package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;

import java.util.List;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public interface CompositionService {
	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException;

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			UserBean user) throws CompositionException, NoAccessException;

	public void saveFunctionalizingEntity(SampleBean sampleBean,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException;

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException;

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, UserBean user) throws CompositionException,
			NoAccessException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException;

	public List<String> deleteNanomaterialEntity(NanomaterialEntity entity,
			UserBean user, Boolean removeVisibility)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public List<String> deleteFunctionalizingEntity(
			FunctionalizingEntity entity, UserBean user,
			Boolean removeVisibility) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException;

	public List<String> deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user, Boolean removeVisibility)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public List<String> deleteCompositionFile(SampleComposition comp,
			File file, UserBean user, Boolean removeVisibility)
			throws CompositionException, NoAccessException;

	public List<String> deleteComposition(SampleComposition comp,
			UserBean user, Boolean removeVisibility)
			throws ChemicalAssociationViolationException, CompositionException,
			NoAccessException;

	public CompositionBean findCompositionBySampleId(String sampleId,
			UserBean user) throws CompositionException, NoAccessException;

	/**
	 * Copy and save a nanomaterial entity from one sample to other samples
	 * 
	 * @param entityBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @param user
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException;

	/**
	 * Copy and save a functionalizing entity from one sample to other samples
	 * 
	 * @param entityBean
	 * @param oldSampleBean
	 * @param newSampleBeans
	 * @param user
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException;
}