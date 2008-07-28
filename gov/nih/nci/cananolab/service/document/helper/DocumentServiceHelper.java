package gov.nih.nci.cananolab.service.document.helper;

import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.apache.log4j.Logger;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of DocumentService and grid service *
 * 
 * @author tanq
 * 
 */
public class DocumentServiceHelper {
	private static Logger logger = Logger.getLogger(DocumentServiceHelper.class);

	private ReportServiceHelper reportHelper = new ReportServiceHelper();
	private PublicationServiceHelper publicationHelper = new PublicationServiceHelper();
	
	public int getNumberOfPublicDocuments() throws Exception {
		int count = publicationHelper.getNumberOfPublicPublications();
		count+=reportHelper.getNumberOfPublicReports();		
		return count;
	}
	

}
