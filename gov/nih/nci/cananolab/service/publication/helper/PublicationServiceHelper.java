package gov.nih.nci.cananolab.service.publication.helper;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of PublicationService and grid service *
 * 
 * @author tanq
 * 
 */
public class PublicationServiceHelper {
	
	public List<Publication> findPublicationsBy(String title,
			String category,  String nanoparticleName, 
			String[] researchArea, String keywordsStr,
			String pubMedId, String digitalObjectId, String authorsStr,
			String[] nanoparticleEntityClassNames,
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
			TextMatchMode categoryMatchMode = new TextMatchMode(category);
			crit.add(Restrictions.ilike("category", categoryMatchMode
					.getUpdatedText(), categoryMatchMode.getMatchMode()));
		}
		
		//pubMedId
		if (pubMedId != null && pubMedId.length() > 0) {
			TextMatchMode pubMedIdMatchMode = new TextMatchMode(pubMedId);
			Long pubMedIdLong = null;	
			try {
				pubMedIdLong = new Long(pubMedIdMatchMode.getUpdatedText());
			}catch (Exception ex) {
				//ignore
				pubMedIdLong = new Long(0);
			}
			crit.add(Restrictions.eq("pubMedId", pubMedIdLong));
		}
		if (digitalObjectId != null && digitalObjectId.length() > 0) {
			TextMatchMode digitalObjectIdMatchMode = new TextMatchMode(digitalObjectId);
			crit.add(Restrictions.ilike("digitalObjectId", digitalObjectIdMatchMode
					.getUpdatedText(), digitalObjectIdMatchMode.getMatchMode()));
		}
		
		//researchArea
		if (researchArea != null && researchArea.length > 0) {
			
			Disjunction disjunction = Restrictions.disjunction();
			for (String research : researchArea) {
				Criterion crit1 = Restrictions.like("researchArea",
						research, MatchMode.ANYWHERE);				
				disjunction.add(crit1);
			}
			crit.add(disjunction);
		}
		
		//keywords
		String keywordsArray[] = null;
		if (keywordsStr != null && keywordsStr.length() > 0) {
			List<String> keywordsList = StringUtils.parseToWords(keywordsStr);
			if (keywordsList != null) {
				keywordsArray = new String[keywordsList.size()];
				keywordsList.toArray(keywordsArray);
			}
		}		
		if (keywordsArray != null && keywordsArray.length > 0) {
			// turn words into upper case before searching keywords
			String[] upperKeywords = new String[keywordsArray.length];
			for (int i = 0; i < keywordsArray.length; i++) {
				upperKeywords[i] = keywordsArray[i].toUpperCase();
			}
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("keywordCollection", "keyword1",
					CriteriaSpecification.LEFT_JOIN);
			for (String keyword : upperKeywords) {
				Criterion keywordCrit1 = Restrictions.like("keyword1.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit1);
			}
			crit.add(disjunction);
		}
		
		
		//authors
		crit.setFetchMode("authorCollection", FetchMode.JOIN);		
		String authorsArray[] = null;
		if (authorsStr != null && authorsStr.length() > 0) {
			List<String> authorsList = StringUtils.parseToWords(authorsStr);
			if (authorsList != null) {
				authorsArray = new String[authorsList.size()];
				authorsList.toArray(authorsArray);
			}
		}		
		if (authorsArray != null && authorsArray.length > 0) {
			// turn words into upper case before searching keywords
			String[] uppers = new String[authorsArray.length];
			for (int i = 0; i < authorsArray.length; i++) {
				uppers[i] = authorsArray[i].toUpperCase();
			}
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("authorCollection", "author1",
					CriteriaSpecification.LEFT_JOIN);
			for (String author : uppers) {
				Criterion crit1 = Restrictions.like("author1.lastName",
						author, MatchMode.ANYWHERE);
				disjunction.add(crit1);
				Criterion crit2 = Restrictions.like("author1.firstName",
						author, MatchMode.ANYWHERE);
				disjunction.add(crit2);
				Criterion crit3 = Restrictions.like("author1.initial",
						author, MatchMode.ANYWHERE);
				disjunction.add(crit3);
			}
			crit.add(disjunction);
		}
		
		crit.createAlias("nanoparticleSampleCollection", "sample",
				CriteriaSpecification.LEFT_JOIN).createAlias(
				"sample.sampleComposition", "compo",
				CriteriaSpecification.LEFT_JOIN).createAlias(
				"compo.nanoparticleEntityCollection", "nanoEntity",
				CriteriaSpecification.LEFT_JOIN);
		//nanoparticleName
		if (nanoparticleName != null && nanoparticleName.length() > 0) {
			TextMatchMode particleMatchMode = new TextMatchMode(nanoparticleName);
			Disjunction disjunction = Restrictions.disjunction();
			Criterion keywordCrit1 = Restrictions.like("sample.name",
					particleMatchMode.getUpdatedText(), 
					particleMatchMode.getMatchMode());
			disjunction.add(keywordCrit1);			
			crit.add(disjunction);
		}		

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

	public Publication findPublicationById(String publicationId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class).add(
				Property.forName("id").eq(new Long(publicationId)));
		crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
		crit.setFetchMode("authorCollection", FetchMode.JOIN);
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
			HSSFSheet sheet, HSSFPatriarch patriarch, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		Publication publication = (Publication) aPub.getDomainFile();
		
		HSSFRow row = null;
		HSSFCell cell = null;
		// PubMedID
		Long pubMedId = publication.getPubMedId();
		row = sheet.createRow(rowCount++);
		short cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Identifier"));
		if (pubMedId!=null && pubMedId.intValue()>0) {			
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(pubMedId.toString()));
		}else {
			String oid = publication.getDigitalObjectId();
			if (oid!=null && oid.length()>0) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(oid));
			}else {
				//row.createCell(cellCount++).setCellValue(
					//	new HSSFRichTextString(""));
			}		
		}		
		//publication type
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Type"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getCategory()));	
		
		//publication status
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication Status"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getStatus()));	
		
		//Authors
		String rowHeader = "Authors";		
		StringBuffer sb = new StringBuffer();
		if (publication.getAuthorCollection()!=null) {			
			List<Author> authorslist = new ArrayList<Author>(publication.getAuthorCollection());
			Collections.sort(authorslist, 
					new Comparator<Author>() {
				 public int compare(Author o1, Author o2) {
				        return (int)(o2.getCreatedDate().compareTo(o1.getCreatedDate()));
				 }});
			for (Author author: authorslist) {				
				sb.append(author.getFirstName());
				sb.append(' ');
				sb.append(author.getInitial());
				sb.append(' ');
				sb.append(author.getLastName());
				
				row = sheet.createRow(rowCount++);
				cellCount = 0;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(rowHeader));
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(sb.toString()));	
				rowHeader = "";
				sb.setLength(0);
			}
		}
		
		//research area
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Research Category"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getResearchArea()));	
		
		//Title
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Title"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getTitle()));	
		
		//Journal
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Journal"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getJournalName()));	
		
		//Year
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Year"));
		int year = 0;
		if(publication.getYear() != null)
			year = publication.getYear();
		if (year>0) {
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(Integer.toString(year)));
		}
		
		//Volume
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Volume"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getVolume()));	
		
		//Pages
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Pages"));
		String startPage = publication.getStartPage();
		String endPage = publication.getEndPage();
		if ((startPage!=null && startPage.trim().length()>0) || 
			(endPage!=null && endPage.trim().length()>0)) {
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(publication.getJournalName()));
		}
		
		//Description
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Description"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getDescription()));
		
