package gov.nih.nci.cananolab.ui.report;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.SortedSet;

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

	public SortedSet<String> getReportCategories(HttpServletRequest request)
			throws Exception {
		return InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "reportCategories",
						"Report", "category", "otherCategory", true);
	}

	public void persistReportDropdowns(HttpServletRequest request,
			ReportBean report) throws Exception {
		InitSetup.getInstance().persistLookup(request, "LabFile", "type",
				"otherType", report.getDomainFile().getType());
		InitSetup.getInstance().persistLookup(request, "Report", "category",
				"otherCategory",
				((Report) (report.getDomainFile())).getCategory());

	}
}
