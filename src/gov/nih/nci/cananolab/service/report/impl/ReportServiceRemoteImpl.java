package gov.nih.nci.cananolab.service.report.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Remote implementation of ReportService
 * 
 * @author pansu
 * 
 */
public class ReportServiceRemoteImpl implements ReportService {
	private static Logger logger = Logger
			.getLogger(ReportServiceRemoteImpl.class);
	private ReportServiceHelper helper = new ReportServiceHelper();

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
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> reportBeans = new ArrayList<ReportBean>();
		try {
			Report[] reports = gridClient.getReportsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
					functionalizingEntityClassNames, functionClassNames);
			for (Report report : reports) {
				reportBeans.add(new ReportBean(report));
			}
			return reportBeans;
		} catch (Exception e) {
			String err = "Problem finding report info.";
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
			ReportBean reportBean = new ReportBean(report);
			return reportBean;
		} catch (Exception e) {
			String err = "Problem finding the report by id: " + reportId;
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}
}
