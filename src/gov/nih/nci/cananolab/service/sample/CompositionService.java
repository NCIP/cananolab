package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * Service methods involving composition.
 *
 * @author pansu
 *
 */
public interface CompositionService {
	public void saveNanomaterialEntity(Sample particleSample,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException;

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			UserBean user) throws CompositionException, NoAccessException;

	public void saveFunctionalizingEntity(Sample particleSample,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException;

	public void saveChemicalAssociation(Sample particleSample,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException;

	public void saveCompositionFile(Sample particleSample, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException;

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, UserBean user) throws CompositionException,
			NoAccessException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException;

	public void deleteNanomaterialEntity(NanomaterialEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException;

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException;

	public void deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException;

	public void deleteCompositionFile(Sample particleSample, File file,
			UserBean user) throws CompositionException, NoAccessException;

	public CompositionBean findCompositionBySampleId(String sampleId,
			UserBean user) throws CompositionException, NoAccessException;

	/**
	 * Export sample composition summary report as Excel spread sheet.
	 *
	 * @param summaryBean
	 *            CompositionBean
	 * @param out
	 *            OutputStream
	 * @throws CompositionException
	 *             if error occurred.
	 */
	public void exportSummary(CompositionBean summaryBean,
			HttpServletRequest request, OutputStream out)
			throws CompositionException;

	/**
	 * Copy and save a nanomaterial entity from one sample to other samples
	 *
	 * @param entityBean
	 * @param oldSample
	 * @param newSamples
	 * @param user
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, Sample oldSample,
			Sample[] newSamples, UserBean user) throws CompositionException,
			NoAccessException;

	/**
	 * Copy and save a functionalizing entity from one sample to other samples
	 *
	 * @param entityBean
	 * @param oldSample
	 * @param newSamples
	 * @param user
	 * @throws CompositionException
	 * @throws NoAccessException
	 */
	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, Sample oldSample,
			Sample[] newSamples, UserBean user) throws CompositionException,
			NoAccessException;
}