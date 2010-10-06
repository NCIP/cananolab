package gov.nih.nci.cananolab.service.admin;

import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.Set;


/**
 * Interface for transfer of ownership.
 */
public interface OwnershipTransferService {
	
	public void transferOwner(SampleService sampleService, SecurityService securityService, Set<String> sampleIds, String currentOwner,
			String newOwner) throws Exception ;

	public void transferOwner(PublicationService publicationService, SecurityService securityService, Set<String> publicationIds, String currentOwner,
			String newOwner) throws Exception ;
	
	public void transferOwner(ProtocolService protocolService, SecurityService securityService, Set<String> protocolIds, String currentOwner,
			String newOwner) throws Exception ;

	}
