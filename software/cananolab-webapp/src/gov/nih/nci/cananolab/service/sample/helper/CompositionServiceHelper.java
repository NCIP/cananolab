/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service methods involving composition.
 *
 * @author pansu, tanq
 *
 */
@Component("compositionServiceHelper")
public class CompositionServiceHelper
{
	private static Logger logger = Logger.getLogger(CompositionServiceHelper.class);
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;

	// for DWR Ajax
	public Function findFunctionById(String funcId) throws Exception {
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(funcId), SecureClassesEnum.FUNCTION.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(funcId), SecureClassesEnum.FUNCTION.getClazz())) {
			new NoAccessException("User has no access to the function " + funcId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Function.class).add(
				Property.forName("id").eq(new Long(funcId)));
		crit.setFetchMode("targetCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Function func = null;
		if (!result.isEmpty()) {
			func = (Function) result.get(0);
		}
		return func;
	}

	// for DWR Ajax
	public ComposingElement findComposingElementById(String ceId) throws Exception
	{
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(ceId), SecureClassesEnum.COMPOSINGELEMENT.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(ceId), SecureClassesEnum.COMPOSINGELEMENT.getClazz())) {
			new NoAccessException("User has no access to the composing element " + ceId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(ComposingElement.class).add(Property.forName("id").eq(new Long(ceId)));
		crit.setFetchMode("inherentFunctionCollection", FetchMode.JOIN);
		crit.setFetchMode("inherentFunctionCollection.targetCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		ComposingElement ce = null;
		if (!result.isEmpty()) {
			ce = (ComposingElement) result.get(0);
		}
		return ce;
	}

	public List<File> findFilesByCompositionInfoId(Long sampleId, String id, String className)
			throws Exception {
		List<File> fileCollection = new ArrayList<File>();
		String fullClassName = null;
		if (ClassUtils.getFullClass(className) != null) {
			fullClassName = ClassUtils.getFullClass(className).getName();
		} else {
			return null;
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		String hql = "select anEntity.fileCollection from " + fullClassName + " anEntity where anEntity.id = " + id;

		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (int i = 0; i < results.size(); i++) {
			File file = (File) results.get(i);
			if (springSecurityAclService.currentUserHasReadPermission(sampleId, SecureClassesEnum.SAMPLE.getClazz()) ||
				springSecurityAclService.currentUserHasWritePermission(sampleId, SecureClassesEnum.SAMPLE.getClazz())) {
				fileCollection.add(file);
			} else {
				logger.debug("User doesn't have access to file of id:" + file.getId());
			}
		}
		return fileCollection;
	}

	public SampleComposition findCompositionBySampleId(String sampleId) throws Exception
	{
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz())) {
			new NoAccessException("User has no access to the sample " + sampleId);
		}
		SampleComposition composition = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(SampleComposition.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.functionCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.functionCollection.targetCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.activationMethod", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementA", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementB", FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);

		if (!result.isEmpty()) {
			composition = (SampleComposition) result.get(0);
			/*if (!springSecurityAclService.currentUserHasReadPermission(composition.getId(), SecureClassesEnum.COMPOSITION.getClazz()) &&
				!springSecurityAclService.currentUserHasWritePermission(composition.getId(), SecureClassesEnum.COMPOSITION.getClazz())) {
				throw new NoAccessException("User doesn't have access to the composition " + composition.getId());
			}*/
		}
		return composition;
	}

	public NanomaterialEntity findNanomaterialEntityById(String sampleId, String entityId) throws Exception
	{
		NanomaterialEntity entity = null;
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz())) {
			new NoAccessException("User has no access to the nanomaterial entity " + entityId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(NanomaterialEntity.class).add(
				Property.forName("id").eq(new Long(entityId)));
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection.associatedElementA", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection.associatedElementB", FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection.inherentFunctionCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection.inherentFunctionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			entity = (NanomaterialEntity) result.get(0);
		}
		return entity;
	}

	public FunctionalizingEntity findFunctionalizingEntityById(String sampleId, String entityId) throws Exception 
	{
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz())) {
			new NoAccessException("User has no access to the functionalizing entity " + entityId);
		}
		FunctionalizingEntity entity = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(FunctionalizingEntity.class).add(
				Property.forName("id").eq(new Long(entityId)));
		crit.setFetchMode("activationMethod", FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("functionCollection", FetchMode.JOIN);
		crit.setFetchMode("functionCollection.targetCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection.associatedElementA", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection.associatedElementB", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			entity = (FunctionalizingEntity) result.get(0);
		}
		return entity;
	}

	public ChemicalAssociation findChemicalAssociationById(String sampleId, String assocId) throws Exception
	{
		if (!springSecurityAclService.currentUserHasReadPermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz()) &&
			!springSecurityAclService.currentUserHasWritePermission(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz())) {
			new NoAccessException("User has no access to the chemical association " + assocId);
		}
		ChemicalAssociation assoc = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(ChemicalAssociation.class).add(Property.forName("id").eq(new Long(assocId)));
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("associatedElementA", FetchMode.JOIN);
		crit.setFetchMode("associatedElementA.nanomaterialEntity", FetchMode.JOIN);
		crit.setFetchMode("associatedElementB", FetchMode.JOIN);
		crit.setFetchMode("associatedElementB.nanomaterialEntity", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			assoc = (ChemicalAssociation) result.get(0);
		}
		return assoc;
	}
}
