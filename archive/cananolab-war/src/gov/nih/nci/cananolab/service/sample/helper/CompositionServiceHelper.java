package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Service methods involving composition.
 *
 * @author pansu, tanq
 *
 */
public class CompositionServiceHelper {

	private static Logger logger = Logger
			.getLogger(CompositionServiceHelper.class);

	private AuthorizationService authService;

	public CompositionServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	// for DWR Ajax
//	public Function findFunctionById(String funcId, UserBean user)
//			throws Exception {
//		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//				.getApplicationService();
//
//		DetachedCriteria crit = DetachedCriteria.forClass(Function.class).add(
//				Property.forName("id").eq(new Long(funcId)));
//		crit.setFetchMode("targetCollection", FetchMode.JOIN);
//		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//		List result = appService.query(crit);
//		Function func = null;
//		if (!result.isEmpty()) {
//			func = (Function) result.get(0);
//			if (authService.checkReadPermission(user, func.getId().toString())) {
//				return func;
//			} else {
//				throw new NoAccessException(
//						"User doesn't have access to the function");
//			}
//		}
//		return func;
//	}
//
//	// for DWR Ajax
//	public ComposingElement findComposingElementById(String ceId, UserBean user)
//			throws Exception {
//		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//				.getApplicationService();
//
//		DetachedCriteria crit = DetachedCriteria.forClass(
//				ComposingElement.class).add(
//				Property.forName("id").eq(new Long(ceId)));
//		crit.setFetchMode("inherentFunctionCollection", FetchMode.JOIN);
//		crit.setFetchMode("inherentFunctionCollection.targetCollection",
//				FetchMode.JOIN);
//		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//		List result = appService.query(crit);
//		ComposingElement ce = null;
//		if (!result.isEmpty()) {
//			ce = (ComposingElement) result.get(0);
//			if (authService.checkReadPermission(user, ce.getId().toString())) {
//				return ce;
//			} else {
//				throw new NoAccessException(
//						"User doesn't have access to the composing element");
//			}
//		}
//		return ce;
//	}

	/**
	 * Get PubChem URL in format of
	 * http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?pubChemDS=pubchemId
	 *
	 * @param pubChemDS
	 * @param pubChemId
	 * @return PubChem URL
	 */
	public static String getPubChemURL(String pubChemDS, Long pubChemId) {
		StringBuffer sb = new StringBuffer(SampleConstants.PUBCHEM_URL);

		if (SampleConstants.BIOASSAY.equals(pubChemDS)) {
			sb.append(SampleConstants.BIOASSAY_ID);
		} else if (SampleConstants.COMPOUND.equals(pubChemDS)) {
			sb.append(SampleConstants.COMPOUND_ID);
		} else if (SampleConstants.SUBSTANCE.equals(pubChemDS)) {
			sb.append(SampleConstants.SUBSTANCE_ID);
		}

		sb.append('=').append(pubChemId);

		return sb.toString();
	}

