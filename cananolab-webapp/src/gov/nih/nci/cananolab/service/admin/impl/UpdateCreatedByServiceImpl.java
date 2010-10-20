package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.exception.AdministrationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

import org.apache.log4j.Logger;

/**
 * Service methods for transfer ownership.
 *
 * @author lethai, pansu
 */
public class UpdateCreatedByServiceImpl {

	private static Logger logger = Logger
			.getLogger(UpdateCreatedByServiceImpl.class);

	private int update(SampleService sampleService, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {
		// TODO add implementation
		int numFailures = 0;
		return numFailures;
	}

	private int update(PublicationService publicationService,
			String currentCreatedBy, String newCreatedBy)
			throws AdministrationException, NoAccessException {
		// TODO add implementation
		int numFailures = 0;
		return numFailures;
	}

	private int update(ProtocolService protocolService,
			String currentCreatedBy, String newCreatedBy)
			throws AdministrationException, NoAccessException {
		// TODO add implementation
		int numFailures = 0;
		return numFailures;
	}

	public int update(SecurityService securityService, String currentCreatedBy,
			String newCreatedBy) throws AdministrationException,
			NoAccessException {
		int numFailures = 0;
		try {
			//TODO call each private method for each service
			SampleService sampleService = new SampleServiceLocalImpl(securityService);

			ProtocolService protocolService = new ProtocolServiceLocalImpl(
					securityService);
			PublicationService publicationService = new PublicationServiceLocalImpl(
					securityService);


		} catch (Exception e) {
			String error = "Error in updating created by ";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
		return numFailures;
	}

}
