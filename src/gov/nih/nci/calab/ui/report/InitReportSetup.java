package gov.nih.nci.calab.ui.report;

import gov.nih.nci.calab.service.common.LookupService;

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
