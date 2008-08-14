package gov.nih.nci.cananolab.service.document.impl;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.DocumentSummaryBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.helper.DocumentServiceHelper;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * Local implementation of DocumentService
 * 
 * @author tanq
 * 
 */
public class DocumentServiceLocalImpl implements DocumentService {
	private static Logger logger = Logger
			.getLogger(DocumentServiceLocalImpl.class);
	private DocumentServiceHelper helper = new DocumentServiceHelper();

		
	//FIXME: clean up XXXXXXXX removeDocumentPublicVisibility
	public void removeDocumentPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity)
			throws CaNanoLabSecurityException {
//		if (functionalizingEntity != null) {
//			authService.removePublicGroup(functionalizingEntity.getId()
//					.toString());
//			// functionalizingEntityCollection.functionCollection
//			Collection<Function> functionCollection = functionalizingEntity
//					.getFunctionCollection();
//			if (functionCollection != null) {
//				for (Function function : functionCollection) {
//					if (function != null) {
//						authService.removePublicGroup(function.getId()
//								.toString());
//					}
//				}
//			}
//		}
	}

	public int getNumberOfPublicDocuments() throws DocumentException {
		try {
			int count = helper.getNumberOfPublicDocuments();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public reports.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}

	public Publication[] findDocumentsByParticleSampleId(String particleId)
			throws DocumentException {
		throw new DocumentException("Not implemented for local search");
	}
	
	/**
	 * if publication/report associates with multiple particle
	 * remove the entry from nanoparticle_sample_publication or nanoparticle_sample_report
	 * otherwise, remove publicVisibility and delete publication/report
	 */
	public void removeDocumentFromParticle(NanoparticleSample particle,
			Long dataId) 	throws DocumentException{
		try {
			ReportServiceHelper reportHelper = new ReportServiceHelper();
			PublicationService publicationService = new PublicationServiceLocalImpl();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
			Object publicationObject = appService.getObject(Publication.class, "id", dataId);
			if (publicationObject!=null) {
				Publication publication = publicationService.findDomainPublicationById(dataId.toString());
				Collection<NanoparticleSample> nanoparticleSampleCollection 
					= publication.getNanoparticleSampleCollection();
				if (nanoparticleSampleCollection==null || nanoparticleSampleCollection.size()==0) {
					//something wrong
					throw new DocumentException();
				}else if (nanoparticleSampleCollection.size()==1) {
					//delete
					authService.removePublicGroup(dataId.toString());	
					appService.delete(publication);
				}else {//size>1
					//remove nanoparticleSample association
					nanoparticleSampleCollection.remove(particle);
					appService.saveOrUpdate(publication);
				}
			}else {
				Object reportObject = appService.getObject(Report.class, "id", dataId);
				if (reportObject!=null) {
					Report report = reportHelper.findReportById(dataId.toString());
					Collection<NanoparticleSample> nanoparticleSampleCollection 
						= report.getNanoparticleSampleCollection();
					if (nanoparticleSampleCollection==null || nanoparticleSampleCollection.size()==0) {
						//something wrong
						throw new DocumentException();
					}else if (nanoparticleSampleCollection.size()==1) {
						//delete
						authService.removePublicGroup(dataId.toString());	
						appService.delete(report);
					}else {//size>1
						//remove nanoparticleSample association
						nanoparticleSampleCollection.remove(particle);
						appService.saveOrUpdate(report);
					}
				}
			}
		} catch (Exception e) {
			String err = "Error deleting document by ID " + dataId;
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
		
	}
	
	
	public void exportFullSummary(DocumentSummaryBean summaryBean,
			OutputStream out) throws IOException {
		DocumentServiceHelper helper = new DocumentServiceHelper();
		helper.exportFullSummary(summaryBean, out);
	}
	
	public void exportSummary(ParticleBean particleBean,
			OutputStream out) throws IOException {
		DocumentServiceHelper helper = new DocumentServiceHelper();
		helper.exportSummary(particleBean, out);
	}

}
