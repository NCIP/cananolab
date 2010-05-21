package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Material;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.dto.common.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.MaterialBean;
import gov.nih.nci.cananolab.dto.common.SampleBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public interface CompositionService {
	public void saveMaterial(SampleBean sampleBean, MaterialBean entityBean,
			UserBean user) throws CompositionException, NoAccessException;

	public MaterialBean findMaterialById(String materialId, UserBean user)
			throws CompositionException, NoAccessException;

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException;

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException;

	public void deleteMaterial(Material material, UserBean user)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException;

	public void deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException;

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
	public void copyAndSaveMaterial(MaterialBean materialBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException;
}