	public List<File> findFilesByCompositionInfoId(String id, String className,
			UserBean user) throws Exception {
		List<File> fileCollection = new ArrayList<File>();
		String fullClassName = null;
		if (ClassUtils.getFullClass(className) != null) {
			fullClassName = ClassUtils.getFullClass(className).getName();
		} else {
			return null;
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select anEntity.fileCollection from " + fullClassName
				+ " anEntity where anEntity.id = " + id;

		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			File file = (File) obj;
			if (user == null
					|| authService.checkReadPermission(user, file.getId()
							.toString())) {
				fileCollection.add(file);
			} else {
				logger.debug("User doesn't have access to file of id:"
						+ file.getId());
			}

		}
		return fileCollection;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}

//	public void assignVisibility(NanomaterialEntity entity,
//			String[] visibleGroups, String owningGroup) throws Exception {
//		if (entity != null) {
//			authService.assignVisibility(entity.getId().toString(),
//					visibleGroups, owningGroup);
//			// nanomaterialEntityCollection.composingElementCollection,
//			Collection<ComposingElement> composingElementCollection = entity
//					.getComposingElementCollection();
//			if (composingElementCollection != null) {
//				for (ComposingElement composingElement : composingElementCollection) {
//					if (composingElement != null) {
//						authService.assignVisibility(composingElement.getId()
//								.toString(), visibleGroups, owningGroup);
//					}
//					// composingElementCollection.inherentFucntionCollection
//					Collection<Function> inherentFunctionCollection = composingElement
//							.getInherentFunctionCollection();
//					if (inherentFunctionCollection != null) {
//						for (Function function : inherentFunctionCollection) {
//							if (function != null) {
//								authService
//										.assignVisibility(function.getId()
//												.toString(), visibleGroups,
//												owningGroup);
//							}
//						}
//					}
//				}
//			}
//		}
//	}

//	public void assignVisibility(FunctionalizingEntity functionalizingEntity,
//			String[] visibleGroups, String owningGroup) throws Exception {
//		if (functionalizingEntity != null) {
//			authService.assignVisibility(functionalizingEntity.getId()
//					.toString(), visibleGroups, owningGroup);
//			// activation method
//			if (functionalizingEntity.getActivationMethod() != null) {
//				authService.assignVisibility(functionalizingEntity
//						.getActivationMethod().getId().toString(),
//						visibleGroups, owningGroup);
//			}
//			// functionalizingEntityCollection.functionCollection
//			Collection<Function> functionCollection = functionalizingEntity
//					.getFunctionCollection();
//			if (functionCollection != null) {
//				for (Function function : functionCollection) {
//					if (function != null) {
//						authService.assignVisibility(function.getId()
//								.toString(), visibleGroups, owningGroup);
//						if (function instanceof TargetingFunction) {
//							for (Target target : ((TargetingFunction) function)
//									.getTargetCollection()) {
//								authService
//										.assignVisibility(target.getId()
//												.toString(), visibleGroups,
//												owningGroup);
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void assignVisibility(SampleComposition comp,
//			String[] visibleGroups, String owningGroup) throws Exception {
//		authService.assignVisibility(comp.getId().toString(), visibleGroups,
//				owningGroup);
//		for (NanomaterialEntity entity : comp.getNanomaterialEntityCollection()) {
//			assignVisibility(entity, visibleGroups, owningGroup);
//		}
//		for (FunctionalizingEntity entity : comp
//				.getFunctionalizingEntityCollection()) {
//			assignVisibility(entity, visibleGroups, owningGroup);
//		}
//		for (ChemicalAssociation assoc : comp
//				.getChemicalAssociationCollection()) {
//			assignVisibility(assoc, visibleGroups, owningGroup);
//		}
//	}
//
//	public void assignVisibility(ChemicalAssociation chemicalAssociation,
//			String[] visibleGroups, String owningGroup) throws Exception {
//		if (chemicalAssociation != null) {
//			authService.assignVisibility(
//					chemicalAssociation.getId().toString(), visibleGroups,
//					owningGroup);
//			// chemicalAssociation.associatedElementA
//			if (chemicalAssociation.getAssociatedElementA() != null) {
//				authService.assignVisibility(chemicalAssociation
//						.getAssociatedElementA().getId().toString(),
//						visibleGroups, owningGroup);
//			}
//			// chemicalAssociation.associatedElementB
//			if (chemicalAssociation.getAssociatedElementB() != null) {
//				authService.assignVisibility(chemicalAssociation
//						.getAssociatedElementB().getId().toString(),
//						visibleGroups, owningGroup);
//			}
//		}
//	}
//
//	public void removeVisibility(SampleComposition comp) throws Exception {
//		authService.removeCSMEntry(comp.getId().toString());
//		for (NanomaterialEntity entity : comp.getNanomaterialEntityCollection()) {
//			removeVisibility(entity);
//		}
//		for (FunctionalizingEntity entity : comp
//				.getFunctionalizingEntityCollection()) {
//			removeVisibility(entity);
//		}
//		for (ChemicalAssociation assoc : comp
//				.getChemicalAssociationCollection()) {
//			removeVisibility(assoc);
//		}
//		if (comp.getFileCollection() != null) {
//			for (File file : comp.getFileCollection()) {
//				authService.removeCSMEntry(file.getId().toString());
//			}
//		}
//	}
//
//	public void removeVisibility(ChemicalAssociation chemicalAssociation)
//			throws Exception {
//		if (chemicalAssociation != null) {
//			authService.removeCSMEntry(chemicalAssociation.getId().toString());
//			// chemicalAssociation.associatedElementA
//			if (chemicalAssociation.getAssociatedElementA() != null) {
//				authService.removeCSMEntry(chemicalAssociation
//						.getAssociatedElementA().getId().toString());
//			}
//			// chemicalAssociation.associatedElementB
//			if (chemicalAssociation.getAssociatedElementB() != null) {
//				authService.removeCSMEntry(chemicalAssociation
//						.getAssociatedElementB().getId().toString());
//			}
//			if (chemicalAssociation.getFileCollection() != null) {
//				for (File file : chemicalAssociation.getFileCollection()) {
//					authService.removeCSMEntry(file.getId().toString());
//				}
//			}
//		}
//	}
//
//	public void removeVisibility(FunctionalizingEntity functionalizingEntity)
//			throws Exception {
//		if (functionalizingEntity != null) {
//			authService
//					.removeCSMEntry(functionalizingEntity.getId().toString());
//			if (functionalizingEntity.getActivationMethod() != null) {
//				authService.removeCSMEntry(functionalizingEntity
//						.getActivationMethod().getId().toString());
//			}
//			// functionalizingEntityCollection.functionCollection
//			Collection<Function> functionCollection = functionalizingEntity
//					.getFunctionCollection();
//			if (functionCollection != null) {
//				for (Function function : functionCollection) {
//					if (function != null) {
//						authService.removeCSMEntry(function.getId().toString());
//						if (function instanceof TargetingFunction) {
//							for (Target target : ((TargetingFunction) function)
//									.getTargetCollection()) {
//								authService.removeCSMEntry(target.getId()
//										.toString());
//							}
//						}
//					}
//				}
//			}
//			if (functionalizingEntity.getFileCollection() != null) {
//				for (File file : functionalizingEntity.getFileCollection()) {
//					authService.removeCSMEntry(file.getId().toString());
//				}
//			}
//		}
//	}
//
//	public void removeVisibility(NanomaterialEntity entity) throws Exception {
//		if (entity != null) {
//			authService.removeCSMEntry(entity.getId().toString());
//			// nanomaterialEntityCollection.composingElementCollection,
//			Collection<ComposingElement> composingElementCollection = entity
//					.getComposingElementCollection();
//			if (composingElementCollection != null) {
//				for (ComposingElement composingElement : composingElementCollection) {
//					if (composingElement != null) {
//						authService.removeCSMEntry(composingElement.getId()
//								.toString());
//					}
//					// composingElementCollection.inherentFucntionCollection
//					Collection<Function> inherentFunctionCollection = composingElement
//							.getInherentFunctionCollection();
//					if (inherentFunctionCollection != null) {
//						for (Function function : inherentFunctionCollection) {
//							if (function != null) {
//								authService.removeCSMEntry(function.getId()
//										.toString());
//							}
//						}
//					}
//				}
//			}
//			if (entity.getFileCollection() != null) {
//				for (File file : entity.getFileCollection()) {
//					authService.removeCSMEntry(file.getId().toString());
//				}
//			}
//		}
//	}
}
