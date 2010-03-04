package gov.nih.nci.cananolab.service.publication.helper;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of PublicationService and grid service *
 * 
 * @author tanq, pansu
 */
public class PublicationServiceHelper {
	private AuthorizationService authService;
	private Logger logger = Logger.getLogger(PublicationServiceHelper.class);

	public PublicationServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<Publication> findPublicationsBy(String title, String category,
			String sampleName, String[] researchArea, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			UserBean user) throws Exception {

		SampleServiceHelper sampleServiceHelper = new SampleServiceHelper();

		Set<String> samplePublicationIds = new HashSet<String>();
		Set<String> compositionPublicationIds = new HashSet<String>();
		Set<String> otherPublicationIds = new HashSet<String>();
		Set<String> allPublicationIds = new HashSet<String>();

		// check if sample is accessible
		if (!StringUtils.isEmpty(sampleName)) {
			Sample sample = sampleServiceHelper.findSampleByName(sampleName,
					user);
			if (sample != null) {
				for (Publication publication : sample
						.getPublicationCollection()) {
					samplePublicationIds.add(publication.getId().toString());
				}
				allPublicationIds.addAll(samplePublicationIds);
			}
		}

		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| functionClassNames != null && functionClassNames.length > 0) {
			List<Sample> samples = sampleServiceHelper.findSamplesBy(null,
					nanomaterialEntityClassNames, otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, null, null, null, user);
			for (Sample sample : samples) {
				for (Publication publication : sample
						.getPublicationCollection()) {
					compositionPublicationIds.add(publication.getId()
							.toString());
				}
			}
			allPublicationIds.addAll(compositionPublicationIds);
		}

