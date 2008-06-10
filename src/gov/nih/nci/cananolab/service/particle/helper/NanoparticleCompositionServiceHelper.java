package gov.nih.nci.cananolab.service.particle.helper;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving composition.
 * 
 * @author pansu, tanq
 * 
 */
public class NanoparticleCompositionServiceHelper {
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

	public List<Function> findInherentFunctionsByComposingElementId(
			java.lang.String composingElementId) throws Exception {
		List<Function> functions = new ArrayList<Function>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select cElement.inherentFunctionCollection from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement cElement where cElement.id = "
						+ composingElementId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Function function = (Function) obj;
			functions.add(function);
		}
		return functions;
	}

	public List<Function> findFunctionsByFunctionalizingEntityId(
			java.lang.String functionalizingEntityId) throws Exception {
		List<Function> functions = new ArrayList<Function>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select entity.functionCollection from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity entity where entity.id = "
						+ functionalizingEntityId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Function function = (Function) obj;
			functions.add(function);
		}
		return functions;
	}

	public List<Target> findTargetsByFunctionId(java.lang.String functionId)
			throws Exception {
		List<Target> targets = new ArrayList<Target>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select function.targetCollection from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.Function function where function.id = "
						+ functionId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Target target = (Target) obj;
			targets.add(target);
		}
		return targets;
	}

	public ActivationMethod findActivationMethodByFunctionalizingEntityId(
			java.lang.String functionalizingEntityId) throws Exception {
		ActivationMethod activationMethod = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select functionEntity.activationMethod from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity functionEntity where functionEntity.id = "
						+ functionalizingEntityId);
		List results = appService.query(crit);
		for (Object obj : results) {
			activationMethod = (ActivationMethod) obj;
		}
		return activationMethod;
	}

	public List<ChemicalAssociation> findChemicalAssociationsByCompositionId(
			java.lang.String compositionId) throws Exception {
		List<ChemicalAssociation> assocs = new ArrayList<ChemicalAssociation>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select sampleComposition.chemicalAssociationCollection from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition sampleComposition where sampleComposition.id = "
						+ compositionId);
		List results = appService.query(crit);
		for (Object obj : results) {
			ChemicalAssociation chemicalAssociation = (ChemicalAssociation) obj;
			assocs.add(chemicalAssociation);
		}
		return assocs;
	}

	public AssociatedElement findAssociatedElementA(
			java.lang.String chemicalAssociationId) throws Exception {
		AssociatedElement element = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select chemicalAssociation.associatedElementA from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation chemicalassociation where chemicalassociation.id = "
						+ chemicalAssociationId);
		List results = appService.query(crit);
		for (Object obj : results) {
			element = (AssociatedElement) obj;
		}
		return element;
	}

	public AssociatedElement findAssociatedElementB(
			java.lang.String chemicalAssociationId) throws Exception {
		AssociatedElement element = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select chemicalAssociation.associatedElementB from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation chemicalassociation where chemicalassociation.id = "
						+ chemicalAssociationId);
		List results = appService.query(crit);
		for (Object obj : results) {
			element = (AssociatedElement) obj;
		}
		return element;
	}

}
