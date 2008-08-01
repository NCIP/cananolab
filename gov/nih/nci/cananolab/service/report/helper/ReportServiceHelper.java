package gov.nih.nci.cananolab.service.report.helper;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of ReportService and grid service *
 * 
 * @author pansu
 * 
 */
public class ReportServiceHelper {
	private static Logger logger = Logger.getLogger(ReportServiceHelper.class);

	public List<Report> findReportsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws Exception {
		List<Report> reports = new ArrayList<Report>();
		DetachedCriteria crit = DetachedCriteria.forClass(Report.class);
		if (reportTitle != null && reportTitle.length() > 0) {
			TextMatchMode titleMatchMode = new TextMatchMode(reportTitle);
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
//		if (reportCategory != null && reportCategory.length() > 0) {
//			crit.add(Restrictions.eq("category", reportCategory));
//		}
		crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);

		crit.createAlias("nanoparticleSampleCollection", "sample",
				CriteriaSpecification.LEFT_JOIN).createAlias(
				"sample.sampleComposition", "compo",
				CriteriaSpecification.LEFT_JOIN).createAlias(
				"compo.nanoparticleEntityCollection", "nanoEntity",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("compo.functionalizingEntityCollection", "funcEntity",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("compo.chemicalAssociationCollection", "asso",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("nanoEntity.composingElementCollection",
				"compoElement", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("compoElement.inherentFunctionCollection",
				"inherentFunc", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("funcEntity.functionCollection", "func",
				CriteriaSpecification.LEFT_JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			Report report = (Report) obj;
			reports.add(report);
		}
		List<Report> compositionFiltered = filterByCompositions(
				nanoparticleEntityClassNames, otherNanoparticleTypes,
				functionalizingEntityClassNames,
				otherFunctionalizingEntityTypes, reports);
		List<Report> functionFiltered = filterByFunctions(functionClassNames,
				otherFunctionTypes, compositionFiltered);
		return functionFiltered;
	}

	private List<Report> filterByFunctions(String[] functionClassNames,
			String[] otherFunctionTypes, List<Report> reports) {
		NanoparticleSampleServiceHelper sampleServiceHelper = new NanoparticleSampleServiceHelper();
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<Report> filteredList = new ArrayList<Report>();
			for (Report report : reports) {
				SortedSet<String> storedFunctions = new TreeSet<String>();
				for (NanoparticleSample particle : report
						.getNanoparticleSampleCollection()) {
					storedFunctions.addAll(sampleServiceHelper
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

	private List<Report> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes, List<Report> reports) {
		NanoparticleSampleServiceHelper sampleServiceHelper = new NanoparticleSampleServiceHelper();

		List<Report> filteredList1 = new ArrayList<Report>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (Report report : reports) {
				SortedSet<String> storedEntities = new TreeSet<String>();
				for (NanoparticleSample particle : report
						.getNanoparticleSampleCollection()) {
					storedEntities.addAll(sampleServiceHelper
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
		List<Report> filteredList2 = new ArrayList<Report>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (Report report : reports) {
				SortedSet<String> storedEntities = new TreeSet<String>();

				for (NanoparticleSample particle : report
						.getNanoparticleSampleCollection()) {
					storedEntities
							.addAll(sampleServiceHelper
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

	public Report findReportById(String reportId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Report.class).add(
				Property.forName("id").eq(new Long(reportId)));
		crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Report report = null;
		if (!result.isEmpty()) {
			report = (Report) result.get(0);
		}
		return report;
	}

	public int getNumberOfPublicReports() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria("select id from gov.nih.nci.cananolab.domain.common.Report");
		List results = appService.query(crit);
		List<String> publicIds = new ArrayList<String>();
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (publicData.contains(id)) {
				publicIds.add(id);
			}
		}
		return publicIds.size();
	}
}
