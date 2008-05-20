package gov.nih.nci.cananolab.service.report;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.TextMatchMode;
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
			FileService fileService = new FileService();
			fileService.prepareSaveFile(report);
			NanoparticleSampleService sampleService = new NanoparticleSampleService();
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
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, 
			String[] otherFunctionTypes) throws ReportException,
			CaNanoLabSecurityException {
		List<ReportBean> reports = new ArrayList<ReportBean>();
		try {
			DetachedCriteria crit = DetachedCriteria.forClass(Report.class);
			if (reportTitle != null & reportTitle.length() > 0) {
				TextMatchMode titleMatchMode = new TextMatchMode(reportTitle);
				crit.add(Restrictions.ilike("title", titleMatchMode
						.getUpdatedText(), titleMatchMode.getMatchMode()));
			}
			if (reportCategory != null & reportCategory.length() > 0) {
				crit.add(Restrictions.eq("category", reportCategory));
			}
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);				
			
			crit.createAlias("nanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"sample.sampleComposition", "compo",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"compo.nanoparticleEntityCollection", "nanoEntity",
					CriteriaSpecification.LEFT_JOIN);			
			crit.createAlias(
					"compo.functionalizingEntityCollection", "funcEntity",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias(
					"compo.chemicalAssociationCollection", "asso",
					CriteriaSpecification.LEFT_JOIN);	
			crit.createAlias(
					"nanoEntity.composingElementCollection", "compoElement",
					CriteriaSpecification.LEFT_JOIN);				
			crit.createAlias(
					"compoElement.inherentFunctionCollection", "inherentFunc",
					CriteriaSpecification.LEFT_JOIN);			
			crit.createAlias(
					"funcEntity.functionCollection", "func",
					CriteriaSpecification.LEFT_JOIN);
			crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);


			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			for (Object obj : results) {
				Report report = (Report) obj;
				reports.add(new ReportBean(report));
			}
			List<ReportBean> compositionFiltered = filterByCompositions(
					nanoparticleEntityClassNames, otherNanoparticleTypes,
					functionalizingEntityClassNames, 
					otherFunctionalizingEntityTypes, reports);
			
			List<ReportBean> theReports = filterByFunctions(functionClassNames,
					otherFunctionTypes, compositionFiltered);
			
			return theReports;
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new ReportException(err, e);
		}
	}

	private List<ReportBean> filterByFunctions(String[] functionClassNames,
			String[] otherFunctionTypes, List<ReportBean> reports) {
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
				for (String other : otherFunctionTypes) {
					if (storedFunctions.contains(other)) {
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
			String[] otherNanoparticleEntityTypes,
			String[] functionalizingEntityClassNames, 
			String[] otherFunctionalizingEntityTypes,
			List<ReportBean> reports) {
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
				for (String other : otherNanoparticleEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
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
				for (String other : otherFunctionalizingEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
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
