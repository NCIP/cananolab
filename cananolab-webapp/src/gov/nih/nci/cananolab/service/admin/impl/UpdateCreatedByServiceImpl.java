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
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.exception.AdministrationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
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
import java.util.List;

import org.apache.log4j.Logger;

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
					Sample domain = sampleService.findSampleById(sampleId,
							false).getDomain();					
					domain.setCreatedBy(newCreatedBy(domain.getCreatedBy(), currentCreatedBy, newCreatedBy));
					
					SampleComposition sampleComposition = domain
							.getSampleComposition();
					appService.saveOrUpdate(domain);
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
					// updating Sample Composition
					if (sampleComposition != null) {
						chemicalAssociation = sampleComposition
								.getChemicalAssociationCollection();
						functionalizingEntity = sampleComposition
								.getFunctionalizingEntityCollection();
						nanomaterialEntity = sampleComposition
								.getNanomaterialEntityCollection();
						characterization = domain
								.getCharacterizationCollection();

						for (ChemicalAssociation ca : chemicalAssociation) {							
							ca.setCreatedBy(newCreatedBy(ca.getCreatedBy(), currentCreatedBy, newCreatedBy));
							appService.saveOrUpdate(ca);							
							Collection<File> fileCollection = ca.getFileCollection();
							if(fileCollection != null){
								for(File file:fileCollection){
									if(file != null){
										file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
										appService.saveOrUpdate(file);		
									}
								}
							}
						}
						for (FunctionalizingEntity fe : functionalizingEntity) {							
							fe.setCreatedBy(newCreatedBy(fe.getCreatedBy(), currentCreatedBy, newCreatedBy));
							appService.saveOrUpdate(fe);
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
							
						}
						for (NanomaterialEntity ne : nanomaterialEntity) {
							ne.setCreatedBy(newCreatedBy(ne.getCreatedBy(), currentCreatedBy, newCreatedBy));
							appService.saveOrUpdate(ne);
							Collection<File> fileCollection = ne.getFileCollection();
							if(fileCollection != null){
								for(File file:fileCollection){
									if(file != null){
										file.setCreatedBy(newCreatedBy(file.getCreatedBy(), currentCreatedBy, newCreatedBy));
										appService.saveOrUpdate(file);		
									}
								}
							}
						}

						for (Characterization c : characterization) {
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
			String test = existingOwner.substring(0, currentOwner.length());
			if(test.equals(currentOwner)){
				newCreatedBy = newOwner;
			}else{
				newCreatedBy = existingOwner;
			}
		}
		return newCreatedBy;
	}
}
