package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
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

	private int update(SampleService sampleService, List<String> sampleIds, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {
		int i = 0;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (String sampleId : sampleIds) {
				try {
					Sample domain = sampleService.findSampleById(sampleId,
							false).getDomain();
					String existingOwner = domain.getCreatedBy();
					System.out.println("ExistingOwner: " + existingOwner);
					if(existingOwner.substring(0, 3).equalsIgnoreCase("COPY")){
						domain.setCreatedBy(newCreatedBy + ":" + existingOwner);
					}else{
						String test = existingOwner.substring(0, currentCreatedBy.length());
						System.out.println("Test: " + test);
						if(test.equals(currentCreatedBy)){
							domain.setCreatedBy(newCreatedBy);
						}
					}
					SampleComposition sampleComposition = domain
							.getSampleComposition();
					appService.saveOrUpdate(domain);
					Collection<ChemicalAssociation> chemicalAssociation = new ArrayList<ChemicalAssociation>();
					Collection<FunctionalizingEntity> functionalizingEntity = new ArrayList<FunctionalizingEntity>();
					Collection<NanomaterialEntity> nanomaterialEntity = new ArrayList<NanomaterialEntity>();
					Collection<Characterization> characterization = new ArrayList<Characterization>();

					//point of contact
					PointOfContact poc = domain.getPrimaryPointOfContact();
					String existingPOC = poc.getCreatedBy();
					System.out.println("ExistingOwner: " + existingPOC);
					if(existingPOC.substring(0, 3).equalsIgnoreCase("COPY")){
						poc.setCreatedBy(newCreatedBy + ":" + existingPOC);
					}else{
						String test = existingPOC.substring(0, currentCreatedBy.length());
						System.out.println("Test: " + test);
						if(test.equals(currentCreatedBy)){
							poc.setCreatedBy(newCreatedBy);
						}
					}
					appService.saveOrUpdate(poc);
					//organization
					Organization organization = poc.getOrganization(); 
					String existingOrg = poc.getCreatedBy();
					System.out.println("ExistingCreatedBy: " + existingOrg);
					if(existingOrg.substring(0, 3).equalsIgnoreCase("COPY")){
						organization.setCreatedBy(newCreatedBy + ":" + existingOrg);
					}else{
						String test = existingOrg.substring(0, currentCreatedBy.length());
						System.out.println("Test: " + test);
						if(test.equals(currentCreatedBy)){
							organization.setCreatedBy(newCreatedBy);
						}
					}
					appService.saveOrUpdate(organization);
					
					//updating Sample Composition
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
							String existingChemicalAsso = ca.getCreatedBy();
							System.out.println("ExistingCreatedBy: " + existingChemicalAsso);
							if(existingChemicalAsso.substring(0, 3).equalsIgnoreCase("COPY")){
								ca.setCreatedBy(newCreatedBy + ":" + existingChemicalAsso);
							}else{
								String test = existingChemicalAsso.substring(0, currentCreatedBy.length());
								System.out.println("Test: " + test);
								if(test.equals(currentCreatedBy)){
									ca.setCreatedBy(newCreatedBy);
								}
							}
							//ca.setCreatedBy(newCreatedBy);
							appService.saveOrUpdate(ca);
						}
						for (FunctionalizingEntity fe : functionalizingEntity) {
							String existingFE = fe.getCreatedBy();
							System.out.println("ExistingCreatedBy: " + existingFE);
							if(existingFE.substring(0, 3).equalsIgnoreCase("COPY")){
								fe.setCreatedBy(newCreatedBy + ":" + existingFE);
							}else{
								String test = existingFE.substring(0, currentCreatedBy.length());
								System.out.println("Test: " + test);
								if(test.equals(currentCreatedBy)){
									fe.setCreatedBy(newCreatedBy);
								}
							}
							//fe.setCreatedBy(newCreatedBy);
							appService.saveOrUpdate(fe);
						}
						for (NanomaterialEntity ne : nanomaterialEntity) {
							String existingNE = ne.getCreatedBy();
							System.out.println("ExistingCreatedBy: " + existingNE);
							if(existingNE.substring(0, 3).equalsIgnoreCase("COPY")){
								ne.setCreatedBy(newCreatedBy + ":" + existingNE);
							}else{
								String test = existingNE.substring(0, currentCreatedBy.length());
								System.out.println("Test: " + test);
								if(test.equals(currentCreatedBy)){
									ne.setCreatedBy(newCreatedBy);
								}
							}
							//ne.setCreatedBy(newCreatedBy);
							appService.saveOrUpdate(ne);
						}

						for (Characterization c : characterization) {
							String existingChar = c.getCreatedBy();
							System.out.println("ExistingCreatedBy: " + existingChar);
							if(existingChar.substring(0, 3).equalsIgnoreCase("COPY")){
								c.setCreatedBy(newCreatedBy + ":" + existingChar);
							}else{
								String test = existingChar.substring(0, currentCreatedBy.length());
								System.out.println("Test: " + test);
								if(test.equals(currentCreatedBy)){
									c.setCreatedBy(newCreatedBy);
								}
							}
							appService.saveOrUpdate(c);
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

	private int update(PublicationService publicationService, List<String> publicationIds,
			String currentCreatedBy, String newCreatedBy)
			throws AdministrationException, NoAccessException {
		
		SecurityService securityService = ((PublicationServiceLocalImpl) publicationService)
		.getSecurityService();
		int i=0;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PublicationServiceHelper helper = new PublicationServiceHelper(
					securityService);
			for (String publicationId : publicationIds) {
				try {
					Publication publication = helper
							.findPublicationById(publicationId);
					String existingCreatedBy = publication.getCreatedBy();
					System.out.println("ExistingCreatedBy: " + existingCreatedBy);
					if(existingCreatedBy.substring(0, 3).equalsIgnoreCase("COPY")){
						publication.setCreatedBy(newCreatedBy + ":" + existingCreatedBy);
					}else{
						String test = existingCreatedBy.substring(0, currentCreatedBy.length());
						if(test.equals(currentCreatedBy)){
							publication.setCreatedBy(newCreatedBy);
						}
					}
										
					appService.saveOrUpdate(publication);
					//datum
					Collection<Datum> datumCollection = publication.getDatumCollection();
					for(Datum d : datumCollection){
						String existingDatum = d.getCreatedBy();
						System.out.println("ExistingCreatedBy: " + existingDatum);
						if(existingDatum.substring(0, 3).equalsIgnoreCase("COPY")){
							d.setCreatedBy(newCreatedBy + ":" + existingCreatedBy);
						}else{
							String test = existingDatum.substring(0, currentCreatedBy.length());
							if(test.equals(currentCreatedBy)){
								d.setCreatedBy(newCreatedBy);
							}
						}
						appService.saveOrUpdate(d);
					}
					//finding
					Collection<Finding> findingCollection = publication.getFindingCollection();
					for(Finding f : findingCollection){
						String existingFinding = f.getCreatedBy();
						System.out.println("ExistingCreatedBy: " + existingFinding);
						if(existingFinding.substring(0, 3).equalsIgnoreCase("COPY")){
							f.setCreatedBy(newCreatedBy + ":" + existingCreatedBy);
						}else{
							String test = existingFinding.substring(0, currentCreatedBy.length());
							if(test.equals(currentCreatedBy)){
								f.setCreatedBy(newCreatedBy);
							}
						}
						appService.saveOrUpdate(f);
					}
					//author
					Collection<Author> authorCollection = publication.getAuthorCollection();
					for(Author a : authorCollection){
						String existingAuthor = a.getCreatedBy();
						System.out.println("ExistingCreatedBy: " + existingAuthor);
						if(existingAuthor.substring(0, 3).equalsIgnoreCase("COPY")){
							a.setCreatedBy(newCreatedBy + ":" + existingCreatedBy);
						}else{
							String test = existingAuthor.substring(0, currentCreatedBy.length());
							if(test.equals(currentCreatedBy)){
								a.setCreatedBy(newCreatedBy);
							}
						}
						appService.saveOrUpdate(a);
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

	private int update(ProtocolService protocolService, List<String> protocolIds,
			String currentCreatedBy, String newCreatedBy)
			throws AdministrationException, NoAccessException {
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
					String existingCreatedBy = protocol.getCreatedBy();
					System.out.println("ExistingCreatedBy: " + existingCreatedBy);
					if(existingCreatedBy.substring(0, 4).equalsIgnoreCase("COPY")){
						protocol.setCreatedBy(newCreatedBy + ":" + existingCreatedBy);
					}else{
						String test = existingCreatedBy.substring(0, currentCreatedBy.length());
						if(test.equals(currentCreatedBy)){
							protocol.setCreatedBy(newCreatedBy);
						}						
					}
				
					appService.saveOrUpdate(protocol);
					
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
		if(!"lethai".equals(userLoginName) || !"pansu".equals(userLoginName)){
			throw new NoAccessException();
		}
		int numFailures = 0;
		try {
			SampleService sampleService = new SampleServiceLocalImpl(securityService);
			List<String> sampleIds = sampleService.findSampleIdsByOwner(currentCreatedBy);
			numFailures = this.update(sampleService, sampleIds, currentCreatedBy,
					newCreatedBy);
			
			ProtocolService protocolService = new ProtocolServiceLocalImpl(securityService);
			List<String> protocolIds = protocolService.findProtocolIdsByOwner(currentCreatedBy);
			numFailures+= this.update(protocolService, protocolIds, currentCreatedBy,
					newCreatedBy);
			
			PublicationService publicationService = new PublicationServiceLocalImpl(securityService);		
			List<String> publicationIds = publicationService.findPublicationIdsByOwner(currentCreatedBy);
			numFailures+= this.update(publicationService, publicationIds, currentCreatedBy,
					newCreatedBy);
		} catch (Exception e) {
			String error = "Error in updating createBy field "
					+ e;
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return numFailures;
	}
}
