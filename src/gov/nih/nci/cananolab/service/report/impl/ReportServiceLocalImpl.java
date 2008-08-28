package gov.nih.nci.cananolab.service.report.impl;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Local implementation of ReportService
 * 
 * @author pansu
 * 
 */
public class ReportServiceLocalImpl implements ReportService {
	private static Logger logger = Logger
			.getLogger(ReportServiceLocalImpl.class);
	private ReportServiceHelper helper = new ReportServiceHelper();

	/**
	 * Persist a new report or update an existing report
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void saveReport(Report report, String[] particleNames,
			byte[] fileData) throws ReportException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(report);
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
			for (String name : particleNames) {
				NanoparticleSample sample = sampleService
						.findNanoparticleSampleByName(name);
				particleSamples.add(sample);
			}

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (report.getNanoparticleSampleCollection() == null) {
				report
						.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
			}
			for (NanoparticleSample sample : particleSamples) {
				report.getNanoparticleSampleCollection().add(sample);
				sample.getReportCollection().add(report);
			}
			appService.saveOrUpdate(report);

			// save to the file system fileData is not empty
			fileService.writeFile(report, fileData);

		} catch (Exception e) {
			String err = "Error in saving the report.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public List<ReportBean> findReportsBy(String reportTitle,
			String reportCategory, String nanoparticleName,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> reportBeans = new ArrayList<ReportBean>();
		try {
			Collection<Report> reports = helper.findReportsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
					otherNanoparticleTypes, functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
			
			for (Report report : reports) {
				reportBeans.add(new ReportBean(report));
			}
			Collections.sort(reportBeans,
					new CaNanoLabComparators.ReportBeanTitleComparator());
			return reportBeans;
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public ReportBean findReportById(String reportId) throws ReportException {
		try {
			Report report = helper.findReportById(reportId);
			ReportBean reportBean = new ReportBean(report);
			return reportBean;
		} catch (Exception e) {
			String err = "Problem finding the report by id: " + reportId;
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public int getNumberOfPublicReports() throws ReportException {
		try {
			int count = helper.getNumberOfPublicReports();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public reports.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public Report[] findReportsByParticleSampleId(String particleId)
			throws ReportException {
		throw new ReportException("Not implemented for local search");
	}
	
	public void exportDetail(ReportBean aReport, OutputStream out)
	throws DocumentException{		
		try {
			ReportServiceHelper helper = new ReportServiceHelper();
			helper.exportDetail(aReport, out);
		} catch (Exception e) {
			String err = "error exporting detail view for "
					+ aReport.getDomainFile().getTitle();
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
}
