package gov.nih.nci.cananolab.service.publication.helper;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;

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
	public static final String TITLE = "Title";
	public static final String AUTHORS = "Author(s)";
	public static final String BIBLIOBRAPHY_INFO = "Bibliography Info";
	public static final String ABSTRACT = "Abstract/Full Text";
	public static final String RESEARCH_CATEGORY = "Research Category";
	public static final String CREATED_DATE = "Created Date";
	public static final String PMID = "PMID: ";
	
	public List<Publication> findPublicationsBy(String title, String category,
			String sampleName, String[] researchArea, String keywordsStr,
			String pubMedId, String digitalObjectId, String authorsStr,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
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
		if (keywordsStr != null && keywordsStr.length() > 0) {
			// turn into upper case before parsing to words
			List<String> keywordsList = StringUtils.parseToWords(keywordsStr
					.toUpperCase());
			if (keywordsList != null) {
				Disjunction disjunction = Restrictions.disjunction();
				crit.createCriteria("keywordCollection", "keyword");
				for (String keyword : keywordsList) {
					Criterion keywordCrit1 = Restrictions.like("keyword.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit1);
				}
				crit.add(disjunction);
			}
		}

		// authors
		if (authorsStr != null && authorsStr.length() > 0) {
			List<String> authorsList = StringUtils.parseToWords(authorsStr
					.toUpperCase());
			if (authorsList != null) {
				Disjunction disjunction = Restrictions.disjunction();
				crit.createAlias("authorCollection", "author");
				for (String author : authorsList) {
					Criterion crit1 = Restrictions.like("author.lastName",
							author, MatchMode.ANYWHERE);
					disjunction.add(crit1);
					Criterion crit2 = Restrictions.like("author.firstName",
							author, MatchMode.ANYWHERE);
					disjunction.add(crit2);
					Criterion crit3 = Restrictions.like("author.initial",
							author, MatchMode.ANYWHERE);
					disjunction.add(crit3);
				}
				crit.add(disjunction);
			}
		}

		// join sample
		if (sampleName != null && sampleName.length() > 0
				|| nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0
				|| functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0) {
			crit.createAlias("sampleCollection", "sample");
		}
		// sample
		if (sampleName != null && sampleName.length() > 0) {
			TextMatchMode particleMatchMode = new TextMatchMode(
					sampleName);
			Disjunction disjunction = Restrictions.disjunction();

			Criterion keywordCrit1 = Restrictions.like("sample.name",
					particleMatchMode.getUpdatedText(), particleMatchMode
							.getMatchMode());
			disjunction.add(keywordCrit1);
			crit.add(disjunction);
		}

		// join composition and nanomaterial entity
		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			crit.createAlias("sample.sampleComposition", "comp");
			crit.createAlias("comp.nanomaterialEntityCollection", "nanoEntity");
		}

		// nanomaterial entity
		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0) {

			Disjunction disjunction = Restrictions.disjunction();
			if (nanomaterialEntityClassNames != null
					&& nanomaterialEntityClassNames.length > 0) {
				Criterion nanoEntityCrit = Restrictions.in("nanoEntity.class",
						nanomaterialEntityClassNames);
				disjunction.add(nanoEntityCrit);
			}
			if (otherNanomaterialEntityTypes != null
					&& otherNanomaterialEntityTypes.length > 0) {
				Criterion otherNanoCrit1 = Restrictions.eq("nanoEntity.class",
						"OtherNanomaterialEntity");
				Criterion otherNanoCrit2 = Restrictions.in("nanoEntity.type",
						otherNanomaterialEntityTypes);
				Criterion otherNanoCrit = Restrictions.and(otherNanoCrit1,
						otherNanoCrit2);
				disjunction.add(otherNanoCrit);
			}
			crit.add(disjunction);
		}

		// function
		if (functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("nanoEntity.composingElementCollection",
					"compElement").createAlias(
					"compElement.inherentFunctionCollection", "inFunc",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("comp.functionalizingEntityCollection",
					"funcEntity");
			crit.createAlias("funcEntity.functionCollection", "func",
					CriteriaSpecification.LEFT_JOIN);
			if (functionClassNames != null && functionClassNames.length > 0) {
				Criterion funcCrit1 = Restrictions.in("inFunc.class",
						functionClassNames);
				Criterion funcCrit2 = Restrictions.in("func.class",
						functionClassNames);
				disjunction.add(funcCrit1).add(funcCrit2);
				disjunction.add(funcCrit2);
			}
			if (otherFunctionTypes != null && otherFunctionTypes.length > 0) {
				Criterion otherFuncCrit1 = Restrictions.and(Restrictions.eq(
						"inFunc.class", "OtherFunction"), Restrictions.in(
						"inFunc.type", otherFunctionTypes));
				Criterion otherFuncCrit2 = Restrictions.and(Restrictions.eq(
						"func.class", "OtherFunction"), Restrictions.in(
						"func.type", otherFunctionTypes));
				disjunction.add(otherFuncCrit1).add(otherFuncCrit2);
				disjunction.add(otherFuncCrit2);
			}
			crit.add(disjunction);
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);

		// don't need this if Hibernate would've allowed for functionalizing
		// entities in the where clause
		List<String> functionalizingEntityTypes = null;
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0) {
			functionalizingEntityTypes = new ArrayList<String>(Arrays
					.asList(functionalizingEntityClassNames));
			functionalizingEntityTypes.addAll(Arrays
					.asList(otherFunctionalizingEntityTypes));
		}

		for (Object obj : results) {
			Publication publication = findPublicationById(obj.toString());
			// don't need this if Hibernate would've allowed for functionalizing
			// entities in the where clause
			boolean matching = hasMatchingFunctionalizingEntities(publication,
					functionalizingEntityTypes);
			if (matching) {
				publications.add(publication);
			}
		}
		return publications;
	}

	// workaround to filter out publications that don't have matching
	// functionalizing entities due to bug in hibernate in handling having
	// .class in
	// where clause in multi-level inheritance
	private boolean hasMatchingFunctionalizingEntities(Publication publication,
			List<String> functionalizingEntityTypes) throws Exception {
		if (functionalizingEntityTypes == null) {
			return true;
		}
		boolean status = false;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(FunctionalizingEntity.class);
		crit.createAlias("sampleComposition", "comp");
		crit.createAlias("comp.sample", "sample");
		crit.createAlias("sample.publicationCollection", "pub");
		crit.add(Restrictions.eq("pub.id", publication.getId()));
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			FunctionalizingEntity entity = (FunctionalizingEntity) result
					.get(0);
			String entityName;
			if (entity instanceof OtherFunctionalizingEntity) {
				entityName = ((OtherFunctionalizingEntity) entity).getType();
			} else {
				entityName = ClassUtils.getShortClassName(entity.getClass()
						.getName());
			}
			if (functionalizingEntityTypes.contains(entityName)) {
				return true;
			}
		}
		return status;
	}

	public Publication findPublicationById(String publicationId)
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
		}
		return publication;
	}

	public int getNumberOfPublicPublications() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
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

	public void exportDetail(PublicationBean aPub, OutputStream out)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("detailSheet");
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		short startRow = 0;
		setDetailSheet(aPub, wb, sheet, patriarch, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	public short setDetailSheet(PublicationBean aPub, HSSFWorkbook wb,
			HSSFSheet sheet, HSSFPatriarch patriarch, short rowIndex) {
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
		short cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Identifier"));
		if (pubMedId != null && pubMedId.intValue() > 0) {
			row.createCell(cellIndex++).setCellValue(
					new HSSFRichTextString(pubMedId.toString()));
		} else {
			String oid = publication.getDigitalObjectId();
			if (oid != null && oid.length() > 0) {
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
		cell.setCellValue(new HSSFRichTextString("Publication Status"));
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
		cell.setCellValue(new HSSFRichTextString("Research Category"));
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
		if ((startPage != null && startPage.trim().length() > 0)
				|| (endPage != null && endPage.trim().length() > 0)) {
			row.createCell(cellIndex++).setCellValue(
					new HSSFRichTextString(publication.getJournalName()));
		}

		// Description
		row = sheet.createRow(rowIndex++);
		cellIndex = 0;
		cell = row.createCell(cellIndex++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Description"));
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
	 * @param summaryBean CharacterizationSummaryViewBean
	 * @param out OutputStream
	 * @throws IOException if error occurred.
	 */
	public void exportSummary(PublicationSummaryViewBean summaryBean, OutputStream out)
			throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			try {
				this.setSummarySheet(summaryBean, wb);
				wb.write(out);
				out.flush();
			} finally {
				out.close();
			}
		}
	}

	/**
	 * Generate Excel report for sample publication summary report.
	 *
	 * @param summaryBean CharacterizationSummaryViewBean
	 * @param wb HSSFWorkbook
	 */
	private void setSummarySheet(PublicationSummaryViewBean summaryBean, HSSFWorkbook wb) {
		HSSFRow row = null;
		HSSFRow rowAuthor = null;
		StringBuilder sb = new StringBuilder();
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		SortedMap<String, List<PublicationBean>> pubs = summaryBean.getCategory2Publications();
		for (String category : pubs.keySet()) {
			short rowIndex = 0;
			short cellIndex = 0;
			
			// Create one work sheet for each category. 
			HSSFSheet sheet = wb.createSheet(category);
			row = sheet.createRow(rowIndex++);

			// Generate header of report
			ExportUtils.createCell(row, cellIndex++, headerStyle, TITLE);
			ExportUtils.createCell(row, cellIndex++, headerStyle, AUTHORS);
			ExportUtils.createCell(row, cellIndex++, headerStyle, BIBLIOBRAPHY_INFO);
			ExportUtils.createCell(row, cellIndex++, headerStyle, ABSTRACT);
			ExportUtils.createCell(row, cellIndex++, headerStyle, RESEARCH_CATEGORY);
			ExportUtils.createCell(row, cellIndex++, headerStyle, CREATED_DATE);
			
			// Generate data of report
			List<PublicationBean> pubBeans = pubs.get(category);
			for (PublicationBean pubBean : pubBeans) {
				Publication pub = (Publication) pubBean.getDomainFile();
				row = sheet.createRow(rowIndex++);
				cellIndex = 0;
				
				// Title: cell index = 0.
				ExportUtils.createCell(row, cellIndex++, pub.getTitle());
				
				// Author(s): cell index = 1, one author per row.
				List<Author> authors = pubBean.getAuthors();
				if (authors != null) {
					int countAuthors = 0;
					for (Author author : authors) {
						sb.setLength(0);
						sb.append(author.getLastName()).append(',').append(' ');
						sb.append(author.getFirstName()).append(' ');
						sb.append(author.getInitial());
						if (countAuthors == 0) {
							ExportUtils.createCell(row, cellIndex, null, sb.toString());
						} else {
							// Create new row for other authors, increase rowIndex.
							rowAuthor = sheet.createRow(rowIndex++);
							ExportUtils.createCell(rowAuthor, cellIndex, null, sb.toString());
						}
						countAuthors++;
					}
				}
				cellIndex++; // Increase cellIndex for next column. 
				
				// Bibliography Info: cell index = 2, need to increase cellIndex first.
				ExportUtils.createCell(row, cellIndex++, pubBean.getBibliographyInfo());
				
				// Abstract/Full Text: cell index = 3.
				if (StringUtils.isEmpty(pub.getAbstractText())) {
					sb.setLength(0);
					if (pub.getPubMedId() != null) {
						sb.append(PMID).append(pub.getPubMedId());
						ExportUtils.createCell(row, cellIndex++, sb.toString());
					} else {
						if (StringUtils.isEmpty(pub.getDigitalObjectId())) {
							ExportUtils.createCell(row, cellIndex++, pub.getUri());
						} else {
							sb.append(PMID).append(pub.getDigitalObjectId());
							ExportUtils.createCell(row, cellIndex++, sb.toString());
						}
					}
				} else {
					ExportUtils.createCell(row, cellIndex++, pub.getAbstractText());
				}
				
				// Research Category: cell index = 4.
				ExportUtils.createCell(row, cellIndex++, pub.getResearchArea());
				
				
				// Created Date: cell index = 5.
				ExportUtils.createCell(row, cellIndex++, pubBean.getCreatedDateStr());
			}
		}
	}
}
