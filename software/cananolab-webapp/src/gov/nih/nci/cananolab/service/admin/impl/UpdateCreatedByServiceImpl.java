package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Organization;
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
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
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

/**
 * Service methods for update createdBy field.
 *
 * @author lethai, pansu
 */
public class UpdateCreatedByServiceImpl {

	private static Logger logger = Logger
			.getLogger(UpdateCreatedByServiceImpl.class);

	private int update(SampleService sampleService, List<String> sampleIds,
			String currentCreatedBy, String newCreatedBy)
			throws AdministrationException, NoAccessException {
		int i = 0;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (String sampleId : sampleIds) {
				try {
					Sample domain = this.findFullyLoadedSampleById(sampleId);
					domain.setCreatedBy(newCreatedBy(domain.getCreatedBy(), currentCreatedBy, newCreatedBy));
					appService.saveOrUpdate(domain);
					SampleComposition sampleComposition = domain
							.getSampleComposition();
					
					Collection<ChemicalAssociation> chemicalAssociation = new ArrayList<ChemicalAssociation>();
					Collection<FunctionalizingEntity> functionalizingEntity = new ArrayList<FunctionalizingEntity>();
					Collection<NanomaterialEntity> nanomaterialEntity = new ArrayList<NanomaterialEntity>();
					Collection<Characterization> characterization = new ArrayList<Characterization>();

					// point of contact
					PointOfContact poc = domain.getPrimaryPointOfContact();
					if (poc != null) {						
						poc.setCreatedBy(newCreatedBy(poc.getCreatedBy(), currentCreatedBy, newCreatedBy));
						appService.saveOrUpdate(poc);

						// organization
						Organization organization = poc.getOrganization();
						if(organization != null){
							organization.setCreatedBy(newCreatedBy(organization.getCreatedBy(), currentCreatedBy, newCreatedBy));
							appService.saveOrUpdate(organization);
						}
					}
					if (domain.getOtherPointOfContactCollection() != null) {
						for (PointOfContact otherpoc : domain
								.getOtherPointOfContactCollection()) {
							if(otherpoc != null){
								otherpoc.setCreatedBy(newCreatedBy(otherpoc.getCreatedBy(),
										currentCreatedBy, newCreatedBy));
								appService.saveOrUpdate(otherpoc);
								Organization org = otherpoc.getOrganization();
								if(org != null){
									org.setCreatedBy(newCreatedBy(org.getCreatedBy(), currentCreatedBy, newCreatedBy));
									appService.saveOrUpdate(org);
								}
							}
						}
					}
					// updating Sample Composition
					if (sampleComposition != null) {
						if (sampleComposition.getFileCollection() != null) {
							for (File file : sampleComposition
									.getFileCollection()) {
								file
										.setCreatedBy(newCreatedBy(file
												.getCreatedBy(), currentCreatedBy,
												newCreatedBy));
								appService.saveOrUpdate(file);
							}
						}
						chemicalAssociation = sampleComposition
								.getChemicalAssociationCollection();
						functionalizingEntity = sampleComposition
								.getFunctionalizingEntityCollection();
						nanomaterialEntity = sampleComposition
								.getNanomaterialEntityCollection();
						characterization = domain
								.getCharacterizationCollection();
						if(chemicalAssociation != null){
							for (ChemicalAssociation ca : chemicalAssociation) {
								if(ca != null){
									ca.setCreatedBy(newCreatedBy(ca.getCreatedBy(), currentCreatedBy, newCreatedBy));
									Collection<File> fileCollection = ca.getFileCollection();
									if(fileCollection != null){
										for(File file:fileCollection){
											if(file != null){
												file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
												appService.saveOrUpdate(file);		
											}
										}
									}
									appService.saveOrUpdate(ca);
								}
							}
						}
						if(functionalizingEntity != null){
							for (FunctionalizingEntity fe : functionalizingEntity) {
								if(fe != null){
									fe.setCreatedBy(newCreatedBy(fe.getCreatedBy(), currentCreatedBy, newCreatedBy));
									Collection<File> fileCollection = fe.getFileCollection();
									if(fileCollection != null){
										for(File file:fileCollection){
											if(file != null){
												file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
												appService.saveOrUpdate(file);		
											}
										}
									}
									Collection<Function> functionCollection = fe.getFunctionCollection();
									if(functionCollection != null){
										for(Function f:functionCollection){
											if(f != null){
												f.setCreatedBy(newCreatedBy(f.getCreatedBy(), currentCreatedBy, newCreatedBy));
												appService.saveOrUpdate(f);		
											}
										}
									}
									appService.saveOrUpdate(fe);	
								}
							}
						}
						if(nanomaterialEntity != null){
							for (NanomaterialEntity ne : nanomaterialEntity) {
								if(ne != null){
									ne.setCreatedBy(newCreatedBy(ne.getCreatedBy(), currentCreatedBy, newCreatedBy));
									Collection<File> fileCollection = ne.getFileCollection();
									if(fileCollection != null){
										for(File file:fileCollection){
											if(file != null){
												file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
												appService.saveOrUpdate(file);		
											}
										}
									}
									if (ne.getComposingElementCollection() != null) {
										for (ComposingElement ce : ne
												.getComposingElementCollection()) {
											ce.setCreatedBy(newCreatedBy(ce
													.getCreatedBy(), currentCreatedBy,
													newCreatedBy));
											if (ce.getInherentFunctionCollection() != null) {
												for (Function function : ce
														.getInherentFunctionCollection()) {
													function.setCreatedBy(newCreatedBy(
															function.getCreatedBy(),
															currentCreatedBy, newCreatedBy));
													if (function instanceof TargetingFunction) {
														TargetingFunction tFunc = (TargetingFunction) function;
														if (tFunc.getTargetCollection() != null) {
															for (Target target : tFunc
																	.getTargetCollection()) {
																target
																		.setCreatedBy(newCreatedBy(
																				target
																						.getCreatedBy(),
																				currentCreatedBy,
																				newCreatedBy));
																appService.saveOrUpdate(target);
															}
														}
													}
													appService.saveOrUpdate(function);
												}
											}
										}
									}
									appService.saveOrUpdate(ne);
								}
							}
						}

						for (Characterization c : characterization) {
							if(c != null){
								c.setCreatedBy(newCreatedBy(c.getCreatedBy(), currentCreatedBy, newCreatedBy));
								appService.saveOrUpdate(c);
								Collection<ExperimentConfig> experimentConfigCollection = c.getExperimentConfigCollection();
								if(experimentConfigCollection != null){
									for(ExperimentConfig expConfig : experimentConfigCollection){
										if(expConfig != null){
											expConfig.setCreatedBy(newCreatedBy(expConfig.getCreatedBy(), currentCreatedBy, newCreatedBy));
											appService.saveOrUpdate(expConfig);		
										}
									}
								}
								
								Collection<Finding> findingCollection = c.getFindingCollection();						
								if(findingCollection != null){
									for(Finding f : findingCollection){
										if(f != null){
											f.setCreatedBy(newCreatedBy(f.getCreatedBy(), currentCreatedBy, newCreatedBy));
											appService.saveOrUpdate(f);	
											Collection<Datum> datumCollection = f.getDatumCollection();
											if(datumCollection != null){
												for(Datum d : datumCollection){
													if(d != null){
														d.setCreatedBy(newCreatedBy(d.getCreatedBy(), currentCreatedBy, newCreatedBy));
														appService.saveOrUpdate(d);		
													}
												}
											}
											Collection<File> fileCollection = f.getFileCollection();
											if(fileCollection != null){
												for(File file:fileCollection){
													if(file != null){
														file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
														appService.saveOrUpdate(file);		
													}
												}
											}
										}
									}								
								}
							}
						}
					}
					
				} catch (Exception e) {
					i++;
					String error = "Error updating createdBy field for sample: "
							+ sampleId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error updating createdBy field for samples";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;
	}

	private int update(PublicationService publicationService,
			List<String> publicationIds, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {

		SecurityService securityService = ((PublicationServiceLocalImpl) publicationService)
				.getSecurityService();
		int i = 0;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PublicationServiceHelper helper = new PublicationServiceHelper(
					securityService);
			for (String publicationId : publicationIds) {
				try {
					Publication publication = helper
							.findPublicationById(publicationId);					
					publication.setCreatedBy(newCreatedBy(publication.getCreatedBy(), currentCreatedBy, newCreatedBy));						
					appService.saveOrUpdate(publication);
					// author
					Collection<Author> authorCollection = publication
							.getAuthorCollection();
					for (Author a : authorCollection) {
						if(a != null ){
							a.setCreatedBy(newCreatedBy(a.getCreatedBy(), currentCreatedBy, newCreatedBy));						
							appService.saveOrUpdate(a);
						}
					}

				} catch (Exception e) {
					i++;
					String error = "Error updating createdBy field for publication: "
							+ publicationId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error updating createdBy field for publications";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;

	}

	private int update(ProtocolService protocolService,
			List<String> protocolIds, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {
		SecurityService securityService = ((ProtocolServiceLocalImpl) protocolService)
				.getSecurityService();

		int i = 0;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			ProtocolServiceHelper helper = new ProtocolServiceHelper(
					securityService);
			for (String protocolId : protocolIds) {
				try {
					Protocol protocol = helper.findProtocolById(protocolId);					
					protocol.setCreatedBy(newCreatedBy(protocol.getCreatedBy(), currentCreatedBy, newCreatedBy));
					appService.saveOrUpdate(protocol);
					//file
					File file = protocol.getFile();
					if(file != null){
						file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
						appService.saveOrUpdate(file);
					}

				} catch (Exception e) {
					i++;
					String error = "Error updating createdBy field for protocol: "
							+ protocolId;
					logger.error(error, e);
				}
			}
		} catch (Exception e) {
			String error = "Error updating createdBy field for protocols";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return i;
	}

	public int update(SecurityService securityService, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {
		String userLoginName = securityService.getUserBean().getLoginName();
		if (!("lethai".equals(userLoginName) || "pansu".equals(userLoginName))) {
			throw new NoAccessException();
		}
		int numFailures = 0;
		try {
			SampleService sampleService = new SampleServiceLocalImpl(
					securityService);
			List<String> sampleIds = sampleService
					.findSampleIdsByOwner(currentCreatedBy);
			numFailures = this.update(sampleService, sampleIds,
					currentCreatedBy, newCreatedBy);

			ProtocolService protocolService = new ProtocolServiceLocalImpl(
					securityService);
			List<String> protocolIds = protocolService
					.findProtocolIdsByOwner(currentCreatedBy);
			numFailures += this.update(protocolService, protocolIds,
					currentCreatedBy, newCreatedBy);

			PublicationService publicationService = new PublicationServiceLocalImpl(
					securityService);
			List<String> publicationIds = publicationService
					.findPublicationIdsByOwner(currentCreatedBy);
			numFailures += this.update(publicationService, publicationIds,
					currentCreatedBy, newCreatedBy);
		} catch (Exception e) {
			String error = "Error in updating createBy field " + e;
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return numFailures;
	}
	private String newCreatedBy(String existingOwner, String currentOwner, String newOwner){
		int copyIndex = existingOwner.indexOf("COPY");
		String newCreatedBy="";
		if(copyIndex ==0){
			newCreatedBy = newOwner + ":" + existingOwner.substring(copyIndex);
		}else{
			if(existingOwner.length() >= currentOwner.length()){
				String test = existingOwner.substring(0, currentOwner.length());
				if(test.equals(currentOwner)){
					newCreatedBy = newOwner;
				}else{
					newCreatedBy = existingOwner;
				}
			}
		}
		return newCreatedBy;
	}
	
	private Sample findFullyLoadedSampleById(String sampleId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// load composition and characterization separate because of Hibernate
		// join limitation
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		Sample sample = null;

		// load composition and characterization separate because of
		// Hibernate join limitation
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}

		// fully load composition
		SampleComposition comp = this
				.loadComposition(sample.getId().toString());
		sample.setSampleComposition(comp);

		// fully load characterizations
		List<Characterization> chars = this.loadCharacterizations(sample
				.getId().toString());
		if (chars != null && !chars.isEmpty()) {
			sample.setCharacterizationCollection(new HashSet<Characterization>(
					chars));
		} else {
			sample.setCharacterizationCollection(null);
		}
		return sample;
	}

	private SampleComposition loadComposition(String sampleId) throws Exception {
		SampleComposition composition = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(SampleComposition.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode(
				"nanomaterialEntityCollection.composingElementCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"functionalizingEntityCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.functionCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"functionalizingEntityCollection.functionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.activationMethod",
				FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"chemicalAssociationCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementA",
				FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementB",
				FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);

		if (!result.isEmpty()) {
			composition = (SampleComposition) result.get(0);
		}
		return composition;
	}

	private List<Characterization> loadCharacterizations(String sampleId)
			throws Exception {
		List<Characterization> chars = new ArrayList<Characterization>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(Characterization.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		// fully load characterization
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocol", FetchMode.JOIN);
		crit.setFetchMode("protocol.file", FetchMode.JOIN);
		crit.setFetchMode("protocol.file.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"findingCollection.datumCollection.conditionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);

		for (Object obj : results) {
			Characterization achar = (Characterization) obj;
			chars.add(achar);
		}
		return chars;
	}
}
