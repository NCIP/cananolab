package gov.nih.nci.calab.ui.report;

import gov.nih.nci.calab.dto.common.ReportBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ReportException;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for report forms.
 * 
 * @author pansu
 * 
 */
public class InitReportSetup {
	private static SearchReportService reportService;

	private InitReportSetup() throws CaNanoLabSecurityException {
		reportService = new SearchReportService();
	}

	public static InitReportSetup getInstance()
			throws CaNanoLabSecurityException {
		return new InitReportSetup();
	}

	public void setAllReports(HttpSession session, String particleId)
			throws ReportException, CaNanoLabSecurityException {
		UserBean user = (UserBean) session.getAttribute("user");
		List<ReportBean> reportBeans = reportService.getReportInfo(particleId,
				CaNanoLabConstants.REPORT, user);
		session.setAttribute("particleReports", reportBeans);

		List<ReportBean> associatedBeans = reportService.getReportInfo(
				particleId, CaNanoLabConstants.ASSOCIATED_FILE, user);
		session.setAttribute("particleAssociatedFiles", associatedBeans);
	}

	public void setAllReportTypes(HttpSession session) {
		if (session.getServletContext().getAttribute("allReportTypes") == null) {
			String[] allReportTypes = reportService.getAllReportTypes();
			session.getServletContext().setAttribute("allReportTypes",
					allReportTypes);
		}
	}
}
