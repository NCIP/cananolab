package gov.nih.nci.cananolab.service.particle.helper;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
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

	public List<Target> findTargetsByFunctionId(
			java.lang.String functionId) throws Exception {
		List<Target> targets = new ArrayList<Target>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select function.targetCollection from "
						+ "gov.nih.nci.cananolab.domain.particle.samplecomposition.Function function where Function.id = "
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
	
	public void assignChemicalAssociationVisibility(AuthorizationService authService,
			ChemicalAssociation chemicalAssociation, String[] visibleGroups)throws CaNanoLabSecurityException{
		if (chemicalAssociation != null) {
			this.removeChemicalAssociationVisibility(authService, chemicalAssociation);
			authService.assignVisibility(chemicalAssociation
					.getId().toString(), visibleGroups);
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService.assignVisibility(
						chemicalAssociation
								.getAssociatedElementA()
								.getId().toString(),
						visibleGroups);
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService.assignVisibility(
						chemicalAssociation
								.getAssociatedElementB()
								.getId().toString(),
						visibleGroups);
			}
		}
	}

	public void assignNanoparicleEntityVisibility(AuthorizationService authService,
		NanoparticleEntity nanoparticleEntity, String[] visibleGroups)throws CaNanoLabSecurityException{
		if (nanoparticleEntity != null) {
			authService.assignVisibility(nanoparticleEntity
					.getId().toString(), visibleGroups);		
			// nanoparticleEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanoparticleEntity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService
								.assignVisibility(composingElement
										.getId().toString(),
										visibleGroups);
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService
										.assignVisibility(
												function.getId()
														.toString(),
												visibleGroups);
							}
						}
					}
				}
			}
		}
	}
	
	public void assignFunctionalizingEntityVisibility(AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity, String[] visibleGroups)
		throws CaNanoLabSecurityException{
		if (functionalizingEntity != null) {			
			authService.assignVisibility(functionalizingEntity
					.getId().toString(), visibleGroups);		
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.assignVisibility(function
								.getId().toString(), visibleGroups);
					}
				}
			}
		}
	}	
	
	public void removeNanoparticleEntityVisibility(AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity)
		throws CaNanoLabSecurityException{
		if (nanoparticleEntity != null) {
			authService.removePublicGroup(nanoparticleEntity
					.getId().toString());			
		
			// nanoparticleEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanoparticleEntity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.removePublicGroup(composingElement
								.getId().toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService.removePublicGroup(function
										.getId().toString());
							}
						}
					}
				}
			}
		}
	}
	
	public void removeFunctionalizingEntityVisibility(AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity)
		throws CaNanoLabSecurityException{		
		if (functionalizingEntity != null) {
			authService.removePublicGroup(functionalizingEntity
					.getId().toString());		
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.removePublicGroup(function.getId()
								.toString());
					}
				}
			}
		}
	}
	
	public void removeChemicalAssociationVisibility(AuthorizationService authService,
			ChemicalAssociation chemicalAssociation)
		throws CaNanoLabSecurityException{		
		if (chemicalAssociation != null) {
			authService.removePublicGroup(chemicalAssociation
					.getId().toString());
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService
						.removePublicGroup(chemicalAssociation
								.getAssociatedElementA().getId()
								.toString());
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService
						.removePublicGroup(chemicalAssociation
								.getAssociatedElementB().getId()
								.toString());
			}
		}
	}
	public void assignSampleCompositionVisibility(AuthorizationService authService,
			NanoparticleSample particleSample, String[] visibleGroups)
		throws CaNanoLabSecurityException{		
		authService
			.removePublicGroup(particleSample.getSampleComposition().getId().toString());
		authService.assignVisibility(particleSample
					.getSampleComposition().getId().toString(),
					visibleGroups);
		
	}

}
