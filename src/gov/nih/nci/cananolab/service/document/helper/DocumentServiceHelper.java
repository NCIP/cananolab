package gov.nih.nci.cananolab.service.document.helper;

import gov.nih.nci.cananolab.dto.common.DocumentSummaryBean;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.report.helper.ReportServiceHelper;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
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
		for (LabFileBean labFileBean : summaryBean.getDocumentBeans()) {
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

	public void exportSummary(DocumentSummaryBean summaryBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		
		//TODO: XXXXYYYY
		//setSummarySheet(summaryBean, wb, sheet, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	

}
