package gov.nih.nci.cananolab.service.document.helper;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;
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
 * local implementation of DocumentService and grid service *
 * 
 * @author tanq
 * 
 */
public class DocumentServiceHelper {
	private static Logger logger = Logger.getLogger(DocumentServiceHelper.class);

	//TODO, tanq
	private ReportServiceHelper reportHelper = new ReportServiceHelper();
	
	public List<Object> findDocumentsBy(String title,
			String category, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws Exception {
		List documents = new ArrayList();
		List<Report> reports = reportHelper.findReportsBy(title, category, nanoparticleEntityClassNames, 
					otherNanoparticleTypes, functionalizingEntityClassNames, 
					otherFunctionalizingEntityTypes, functionClassNames, otherFunctionTypes);
		if (reports!=null) {
			for (Report report: reports) {
				documents.add(report);
			}
		}
		List<Publication> publications = findPublicationsBy(title, category, nanoparticleEntityClassNames, 
				otherNanoparticleTypes, functionalizingEntityClassNames, 
				otherFunctionalizingEntityTypes, functionClassNames, otherFunctionTypes);
		if (publications!=null) {
			for (Publication publication: publications) {
				documents.add(publication);
			}
		}
		return documents;
	}

	
	private List<Publication> findPublicationsBy(String title,
			String category, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class);
		if (title != null && title.length() > 0) {
			TextMatchMode titleMatchMode = new TextMatchMode(title);
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		if (category != null && category.length() > 0) {
			crit.add(Restrictions.eq("category", category));
		}
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
			Publication publication = (Publication) obj;
			publications.add(publication);
		}
		List<Publication> compositionFiltered = filterByCompositions(
				nanoparticleEntityClassNames, otherNanoparticleTypes,
				functionalizingEntityClassNames,
				otherFunctionalizingEntityTypes, publications);
		List<Publication> functionFiltered = filterByFunctions(functionClassNames,
				otherFunctionTypes, compositionFiltered);
		return functionFiltered;
	}


	private List<Publication> filterByFunctions(String[] functionClassNames,
			String[] otherFunctionTypes, List<Publication> publications) {
		NanoparticleSampleServiceHelper sampleServiceHelper = new NanoparticleSampleServiceHelper();
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<Publication> filteredList = new ArrayList<Publication>();
			for (Publication publication : publications) {
				SortedSet<String> storedFunctions = new TreeSet<String>();
				for (NanoparticleSample particle : publication
						.getNanoparticleSampleCollection()) {
					storedFunctions.addAll(sampleServiceHelper
							.getStoredFunctionClassNames(particle));
				}
				for (String func : functionClassNames) {
					// if at least one function type matches, keep the
					// publication
					if (storedFunctions.contains(func)) {
						filteredList.add(publication);
						break;
					}
				}
				for (String other : otherFunctionTypes) {
					if (storedFunctions.contains(other)) {
						filteredList.add(publication);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return publications;
		}
	}

	private List<Publication> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes, List<Publication> publications) {
		NanoparticleSampleServiceHelper sampleServiceHelper = new NanoparticleSampleServiceHelper();

		List<Publication> filteredList1 = new ArrayList<Publication>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (Publication publication : publications) {
				SortedSet<String> storedEntities = new TreeSet<String>();
				for (NanoparticleSample particle : publication
						.getNanoparticleSampleCollection()) {
					storedEntities.addAll(sampleServiceHelper
							.getStoredNanoparticleEntityClassNames(particle));
				}
				for (String entity : nanoparticleEntityClassNames) {
					// if at least one function type matches, keep the report
					if (storedEntities.contains(entity)) {
						filteredList1.add(publication);
						break;
					}
				}
				for (String other : otherNanoparticleEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
						filteredList1.add(publication);
						break;
					}
				}
			}
		} else {
			filteredList1 = publications;
		}
		List<Publication> filteredList2 = new ArrayList<Publication>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (Publication publication : publications) {
				SortedSet<String> storedEntities = new TreeSet<String>();

				for (NanoparticleSample particle : publication
						.getNanoparticleSampleCollection()) {
					storedEntities
							.addAll(sampleServiceHelper
									.getStoredFunctionalizingEntityClassNames(particle));
				}
				for (String entity : functionalizingEntityClassNames) {
					// if at least one function type matches, keep the report
					if (storedEntities.contains(entity)) {
						filteredList2.add(publication);
						break;
					}
				}
				for (String other : otherFunctionalizingEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
						filteredList2.add(publication);
						break;
					}
				}
			}
		} else {
			filteredList2 = publications;
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

	//TODO, tanq
	public Publication findPublicationById(String publicationId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class).add(
				Property.forName("id").eq(new Long(publicationId)));
		crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Publication publication = null;
		if (!result.isEmpty()) {
			publication = (Publication) result.get(0);
		}
		return publication;
	}

	//TODO, tanq
	public int getNumberOfPublicDocuments() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		int count = getNumberOfPublicPublications();
		count+=reportHelper.getNumberOfPublicReports();		
		return count;
	}
	
	private int getNumberOfPublicPublications() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria("select id from gov.nih.nci.cananolab.domain.common.Publication");
		List results = appService.query(crit);
		int count = 0;
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (publicData.contains(id)) {
				count++;
			}
		}
		return count;
	}
}
