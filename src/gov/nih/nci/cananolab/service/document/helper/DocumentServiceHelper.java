package gov.nih.nci.cananolab.service.document.helper;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.DocumentSummaryBean;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of DocumentService and grid service *
 * 
 * @author tanq
 * 
 */
public class DocumentServiceHelper {
	private static Logger logger = Logger.getLogger(DocumentServiceHelper.class);

	private ReportServiceHelper reportHelper = new ReportServiceHelper();
	private PublicationServiceHelper publicationHelper = new PublicationServiceHelper();
	
	public int getNumberOfPublicDocuments() throws Exception {
		int count = publicationHelper.getNumberOfPublicPublications();
		count+=reportHelper.getNumberOfPublicReports();		
		return count;
	}
	
	public void exportFullSummary(DocumentSummaryBean summaryBean,
			OutputStream out) throws IOException {
		PublicationServiceHelper publicationHelper = new PublicationServiceHelper();
		ReportServiceHelper reportHelper = new ReportServiceHelper();		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short rowCount = 0;
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		for (LabFileBean labFileBean : summaryBean.getDocumentRows()) {
			if (labFileBean instanceof PublicationBean) {
				publicationHelper.setDetailSheet((PublicationBean)labFileBean, wb, sheet, patriarch, rowCount);
			}else {
				reportHelper.setDetailSheet((ReportBean)labFileBean, wb, sheet, patriarch, rowCount);
			}
			rowCount += 2;
		}
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
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
			Collection<DocumentAuthor> authors = null;
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
				
				authors = publication.getDocumentAuthorCollection();
				if (authors!=null) {
					int countAuthors = 0;
					for (DocumentAuthor author: authors) {
						sb.setLength(0);
						sb.append(author.getFirstName());
						sb.append(' ');
						sb.append(author.getLastName());
						sb.append(' ');
						sb.append(author.getMiddleInitial());						
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
		
		if (particle.getReportCollection()!=null) {	
			for (Report report: particle.getReportCollection()) {				
				row = sheet.createRow(rowCount);
				rowCount++;
				cellCount = 0;
				//identifier
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString("Report"));				
				//title
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(report.getTitle()));
			}
		}
		return rowCount;
	}

}
