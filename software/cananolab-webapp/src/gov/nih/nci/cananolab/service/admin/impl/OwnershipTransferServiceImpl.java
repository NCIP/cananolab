/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.exception.AdministrationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Service methods for transfer ownership.
 *
 * @author lethai, pansu
 */
@Component("ownershipTransferService")
public class OwnershipTransferServiceImpl implements OwnershipTransferService {

	private static Logger logger = Logger.getLogger(OwnershipTransferServiceImpl.class);
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private ProtocolService protocolService;
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;

	private int transferOwner(SampleService sampleService,
			List<String> sampleIds, String currentOwner,
			Boolean currentOwnerIsCurator, String newOwner,
			Boolean newOwnerIsCurator) throws AdministrationException,
			NoAccessException {
		if (!(SpringSecurityUtil.getPrincipal().isCurator() && SpringSecurityUtil.getPrincipal().isAdmin())) {
			throw new NoAccessException();
		}
		int i = 0;
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			for (String sampleId : sampleIds) {
				try {
					Sample domain = this.findFullyLoadedSampleById(sampleId);
					String existingOwner = domain.getCreatedBy();
					domain.setCreatedBy(newCreatedBy(existingOwner, currentOwner, newOwner));
					appService.saveOrUpdate(domain);
					SampleComposition sampleComposition = domain.getSampleComposition();
					Collection<ChemicalAssociation> chemicalAssociation = new ArrayList<ChemicalAssociation>();
					Collection<FunctionalizingEntity> functionalizingEntity = new ArrayList<FunctionalizingEntity>();
					Collection<NanomaterialEntity> nanomaterialEntity = new ArrayList<NanomaterialEntity>();
					Collection<Characterization> characterization = new ArrayList<Characterization>();
					// poc
					PointOfContact primaryPOC = domain.getPrimaryPointOfContact();
					if (primaryPOC != null) {
						primaryPOC.setCreatedBy(newCreatedBy(primaryPOC.getCreatedBy(), currentOwner, newOwner));
						if (primaryPOC.getOrganization() != null) {
							primaryPOC.getOrganization().setCreatedBy(newCreatedBy(primaryPOC.getOrganization().getCreatedBy(), currentOwner,newOwner));
						}
						appService.saveOrUpdate(primaryPOC);
					}
					if (domain.getOtherPointOfContactCollection() != null) {
						for (PointOfContact poc : domain
								.getOtherPointOfContactCollection()) {
							poc.setCreatedBy(newCreatedBy(poc.getCreatedBy(), currentOwner, newOwner));
							if (poc.getOrganization() != null) {
								poc.getOrganization().setCreatedBy(newCreatedBy(poc.getOrganization().getCreatedBy(), currentOwner, newOwner));
							}
							appService.saveOrUpdate(poc);
						}
					}
					// composition
					if (sampleComposition != null) {
						if (sampleComposition.getFileCollection() != null) {
							for (File file : sampleComposition.getFileCollection()) {
								file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentOwner, newOwner));
								appService.saveOrUpdate(file);
							}
						}
						chemicalAssociation = sampleComposition.getChemicalAssociationCollection();
						functionalizingEntity = sampleComposition.getFunctionalizingEntityCollection();
						nanomaterialEntity = sampleComposition.getNanomaterialEntityCollection();
						characterization = domain.getCharacterizationCollection();
						if (chemicalAssociation != null) {
							for (ChemicalAssociation ca : chemicalAssociation) {
								ca.setCreatedBy(newCreatedBy(ca.getCreatedBy(), currentOwner, newOwner));
								if (ca.getFileCollection() != null) {
									for (File file : ca.getFileCollection()) {
										file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentOwner, newOwner));
									}
								}
								appService.saveOrUpdate(ca);
							}
						}
						if (functionalizingEntity != null) {
							for (FunctionalizingEntity fe : functionalizingEntity) {
								fe.setCreatedBy(newCreatedBy(fe.getCreatedBy(), currentOwner, newOwner));
								if (fe.getFileCollection() != null) {
									for (File file : fe.getFileCollection()) {
										file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentOwner, newOwner));
									}
								}
								if (fe.getFunctionCollection() != null) {
									for (Function function : fe.getFunctionCollection()) {
										function.setCreatedBy(newCreatedBy(function.getCreatedBy(), currentOwner, newOwner));
									}
								}
								appService.saveOrUpdate(fe);
							}
						}
						if (nanomaterialEntity != null) {
							for (NanomaterialEntity ne : nanomaterialEntity) {
								ne.setCreatedBy(newCreatedBy(ne.getCreatedBy(), currentOwner, newOwner));
								if (ne.getFileCollection() != null) {
									for (File file : ne.getFileCollection()) {
										file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentOwner, newOwner));
									}
								}
								if (ne.getComposingElementCollection() != null) {
									for (ComposingElement ce : ne.getComposingElementCollection()) {
										ce.setCreatedBy(newCreatedBy(ce.getCreatedBy(), currentOwner, newOwner));
										if (ce.getInherentFunctionCollection() != null) {
											for (Function function : ce.getInherentFunctionCollection()) {
												function.setCreatedBy(newCreatedBy(function.getCreatedBy(), currentOwner, newOwner));
												if (function instanceof TargetingFunction) {
													TargetingFunction tFunc = (TargetingFunction) function;
													if (tFunc.getTargetCollection() != null) {
														for (Target target : tFunc.getTargetCollection()) {
															target.setCreatedBy(newCreatedBy(
																			target.getCreatedBy(),
																			currentOwner, newOwner));
														}
													}
												}
											}
										}
									}
								}
								appService.saveOrUpdate(ne);
							}
						}
						// characterization
						if (characterization != null) {
							for (Characterization c : characterization) {
								c.setCreatedBy(newCreatedBy(c.getCreatedBy(), currentOwner, newOwner));
								if (c.getExperimentConfigCollection() != null) {
									for (ExperimentConfig config : c.getExperimentConfigCollection()) {
										config.setCreatedBy(newCreatedBy(config.getCreatedBy(), currentOwner, newOwner));
										if (config.getTechnique() != null) {
											config.getTechnique().setCreatedBy(
													newCreatedBy(config.getTechnique().getCreatedBy(),
															currentOwner, newOwner));
										}
										if (config.getInstrumentCollection() != null) {
											for (Instrument instrument : config.getInstrumentCollection()) {
												instrument.setCreatedBy(newCreatedBy(instrument.getCreatedBy(),
																currentOwner, newOwner));
											}
										}
										appService.saveOrUpdate(config);
									}
								}
								if (c.getFindingCollection() != null) {
									for (Finding finding : c.getFindingCollection()) {
										finding.setCreatedBy(newCreatedBy(finding.getCreatedBy(), currentOwner, newOwner));
										if (finding.getDatumCollection() != null) {
											for (Datum datum : finding.getDatumCollection()) {
												datum.setCreatedBy(newCreatedBy(datum.getCreatedBy(), currentOwner, newOwner));
												if (datum.getConditionCollection() != null) {
													for (Condition cond : datum.getConditionCollection()) {
														cond.setCreatedBy(newCreatedBy(cond.getCreatedBy(), currentOwner, newOwner));
													}
												}
											}
										}
										if (finding.getFileCollection() != null) {
											for (File file : finding.getFileCollection()) {
												file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentOwner, newOwner));
											}
										}
										appService.saveOrUpdate(finding);
									}
								}
								appService.saveOrUpdate(c);
							}
						}
					}
					appService.saveOrUpdate(domain);
					this.assignAndRemoveAccess(domain.getId(), currentOwner, currentOwnerIsCurator, newOwner, newOwnerIsCurator, SecureClassesEnum.SAMPLE.getClazz());

				} catch (Exception e) {
					i++;
					String error = "Error transferring ownership for sample: " + sampleId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for samples";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;
	}

	private void assignAndRemoveAccess(Long dataId, String currentOwner, Boolean currentOwnerIsCurator,
			String newOwner, Boolean newOwnerIsCurator, Class clazz) throws Exception
	{
		AccessControlInfo userAccess = springSecurityAclService.fetchAccessControlInfoForObjectForUser(dataId, clazz, currentOwner);

		// ----assign access to the owner
		// if newOwner is a curator, don't need to assign access to new owner
		// ---remove access from the owner
		// if currentOwner is a curator, don't need to remove access from current owner
		// remove currentOwner access if user accesses contains user
		if (userAccess != null && userAccess.getPermissions() != null & userAccess.getPermissions().size() > 0) {
			springSecurityAclService.saveAccessForObject(dataId, clazz, newOwner, true, userAccess.getPermissions());
			springSecurityAclService.retractAccessToObjectForSid(dataId, clazz, currentOwner);
			springSecurityAclService.updateObjectOwner(dataId, clazz, newOwner);
		}

	}

	private int transferOwner(PublicationService publicationService,
			List<String> publicationIds, String currentOwner,
			Boolean currentOwnerIsCurator, String newOwner,
			Boolean newOwnerIsCurator) throws AdministrationException,
			NoAccessException {
		// user needs to be both curator and admin
		if (!(SpringSecurityUtil.getPrincipal().isCurator() && SpringSecurityUtil.getPrincipal().isAdmin())) {
			throw new NoAccessException();
		}
		int i = 0;
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			PublicationServiceHelper helper = publicationService.getPublicationServiceHelper();
			for (String publicationId : publicationIds) {
				try {
					Publication publication = helper.findPublicationById(publicationId);
					publication.setCreatedBy(newCreatedBy(publication.getCreatedBy(), currentOwner, newOwner));
					if (publication.getAuthorCollection() != null) {
						for (Author author : publication.getAuthorCollection()) {
							author.setCreatedBy(newCreatedBy(author.getCreatedBy(), currentOwner, newOwner));
						}
					}
					appService.saveOrUpdate(publication);
					this.assignAndRemoveAccess(publication.getId(), currentOwner, currentOwnerIsCurator, newOwner,
							newOwnerIsCurator, SecureClassesEnum.PUBLICATION.getClazz());
				} catch (Exception e) {
					i++;
					String error = "Error transferring ownership for publication: " + publicationId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for publications";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;
	}

	private int transferOwner(ProtocolService protocolService,
			List<String> protocolIds, String currentOwner,
			Boolean currentOwnerIsCurator, String newOwner,
			Boolean newOwnerIsCurator) throws AdministrationException,
			NoAccessException {
		// user needs to be both curator and admin
		if (!(SpringSecurityUtil.getPrincipal().isCurator() && SpringSecurityUtil.getPrincipal().isAdmin())) {
			throw new NoAccessException();
		}
		int i = 0;
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			ProtocolServiceHelper helper = protocolService.getHelper();
			for (String protocolId : protocolIds) {
				try {
					Protocol protocol = helper.findProtocolById(protocolId);
					protocol.setCreatedBy(newCreatedBy(protocol.getCreatedBy(), currentOwner, newOwner));
					if (protocol.getFile() != null) {
						protocol.getFile().setCreatedBy(newCreatedBy(protocol.getFile().getCreatedBy(),
										currentOwner, newOwner));
					}
					appService.saveOrUpdate(protocol);
					this.assignAndRemoveAccess(protocol.getId(), currentOwner, currentOwnerIsCurator, newOwner,
							newOwnerIsCurator, SecureClassesEnum.PROTOCOL.getClazz());
				} catch (Exception e) {
					i++;
					String error = "Error transferring ownership for protocol: " + protocolId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for protocols";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;
	}

	private int transferOwner(CommunityService communityService,
			List<String> collaborationGroupIds, String currentOwner,
			Boolean currentOwnerIsCurator, String newOwner,
			Boolean newOwnerIsCurator) throws AdministrationException,
			NoAccessException
	{
		// user needs to be both curator and admin
		if (!(SpringSecurityUtil.getPrincipal().isCurator() && SpringSecurityUtil.getPrincipal().isAdmin())) {
			throw new NoAccessException();
		}
		int i = 0;
		try {
			for (String id : collaborationGroupIds) {
				try {
					communityService.assignOwner(id, newOwner);
					this.assignAndRemoveAccess(Long.valueOf(id), currentOwner,
									currentOwnerIsCurator, newOwner, newOwnerIsCurator, SecureClassesEnum.COLLABORATIONGRP.getClazz());
				} catch (Exception e) {
					i++;
					String error = "Error transferring ownership for collaboration group: " + id;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error changing the collaboration group owner";
			throw new AdministrationException(error, e);
		}
		return i;
	}

	public int transferOwner(List<String> dataIds, String dataType, String currentOwner, String newOwner) 
			throws AdministrationException, NoAccessException
	{
		int numFailures = 0;
		try {
			if (dataType.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_SAMPLE)) {
				numFailures = this.transferOwner(sampleService, dataIds, currentOwner, newOwner);
			} else if (dataType.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PROTOCOL)) {
				numFailures = this.transferOwner(protocolService, dataIds, currentOwner, newOwner);
			} else if (dataType.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PUBLICATION)) {
				numFailures = this.transferOwner(publicationService, dataIds, currentOwner, newOwner);
			} else if (dataType.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_GROUP)) {
				numFailures = this.transferOwner(communityService, dataIds, currentOwner, newOwner);
			} else {
				throw new AdministrationException("No such transfer data type is supported.");
			}
		} catch (Exception e) {
			String error = "Error in transfering ownership for type " + dataType;
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return numFailures;
	}

	/*// replace the current owner userAccess userBean with new owner userBean
	// when the loginName match
	private AccessibilityBean getNewUserAccessFromUserAccesses(List<AccessibilityBean> userAccesses, String currentOwner,
			String newOwner) {
		for (AccessibilityBean access : userAccesses) {
			UserBean currentOwnerBean = access.getUserBean();
			if (currentOwnerBean.getLoginName().equals(currentOwner)) {
				UserBean newOwnerBean = new UserBean(newOwner);
				AccessibilityBean newAccess = new AccessibilityBean();
				newAccess.setAccessBy(access.getAccessBy());
				newAccess.setRoleName(access.getRoleName());
				newAccess.setUserBean(newOwnerBean);
				return newAccess;
			}
		}
		return null;
	}*/

	/*// replace the current owner groupAccess userBean with new owner userBean
	// when the loginName match
	private AccessibilityBean getNewUserAccessFromGroupAccesses(
			SecurityService securityService,
			List<AccessibilityBean> groupAccesses, String currentOwner,
			String newOwner) throws Exception {
		String newRoleName = null;
		for (AccessibilityBean access : groupAccesses) {
			String groupName = access.getGroupName();
			if (!groupName.equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
				// check if currentOwner a member of the group, if yes obtain
				// the role. If role is CURD, break, otherwise continue
				if (securityService.isUserInGroup(currentOwner, groupName)) {
					String roleName = access.getRoleName();
					if (roleName.equals(AccessibilityBean.CSM_CURD_ROLE)) {
						newRoleName = roleName;
						break;
					} else {
						newRoleName = roleName;
					}
				}
			}
		}
		if (newRoleName != null) {
			UserBean newOwnerBean = new UserBean(newOwner);
			AccessibilityBean newAccess = new AccessibilityBean();
			newAccess.setAccessBy(AccessibilityBean.ACCESS_BY_USER);
			newAccess.setRoleName(newRoleName);
			newAccess.setUserBean(newOwnerBean);
			return newAccess;
		}

		return null;
	}*/

	public int transferOwner(BaseService dataService, List<String> dataIds, String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException {
		// check whether currentOwner and newOwner are curators
		boolean currentOwnerIsCurator = false;
		boolean newOwnerIsCurator = false;
		CananoUserDetails currOwnerDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(currentOwner);
		CananoUserDetails newOwnerDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(newOwner);
		if (currOwnerDetails != null && currOwnerDetails.isCurator()) {
			currentOwnerIsCurator = true;
		}
		if (newOwnerDetails != null && newOwnerDetails.isCurator()) {
			newOwnerIsCurator = true;
		}
		int numFailures = 0;
		if (dataService instanceof SampleService) {
			SampleService sampleService = (SampleService) dataService;
			numFailures = this.transferOwner(sampleService, dataIds, currentOwner, currentOwnerIsCurator, newOwner, newOwnerIsCurator);
		} else if (dataService instanceof ProtocolService) {
			ProtocolService protocolService = (ProtocolService) dataService;
			numFailures = this.transferOwner(protocolService, dataIds, currentOwner, currentOwnerIsCurator, newOwner, newOwnerIsCurator);
		} else if (dataService instanceof PublicationService) {
			PublicationService publicationService = (PublicationService) dataService;
			numFailures = this.transferOwner(publicationService, dataIds, currentOwner, currentOwnerIsCurator, newOwner, newOwnerIsCurator);
		} else if (dataService instanceof CommunityService) {
			CommunityService communityService = (CommunityService) dataService;
			numFailures = this.transferOwner(communityService, dataIds, currentOwner, currentOwnerIsCurator, newOwner, newOwnerIsCurator);
		} else {
			throw new AdministrationException("Not a supported service for transferring ownership");
		}
		return numFailures;
	}

	private String newCreatedBy(String existingOwner, String currentOwner, String newOwner)
	{
		// if the existing createdBy is the same as currentOwner, replace with newOwner
		if (existingOwner.equals(currentOwner)) {
			return newOwner;
		}
		int copyIndex = existingOwner.indexOf(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// if the existing createdBy is not the same as current owner and
		// doesn't contain COPY, retain existingOwner
		if (copyIndex == -1) {
			return existingOwner;
		}
		// if the existing createdBy is not the same as current owner but
		// contains COPY and contains existing createdBy, replace with newOwner
		if (existingOwner.startsWith(currentOwner)) {
			String newCreatedBy = existingOwner.replaceFirst(currentOwner + ":", newOwner + ":");
			return newCreatedBy;
		}
		return existingOwner;
	}

	private Sample findFullyLoadedSampleById(String sampleId) throws Exception
	{
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		// load composition and characterization separate because of Hibernate
		// join limitation
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(Property.forName("id").eq(new Long(sampleId)));
		Sample sample = null;

		// load composition and characterization separate because of
		// Hibernate join limitation
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization", FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}

		// fully load composition
		SampleComposition comp = this.loadComposition(sample.getId().toString());
		sample.setSampleComposition(comp);

		// fully load characterizations
		List<Characterization> chars = this.loadCharacterizations(sample.getId().toString());
		if (chars != null && !chars.isEmpty()) {
			sample.setCharacterizationCollection(new HashSet<Characterization>(chars));
		} else {
			sample.setCharacterizationCollection(null);
		}
		return sample;
	}

	private SampleComposition loadComposition(String sampleId) throws Exception
	{
		SampleComposition composition = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(SampleComposition.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection", FetchMode.JOIN);
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
		}
		return composition;
	}

	private List<Characterization> loadCharacterizations(String sampleId) throws Exception
	{
		List<Characterization> chars = new ArrayList<Characterization>();

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Characterization.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		// fully load characterization
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocol", FetchMode.JOIN);
		crit.setFetchMode("protocol.file", FetchMode.JOIN);
		crit.setFetchMode("protocol.file.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection.conditionCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);

		for (int i = 0; i < results.size(); i++) {
			Characterization achar = (Characterization) results.get(i);
			chars.add(achar);
		}
		return chars;
	}
}
