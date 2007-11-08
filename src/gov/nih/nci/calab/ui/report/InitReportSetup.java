package gov.nih.nci.calab.ui.report;

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.common.LookupService;
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
	private static LookupService lookupService;

	private InitReportSetup() throws Exception {
		lookupService = new LookupService();
	}

	public static InitReportSetup getInstance() throws Exception {
		return new InitReportSetup();
	}
}
