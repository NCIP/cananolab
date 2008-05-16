package gov.nih.nci.cananolab.ui.report;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for report forms.
 * 
 * @author pansu
 * 
 */
public class InitReportSetup {
	private InitReportSetup() {
	}

	public static InitReportSetup getInstance() {
		return new InitReportSetup();
	}

	public void setReportDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "reportCategories",
						"Report", "category", "otherCategory", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().getAllNanoparticleSampleNames(
				request, user);
	}

	public void persistReportDropdowns(HttpServletRequest request,
			ReportBean report) throws Exception {
		InitSetup.getInstance().persistLookup(request, "LabFile", "type",
				"otherType", report.getDomainFile().getType());
		InitSetup.getInstance().persistLookup(request, "Report", "category",
				"otherCategory",
				((Report) (report.getDomainFile())).getCategory());
		setReportDropdowns(request);
	}
}