		// can't query for the entire Publication object due to limitations in
		// pagination in SDK
		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class)
				.setProjection(Projections.distinct(Property.forName("id")));

		if (!StringUtils.isEmpty(title)) {
			TextMatchMode titleMatchMode = new TextMatchMode(title);
			// case insensitive
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(category)) {
			// case insensitive
			crit.add(Restrictions.ilike("category", category, MatchMode.EXACT));
		}

		// pubMedId
		if (!StringUtils.isEmpty(pubMedId)) {
			Long pubMedIdLong = null;
			try {
				pubMedIdLong = new Long(pubMedId);
			} catch (Exception ex) {
				// ignore
				pubMedIdLong = new Long(0);
			}
			crit.add(Restrictions.eq("pubMedId", pubMedIdLong));
		}
		if (!StringUtils.isEmpty(digitalObjectId)) {
			// case insensitive
			crit.add(Restrictions.ilike("digitalObjectId", digitalObjectId,
					MatchMode.EXACT));
		}

		// researchArea
		if (researchArea != null && researchArea.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String research : researchArea) {
				// case insensitive
				Criterion crit1 = Restrictions.ilike("researchArea", research,
						MatchMode.EXACT);
				disjunction.add(crit1);
			}
			crit.add(disjunction);
		}

		// keywords
		if (keywords != null && keywords.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createCriteria("keywordCollection", "keyword");
			for (String keyword : keywords) {
				// string wildcards from either end of keyword is entered
				keyword = StringUtils.stripWildcards(keyword);
				// case insensitive
				Criterion keywordCrit1 = Restrictions.ilike("keyword.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit1);
			}
			crit.add(disjunction);
		}

		// authors
		if (authors != null && authors.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("authorCollection", "author");
			for (String author : authors) {
				// string wildcards from either end of author is entered
				author = StringUtils.stripWildcards(author);
				// case insensitive
				Criterion crit1 = Restrictions.ilike("author.lastName", author,
						MatchMode.ANYWHERE);
				disjunction.add(crit1);
				Criterion crit2 = Restrictions.ilike("author.firstName",
						author, MatchMode.ANYWHERE);
				disjunction.add(crit2);
				Criterion crit3 = Restrictions.ilike("author.initial", author,
						MatchMode.ANYWHERE);
				disjunction.add(crit3);
			}
			crit.add(disjunction);
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			otherPublicationIds.add(obj.toString());
		}
		allPublicationIds.addAll(otherPublicationIds);

		// find the union of all publication Ids
		if (!samplePublicationIds.isEmpty()) {
			allPublicationIds.retainAll(samplePublicationIds);
		}
		if (!compositionPublicationIds.isEmpty()) {
			allPublicationIds.retainAll(compositionPublicationIds);
		}
		if (!otherPublicationIds.isEmpty()) {
			allPublicationIds.retainAll(otherPublicationIds);
		}

		List filteredResults = new ArrayList(allPublicationIds);
		if (user == null) {
			filteredResults = authService.filterNonPublic(new ArrayList(
					allPublicationIds));
		}
		List<Publication> publications = new ArrayList<Publication>();
		for (Object obj : filteredResults) {
			try {
				Publication publication = findPublicationById(obj.toString(),
						user);
				publications.add(publication);
			} catch (NoAccessException e) {
				// ignore no access exception
				logger.debug("User doesn't have access to publication with id "
						+ obj.toString());
			}
		}
		Collections.sort(publications,
				new Comparators.PublicationCategoryTitleComparator());
		return publications;
	}

	public List<String> findPublicationIdsBy(String title, String category,
			String sampleName, String[] researchArea, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			UserBean user) throws Exception {

		SampleServiceHelper sampleServiceHelper = new SampleServiceHelper();

		Set<String> samplePublicationIds = new HashSet<String>();
		Set<String> compositionPublicationIds = new HashSet<String>();
		Set<String> otherPublicationIds = new HashSet<String>();
		Set<String> allPublicationIds = new HashSet<String>();

		// check if sample is accessible
		if (sampleName != null) {
			Sample sample = sampleServiceHelper.findSampleByName(sampleName,
					user);
			if (sample != null) {
				for (Publication publication : sample
						.getPublicationCollection()) {
					samplePublicationIds.add(publication.getId().toString());
				}
				allPublicationIds.addAll(samplePublicationIds);
			}
		}

		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| functionClassNames != null && functionClassNames.length > 0) {
			List<Sample> samples = sampleServiceHelper.findSamplesBy(null,
					nanomaterialEntityClassNames, otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, null, null, null, user);
			for (Sample sample : samples) {
				for (Publication publication : sample
						.getPublicationCollection()) {
					compositionPublicationIds.add(publication.getId()
							.toString());
				}
			}
			allPublicationIds.addAll(compositionPublicationIds);
		}

		// can't query for the entire Publication object due to limitations in
		// pagination in SDK
		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class)
				.setProjection(Projections.distinct(Property.forName("id")));

		if (!StringUtils.isEmpty(title)) {
			TextMatchMode titleMatchMode = new TextMatchMode(title);
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(category)) {
			TextMatchMode categoryMatchMode = new TextMatchMode(category);
			crit.add(Restrictions.ilike("category", categoryMatchMode
					.getUpdatedText(), categoryMatchMode.getMatchMode()));
		}

		// pubMedId
		if (!StringUtils.isEmpty(pubMedId)) {
			TextMatchMode pubMedIdMatchMode = new TextMatchMode(pubMedId);
			Long pubMedIdLong = null;
			try {
				pubMedIdLong = new Long(pubMedIdMatchMode.getUpdatedText());
			} catch (Exception ex) {
				// ignore
				pubMedIdLong = new Long(0);
			}
			crit.add(Restrictions.eq("pubMedId", pubMedIdLong));
		}
		if (!StringUtils.isEmpty(digitalObjectId)) {
			TextMatchMode digitalObjectIdMatchMode = new TextMatchMode(
					digitalObjectId);
			crit.add(Restrictions.ilike("digitalObjectId",
					digitalObjectIdMatchMode.getUpdatedText(),
					digitalObjectIdMatchMode.getMatchMode()));
		}

		// researchArea
		if (researchArea != null && researchArea.length > 0) {

			Disjunction disjunction = Restrictions.disjunction();
			for (String research : researchArea) {
				Criterion crit1 = Restrictions.like("researchArea", research,
						MatchMode.ANYWHERE);
				disjunction.add(crit1);
			}
			crit.add(disjunction);
		}

		// keywords
		if (keywords != null && keywords.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createCriteria("keywordCollection", "keyword");
			for (String keyword : keywords) {
				Criterion keywordCrit1 = Restrictions.ilike("keyword.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit1);
			}
			crit.add(disjunction);
		}

		// authors
		if (authors != null && authors.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("authorCollection", "author");
			for (String author : authors) {
				Criterion crit1 = Restrictions.ilike("author.lastName", author,
						MatchMode.ANYWHERE);
				disjunction.add(crit1);
				Criterion crit2 = Restrictions.ilike("author.firstName",
						author, MatchMode.ANYWHERE);
				disjunction.add(crit2);
				Criterion crit3 = Restrictions.ilike("author.initial", author,
						MatchMode.ANYWHERE);
				disjunction.add(crit3);
			}
			crit.add(disjunction);
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			otherPublicationIds.add(obj.toString());
		}
		allPublicationIds.addAll(otherPublicationIds);

		// find the union of all publication Ids
		if (!samplePublicationIds.isEmpty()) {
			allPublicationIds.retainAll(samplePublicationIds);
		}
		if (!compositionPublicationIds.isEmpty()) {
			allPublicationIds.retainAll(compositionPublicationIds);
		}
		if (!otherPublicationIds.isEmpty()) {
			allPublicationIds.retainAll(otherPublicationIds);
		}

		List filteredResults = new ArrayList(allPublicationIds);
		if (user == null) {
			filteredResults = authService.filterNonPublic(new ArrayList(
					allPublicationIds));
		}
		List<String> publicationIds = new ArrayList<String>();
		for (Object obj : filteredResults) {
			String publicationId = obj.toString();
			if (user == null
					|| authService.checkReadPermission(user, publicationId)) {
				publicationIds.add(publicationId);
			} else {
				// ignore no access exception
				logger.debug("User doesn't have access to publication with id "
						+ obj.toString());
			}
		}
		return publicationIds;
	}

	public Publication findPublicationById(String publicationId, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class)
				.add(Property.forName("id").eq(new Long(publicationId)));
		crit.setFetchMode("authorCollection", FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Publication publication = null;
		if (!result.isEmpty()) {
			publication = (Publication) result.get(0);
			if (authService.checkReadPermission(user, publication.getId()
					.toString())) {
				return publication;
			} else {
				throw new NoAccessException();
			}
		}
		return publication;
	}

	public int getNumberOfPublicPublications() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Publication");
		List results = appService.query(crit);
		int count = 0;
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, id)) {
				count++;
			}
		}
		return count;
	}

	public String[] findSampleNamesByPublicationId(String publicationId,
			UserBean user) throws Exception {
		// check if user have access to publication first
		if (authService.checkReadPermission(user, publicationId)) {
			String query = "select sample.name from gov.nih.nci.cananolab.domain.particle.Sample as sample join sample.publicationCollection as pub where pub.id='"
					+ publicationId + "'";
			HQLCriteria crit = new HQLCriteria(query);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			SortedSet<String> names = new TreeSet<String>();
			for (Object obj : results) {
				String sampleName = obj.toString();
				if (authService.checkReadPermission(user, sampleName)) {
					names.add(sampleName);
				} else {
					logger.debug("User doesn't have access to sample "
							+ sampleName);
				}
			}
			return names.toArray(new String[0]);
		} else {
			throw new NoAccessException(
					"User doesn't have acess to the publication of id: "
							+ publicationId);
		}
	}

	public List<Publication> findPublicationsBySampleId(String sampleId,
			UserBean user) throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
		Sample sample = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.keywordCollection",
				FetchMode.JOIN);
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		List filteredResults = new ArrayList(new ArrayList(sample
				.getPublicationCollection()));
		if (user == null) {
			filteredResults = authService.filterNonPublic(new ArrayList(sample
					.getPublicationCollection()));
		}
		for (Object obj : filteredResults) {
			Publication pub = (Publication) obj;
			if (user == null
					|| authService.checkReadPermission(user, pub.getId()
							.toString())) {
				publications.add(pub);
			} else {
				logger.debug("User doesn't have access ot publication with id "
						+ pub.getId());
			}
		}
		Collections.sort(publications,
				new Comparators.PublicationCategoryTitleComparator());
		return publications;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}
}
