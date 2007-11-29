package gov.nih.nci.calab.ui.report;

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
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

	private InitReportSetup() throws Exception {
		reportService = new SearchReportService();
	}

	public static InitReportSetup getInstance() throws Exception {
		return new InitReportSetup();
	}

	public void setAllReports(HttpSession session, String particleId)
			throws Exception {
		UserBean user = (UserBean) session.getAttribute("user");
		if (session.getAttribute("particleReports") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {

			List<LabFileBean> reportBeans = reportService.getReportInfo(
					particleId, CaNanoLabConstants.REPORT, user);
			session.setAttribute("particleReports", reportBeans);
		}

		if (session.getAttribute("particleAssociatedFiles") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			List<LabFileBean> associatedBeans = reportService.getReportInfo(
					particleId, CaNanoLabConstants.ASSOCIATED_FILE, user);
			session.setAttribute("particleAssociatedFiles", associatedBeans);
		}
	}

	public void setAllReportTypes(HttpSession session) {
		if (session.getAttribute("allReportTypes") == null) {
			String[] allReportTypes = reportService.getAllReportTypes();
			session.setAttribute("allReportTypes", allReportTypes);
		}
	}
}
