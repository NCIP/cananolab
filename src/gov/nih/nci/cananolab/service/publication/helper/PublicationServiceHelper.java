package gov.nih.nci.cananolab.service.publication.helper;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
	/**
	 * Constants for generating Excel report for summary view.
	 */
	public static final String BIBLIOBRAPHY_INFO = "Bibliography Info";
	public static final String RESEARCH_CATEGORY = "Research Category";
	public static final String DESCRIPTION = "Description";
	public static final String PUB_STATUS = "Publication Status";

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

		if (title != null && title.length() > 0) {
			TextMatchMode titleMatchMode = new TextMatchMode(title);
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		if (category != null && category.length() > 0) {
			TextMatchMode categoryMatchMode = new TextMatchMode(category);
			crit.add(Restrictions.ilike("category", categoryMatchMode
					.getUpdatedText(), categoryMatchMode.getMatchMode()));
		}

		// pubMedId
		if (pubMedId != null && pubMedId.length() > 0) {
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
		if (digitalObjectId != null && digitalObjectId.length() > 0) {
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

	public static void exportDetail(PublicationBean aPub, OutputStream out)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("detailSheet");
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		int startRow = 0;
		setDetailSheet(aPub, wb, sheet, patriarch, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	private static int setDetailSheet(PublicationBean aPub, HSSFWorkbook wb,
			HSSFSheet sheet, HSSFPatriarch patriarch, int rowIndex) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		Publication publication = (Publication) aPub.getDomainFile();

		HSSFRow row = null;
		HSSFCell cell = null;
		// PubMedID
		Long pubMedId = publication.getPubMedId();
		row = sheet.createRow(rowIndex++);
		int cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Identifier"));
		if (pubMedId != null && pubMedId.intValue() > 0) {
			row.createCell(cellIndex++).setCellValue(
					new HSSFRichTextString(pubMedId.toString()));
		} else {
			String oid = publication.getDigitalObjectId();
			if (!StringUtils.isEmpty(oid)) {
				row.createCell(cellIndex++).setCellValue(
						new HSSFRichTextString(oid));
			} else {
				// row.createCell(cellIndex++).setCellValue(
				// new HSSFRichTextString(""));
			}
		}
		// publication type
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Type"));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getCategory()));

		// publication status
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString(PUB_STATUS));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getStatus()));

		// Authors
		String rowHeader = "Authors";
		StringBuffer sb = new StringBuffer();
		if (publication.getAuthorCollection() != null) {
			List<Author> authorslist = new ArrayList<Author>(publication
					.getAuthorCollection());
			Collections.sort(authorslist, new Comparator<Author>() {
				public int compare(Author o1, Author o2) {
					return (int) (o1.getCreatedDate().compareTo(o2
							.getCreatedDate()));
				}
			});
			for (Author author : authorslist) {
				sb.append(author.getFirstName());
				sb.append(' ');
				sb.append(author.getInitial());
				sb.append(' ');
				sb.append(author.getLastName());

				row = sheet.createRow(rowIndex++);
				cellIndex = 0;
				cell = row.createCell(cellIndex++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(rowHeader));
				row.createCell(cellIndex++).setCellValue(
						new HSSFRichTextString(sb.toString()));
				rowHeader = "";
				sb.setLength(0);
			}
		}

		// research area
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString(RESEARCH_CATEGORY));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getResearchArea()));

		// Title
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Title"));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getTitle()));

		// Journal
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Journal"));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getJournalName()));

		// Year
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Year"));
		int year = 0;
		if (publication.getYear() != null)
			year = publication.getYear();
		if (year > 0) {
			row.createCell(cellIndex++).setCellValue(
					new HSSFRichTextString(Integer.toString(year)));
		}

		// Volume
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Volume"));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getVolume()));

		// Pages
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Pages"));
		String startPage = publication.getStartPage();
		String endPage = publication.getEndPage();
		if ((!StringUtils.isEmpty(startPage))
				|| (!StringUtils.isEmpty(endPage))) {
			row.createCell(cellIndex++).setCellValue(
					new HSSFRichTextString(publication.getJournalName()));
		}

		// Description
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString(DESCRIPTION));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getDescription()));

		// Uploaded Publication URI
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication URI"));
		row.createCell(cellIndex++).setCellValue(
				new HSSFRichTextString(publication.getUri()));

		return rowIndex;
	}

	/**
	 * Export sample publication summary report as Excel spread sheet.
	 * 
	 * @param summaryBean
	 * @param out
	 * @throws IOException
	 */
	public static void exportSummary(PublicationSummaryViewBean summaryBean,
			OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			exportSummarySheet(summaryBean, wb);
			wb.write(out);
			out.flush();
			out.close();
		}
	}

	/**
	 * Output Excel report for sample publication summary report.
	 * 
	 * @param summaryBean
	 * @param wb
	 */
	private static void exportSummarySheet(
			PublicationSummaryViewBean summaryBean, HSSFWorkbook wb) {
		HSSFRow row = null;
		StringBuilder sb = new StringBuilder();
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		SortedMap<String, List<PublicationBean>> pubs = summaryBean
				.getCategory2Publications();
		for (String category : pubs.keySet()) {
			int rowIndex = 0;

			// Create one work sheet for each category.
			HSSFSheet sheet = wb.createSheet(category);
			row = sheet.createRow(rowIndex++);

			// Output header of report
			ExportUtils.createCell(row, 0, headerStyle, BIBLIOBRAPHY_INFO);
			ExportUtils.createCell(row, 1, headerStyle, RESEARCH_CATEGORY);
			ExportUtils.createCell(row, 2, headerStyle, DESCRIPTION);
			ExportUtils.createCell(row, 3, headerStyle, PUB_STATUS);

			// Output data of report
			List<PublicationBean> pubBeans = pubs.get(category);
			for (PublicationBean pubBean : pubBeans) {
				Publication pub = (Publication) pubBean.getDomainFile();
				row = sheet.createRow(rowIndex++);

				// Bibliography Info: cell index = 0.
				ExportUtils.createCell(row, 0, pubBean.getDisplayName());

				// Research Category: cell index = 1.
				ExportUtils.createCell(row, 1, pub.getResearchArea());

				// Description: cell index = 2.
				if (!StringUtils.isEmpty(pub.getDescription())) {
					ExportUtils.createCell(row, 2, pub.getDescription());
				}

				// Publication Status: cell index = 3.
				ExportUtils.createCell(row, 3, pub.getStatus());
			}
		}
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