//		Uploaded Publication URI
		row = sheet.createRow(rowCount++);
		cellCount = 0;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Publication URI"));
		row.createCell(cellCount++).setCellValue(
				new HSSFRichTextString(publication.getUri()));

		return rowCount;
	}
	
	public void exportSummary(ParticleBean particleBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		setSummarySheet(particleBean, wb, sheet, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}
	
	private short setSummarySheet(ParticleBean particleBean,
			HSSFWorkbook wb, HSSFSheet sheet, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFCellStyle newLineStyle = wb.createCellStyle();
		// Word Wrap MUST be turned on
		newLineStyle.setWrapText(true);

		short cellCount = 0;
		HSSFRow row = null;
		HSSFRow rowAuthor = null;
		HSSFCell cell = null;

		// summary header
		row = sheet.createRow(rowCount);
		rowCount++;

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Identifier"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Title"));
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Authors"));
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Year"));

		// data
		StringBuffer sb = new StringBuffer();
		NanoparticleSample particle = particleBean.getDomainParticleSample();
		if (particle.getPublicationCollection()!=null) {		
			Long pubmedid = null;
			String doi = null;
			String id = null;
			int year = 0;
			Collection<Author> authors = null;
			for (Publication publication: particle.getPublicationCollection()) {				
				row = sheet.createRow(rowCount);
				rowCount++;
				cellCount = 0;
				pubmedid = publication.getPubMedId();				
				if(pubmedid!=null && pubmedid>0) {
					id = "PMID: "+pubmedid;
				}else {
					doi = publication.getDigitalObjectId();
					if (doi!=null && doi.length()>0) {
						id = "DOI: "+doi;
					}else {
						id = "Publication: "+publication.getTitle();
						
					}
				}
				//identifier
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(id));				
				//title
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(publication.getTitle()));
				//authors
				sb.setLength(0);
				//TODO, ORDER
				authors = publication.getAuthorCollection();
				if (authors!=null) {
					int countAuthors = 0;
					for (Author author: authors) {
						sb.setLength(0);
						sb.append(author.getFirstName());
						sb.append(' ');
						sb.append(author.getLastName());
						sb.append(' ');
						sb.append(author.getInitial());						
						if (countAuthors==0) {
							row.createCell((short)2).setCellValue(
									new HSSFRichTextString(sb.toString()));
						}else {							
							rowAuthor = sheet.createRow(rowCount);
							rowCount++;
							rowAuthor.createCell((short)2).setCellValue(
									new HSSFRichTextString(sb.toString()));
						}
						countAuthors++;
					}
				}						
				//year
				year = publication.getYear();
				if (year>0) {
					row.createCell((short)3).setCellValue(
						new HSSFRichTextString(Integer.toString(year)));
				}
			}
		}		
		return rowCount;
	}


}
