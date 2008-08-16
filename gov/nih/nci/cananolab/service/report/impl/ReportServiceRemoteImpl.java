package gov.nih.nci.cananolab.service.report.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Remote implementation of ReportService
 * 
 * @author pansu, tanq
 * 
 */
public class ReportServiceRemoteImpl implements ReportService {
	private static Logger logger = Logger
			.getLogger(ReportServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public ReportServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new report or update an existing report
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void saveReport(Report report, String[] particleNames,
			byte[] fileData) throws ReportException {
		throw new ReportException("not implemented for grid service.");
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
		List resultList = null;
		try {			
			resultList = Arrays.asList(gridClient.getReportsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
					functionalizingEntityClassNames, functionClassNames));
			if (resultList != null) {
				for (Object obj : resultList) {
					Report report = (Report) obj;
					loadParticleSamplesForReport(report);
					reportBeans.add(new ReportBean(report));
				}
			}
			return reportBeans;
		} catch (ClassCastException e) {//to be tested
			List<Report> reportLists = ClassUtils.mapObjects(Report.class,
					resultList);
			if (reportLists != null) {
				for (Report report : reportLists) {
					if (report != null) {
						loadParticleSamplesForReport(report);
						reportBeans.add(new ReportBean(report));
					}
				}
			}
			return reportBeans;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ReportException(CaNanoLabConstants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	private void loadParticleSamplesForReport(Report report)
			throws ReportException {
		try {
			CQLQuery query = new CQLQuery();
		
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.common.Report");
			association.setRoleName("reportCollection");
		
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(report.getId().toString());
			association.setAttribute(attribute);
		
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleSample particleSample = null;
			report
					.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				particleSample = (NanoparticleSample) obj;
				report.getNanoparticleSampleCollection().add(particleSample);
			}
		} catch (Exception e) {
			String err = "Problem loading nanoparticle samples for the report : "
					+ report.getId();
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public ReportBean findReportById(String reportId) throws ReportException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Report");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(reportId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Report report = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				report = (Report) obj;
			}
			loadParticleSamplesForReport(report);
			ReportBean reportBean = new ReportBean(report);
			return reportBean;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ReportException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
		} catch (Exception e) {
			String err = "Problem finding the report by id: " + reportId;
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public int getNumberOfPublicReports() throws ReportException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Report");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of remote public reports.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public Report[] findReportsByParticleSampleId(String particleId)
		throws ReportException {
	
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Report");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSampleCollection");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);
			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
					results);
			Report report = null;
			List<Report> reports = new ArrayList<Report>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				report = (Report) obj;
				reports.add(report);
			}
			return reports.toArray(new Report[0]);
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ReportException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
		} catch (Exception e) {
			String err = "Error finding reports for particle.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
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
