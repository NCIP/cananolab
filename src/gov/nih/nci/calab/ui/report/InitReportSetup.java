/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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
