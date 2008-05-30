package gov.nih.nci.cananolab.service.particle.helper;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCompositionServiceHelper {
	private static Logger logger = Logger
			.getLogger(NanoparticleCompositionServiceHelper.class);

	public NanoparticleCompositionServiceHelper() {
	}

	public NanoparticleEntity findNanoparticleEntityById(String entityId)
			throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleEntity.class).add(
				Property.forName("id").eq(new Long(entityId)));
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setFetchMode("labFileCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		NanoparticleEntity entity = null;
		if (!result.isEmpty()) {
			entity = (NanoparticleEntity) result.get(0);
		}
		return entity;
	}

	public FunctionalizingEntity findFunctionalizingEntityById(String entityId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				FunctionalizingEntity.class).add(
				Property.forName("id").eq(new Long(entityId)));
		crit.setFetchMode("labFileCollection", FetchMode.JOIN);
		crit.setFetchMode("functionCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		FunctionalizingEntity entity = null;
		if (!result.isEmpty()) {
			entity = (FunctionalizingEntity) result.get(0);

		}
		return entity;
	}

	public ChemicalAssociation findChemicalAssocationById(String assocId)
			throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				ChemicalAssociation.class).add(
				Property.forName("id").eq(new Long(assocId)));
		crit.setFetchMode("labFileCollection", FetchMode.JOIN);
		crit.setFetchMode("associatedElementA", FetchMode.JOIN);
		crit.setFetchMode("associatedElementB", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		ChemicalAssociation assoc = null;
		if (!result.isEmpty()) {
			assoc = (ChemicalAssociation) result.get(0);
			// load nanoparticle entity associated with composing element in
			// an association
			if (assoc.getAssociatedElementA() instanceof ComposingElement) {
				NanoparticleEntity entity = findNanoparticleEntityById(((ComposingElement) assoc
						.getAssociatedElementA()).getNanoparticleEntity()
						.getId().toString());
				((ComposingElement) assoc.getAssociatedElementA())
						.setNanoparticleEntity(entity);
			}
			if (assoc.getAssociatedElementB() instanceof ComposingElement) {
				NanoparticleEntity entity = findNanoparticleEntityById(((ComposingElement) assoc
						.getAssociatedElementB()).getNanoparticleEntity()
						.getId().toString());
				((ComposingElement) assoc.getAssociatedElementB())
						.setNanoparticleEntity(entity);
			}
		}
		return assoc;
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
}
