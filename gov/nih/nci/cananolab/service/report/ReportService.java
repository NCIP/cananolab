package gov.nih.nci.cananolab.service.report;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ReportException;

import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching reports.
 * 
 * @author pansu
 * 
 */
public interface ReportService {

	/**
	 * Persist a new report or update an existing report
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void saveReport(Report report, String[] particleNames,
			byte[] fileData) throws ReportException;

	public List<ReportBean> findReportsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws ReportException, CaNanoLabSecurityException;

	public ReportBean findReportById(String reportId) throws ReportException;
}
