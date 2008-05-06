package gov.nih.nci.cananolab.service.report;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * This class includes methods invovled in submiting and searching reports.
 * 
 * @author pansu
 * 
 */
public class ReportService {
	private static Logger logger = Logger.getLogger(ReportService.class);

	/**
	 * Persist a new report or update an existing report
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void saveReport(Report report, String[] particleNames,
			byte[] fileData) throws ReportException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			NanoparticleSampleService sampleService = new NanoparticleSampleService();
			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
			for (String name : particleNames) {
				NanoparticleSample sample = sampleService
						.findNanoparticleSampleByName(name);
				particleSamples.add(sample);
			}
			if (report.getId() != null) {
				try {
					Report dbReport = (Report) appService.get(Report.class,
							report.getId());
					// don't change createdBy and createdDate it is already
					// persisted
					report.setCreatedBy(dbReport.getCreatedBy());
					report.setCreatedDate(dbReport.getCreatedDate());
					// load fileName and uri if no new data has been uploaded or
					// no new url has been entered
					if (fileData == null || !report.getUriExternal()) {
						report.setName(dbReport.getName());
					}
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ReportException(err, e);
				}
			}
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
			FileService fileService = new FileService();
			fileService.writeFile(report, fileData);

			// TODO save other report type

		} catch (Exception e) {
			String err = "Error in saving the nanoparticle sample.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	public List<ReportBean> findReportsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames,
			String[] functionClassNames) throws ReportException,
			CaNanoLabSecurityException {
		List<ReportBean> reports = new ArrayList<ReportBean>();
		try {
			DetachedCriteria crit = DetachedCriteria.forClass(Report.class);
			if (reportTitle != null & reportTitle.length() > 0) {
				crit.add(Restrictions.ilike("title", reportTitle,
						MatchMode.ANYWHERE));
			}
			if (reportCategory != null & reportCategory.length() > 0) {
				crit.add(Restrictions.eq("category", reportCategory));
			}
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			for (Object obj : results) {
				Report report = (Report) obj;
				reports.add(new ReportBean(report));
			}
			List<ReportBean> compositionFiltered = filterByCompositions(
					nanoparticleEntityClassNames,
					functionalizingEntityClassNames, reports);
			List<ReportBean> theReports = filterByFunctions(functionClassNames,
					compositionFiltered);
			return theReports;
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	private List<ReportBean> filterByFunctions(String[] functionClassNames,
			List<ReportBean> reports) {
		NanoparticleSampleService sampleService = new NanoparticleSampleService();
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<ReportBean> filteredList = new ArrayList<ReportBean>();
			for (ReportBean report : reports) {
				SortedSet<String> storedFunctions = new TreeSet<String>();
				for (NanoparticleSample particle : ((Report) report
						.getDomainFile()).getNanoparticleSampleCollection()) {
					storedFunctions.addAll(sampleService
							.getStoredFunctionClassNames(particle));
				}
				for (String func : functionClassNames) {
					// if at least one function type matches, keep the
					// report
					if (storedFunctions.contains(func)) {
						filteredList.add(report);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return reports;
		}
	}

	private List<ReportBean> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames, List<ReportBean> reports) {
		NanoparticleSampleService sampleService = new NanoparticleSampleService();

		List<ReportBean> filteredList1 = new ArrayList<ReportBean>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (ReportBean report : reports) {
				SortedSet<String> storedEntities = new TreeSet<String>();
				for (NanoparticleSample particle : ((Report) report
						.getDomainFile()).getNanoparticleSampleCollection()) {
					storedEntities.addAll(sampleService
							.getStoredNanoparticleEntityClassNames(particle));
				}
				for (String entity : nanoparticleEntityClassNames) {
					// if at least one function type matches, keep the report
					if (storedEntities.contains(entity)) {
						filteredList1.add(report);
						break;
					}
				}
			}
		} else {
			filteredList1 = reports;
		}
		List<ReportBean> filteredList2 = new ArrayList<ReportBean>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (ReportBean report : reports) {
				SortedSet<String> storedEntities = new TreeSet<String>();

				for (NanoparticleSample particle : ((Report) report
						.getDomainFile()).getNanoparticleSampleCollection()) {
					storedEntities
							.addAll(sampleService
									.getStoredFunctionalizingEntityClassNames(particle));
				}
				for (String entity : functionalizingEntityClassNames) {
					// if at least one function type matches, keep the report
					if (storedEntities.contains(entity)) {
						filteredList2.add(report);
						break;
					}
				}
			}
		} else {
			filteredList2 = reports;
		}
		if (filteredList1.size() >= filteredList2.size()
				&& !filteredList2.isEmpty()) {
			filteredList1.retainAll(filteredList2);
			return filteredList1;
		} else {
			if (!filteredList1.isEmpty())
				filteredList2.retainAll(filteredList1);
			return filteredList2;
		}
	}

	public ReportBean findReportById(String reportId) throws ReportException {
		ReportBean reportBean = null;
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(Report.class)
					.add(Property.forName("id").eq(new Long(reportId)));
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				Report report = (Report) result.get(0);
				reportBean = new ReportBean(report);
			}
			return reportBean;
		} catch (Exception e) {
			String err = "Problem finding the report by id: " + reportId;
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}
}
