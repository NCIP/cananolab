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
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

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
	
//	public List<ReportBean> findReportsBy(String reportTitle,
//			String reportCategory, String[] nanoparticleEntityClassNames,
//			String[] otherNanoparticleTypes,
//			String[] functionalizingEntityClassNames,
//			String[] otherFunctionalizingEntityTypes,
//			String[] functionClassNames, String[] otherFunctionTypes)
//			throws ReportException, CaNanoLabSecurityException {
//		List<ReportBean> reportBeans = new ArrayList<ReportBean>();
//		try {
//			//TODO XXXXXXXXXX, uncomment RemoteException
//			//mapping error here?? Report[] reports = gridClient.getReportsBy()
//			//gridClient returns a report with comments, try to match to a local report object which
//			//do not comments--> error
//			
//			//if I only add the comments to local report object, should solve problem now?
//			//when the model is load to caDSR, I need to change report to updated model (without comments)
//			//if some institute do not deploy v1.4.1, then grid service object do not match local report object
//			//==> solution: change the labFile model to original 1.4, reload to caDSR
//			//==> better solution: do mapping in this line: Report[] reports = gridClient.getReportsBy()
//			
//			//TODO: only do this when classCastExecption
//			
//			//Object[] reportObjects = gridClient.getReportsBy(reportTitle,
//			List resultList = Arrays.asList(gridClient.getReportsBy(reportTitle,
//					reportCategory, nanoparticleEntityClassNames,
//					functionalizingEntityClassNames, functionClassNames));
//			try{
//			//	Report[] reports = null;
////				reports = gridClient.getReportsBy(reportTitle,
////						reportCategory, nanoparticleEntityClassNames,
////						functionalizingEntityClassNames, functionClassNames);
//				if (reportObjects != null) {
//					for (Object obj : reportObjects) {
//						Report report = (Report) obj;
//						loadParticleSamplesForReport(report);
//						reportBeans.add(new ReportBean(report));
//					}
//				}	
//
//			}catch (Exception ex){//grid object model and local object model not in sync
//				System.out.println("############### catched ex");
//				ex.printStackTrace();
//				System.out.println("############### end of catched ex");
//				List<Report> reportLists = ClassUtils.mapObjects(new Report().getClass(), reportObjects);
//				if (reportLists != null) {
//					for (Report report : reportLists) {
//						loadParticleSamplesForReport(report);
//						reportBeans.add(new ReportBean(report));
//					}
//				}					
//			}
//		
//			return reportBeans;
//		} catch (RemoteException e) {
//			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
//			throw new ReportException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
//		} catch (Exception e) {
//			String err = "Problem finding report info.";
//			logger.error(err, e);
//			throw new ReportException(err, e);
//		}
//	}
//	
	
	
	public List<ReportBean> findReportsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> reportBeans = new ArrayList<ReportBean>();
		List resultList = null;
		try {
			//TODO XXXXXXXXXX, uncomment RemoteException
			//mapping error here?? Report[] reports = gridClient.getReportsBy()
			//gridClient returns a report with comments, try to match to a local report object which
			//do not comments--> error
			
			//if I only add the comments to local report object, should solve problem now?
			//when the model is load to caDSR, I need to change report to updated model (without comments)
			//if some institute do not deploy v1.4.1, then grid service object do not match local report object
			//==> solution: change the labFile model to original 1.4, reload to caDSR
			//==> better solution: do mapping in this line: Report[] reports = gridClient.getReportsBy()
			
			//TODO: only do this when classCastExecption	
			
			resultList = Arrays.asList(gridClient.getReportsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
					functionalizingEntityClassNames, functionClassNames));	
			//TODO: this part need to test
			if (resultList != null) {
				for (Object obj : resultList) {
					Report report = (Report) obj;
					loadParticleSamplesForReport(report);
					reportBeans.add(new ReportBean(report));
				}
			}			
			return reportBeans;
		} catch (ClassCastException e) {			
			List<Report> reportLists = ClassUtils.mapObjects(new Report().getClass(), resultList);				
			if (reportLists != null) {
				for (Report report : reportLists) {					
					if (report!=null){
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
	
	
	//TODO: not in used
	private List<Report> gridGetReportsBy(String reportTitle,
		String reportCategory)
		throws ReportException, CaNanoLabSecurityException {
		List<Report> reports = new ArrayList<Report>();
		try {			
			CQLQuery query = new CQLQuery();		
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.common.Report");/*
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.common.Report");
			association.setRoleName("reportCollection");*/
		
			//Attribute attribute = new Attribute();
			//attribute.setName("title");
			//attribute.setPredicate(Predicate.EQUAL_TO);
			//attribute.setValue(reportTitle);
			//association.setAttribute(attribute);
		
			//target.setAttribute(attribute);
			//target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				reports.add((Report) obj);
			}
		
		} catch (Exception e) {
			String err = "Problem getting reports : "
					+ reportTitle;
			logger.error(err, e);
			throw new ReportException(err, e);
		}
		return reports;
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
			Object obj = null;
			report
					.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
			boolean inSync = true;
			while (iter.hasNext()) {
				try{
					obj = iter.next();
					if (inSync){
						particleSample = (NanoparticleSample) obj;					
						report.getNanoparticleSampleCollection().add(particleSample);
					}else{
						List objList = new ArrayList();
						objList.add(obj);
						List<NanoparticleSample> particleSampleList = ClassUtils.mapObjects(new NanoparticleSample().getClass(), objList);
						particleSample = particleSampleList.get(0);
						report.getNanoparticleSampleCollection().add(particleSample);					
					}
					//TODO, need to check what kind of exception 
				}catch (Exception ex){//grid object model and local object model not in sync
					inSync = false;
					List objList = new ArrayList();
					objList.add(obj);
					List<NanoparticleSample> particleSampleList = ClassUtils.mapObjects(new NanoparticleSample().getClass(), objList);
					particleSample = particleSampleList.get(0);
					report.getNanoparticleSampleCollection().add(particleSample);					
				}
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
	
}
