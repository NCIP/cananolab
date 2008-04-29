/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Report;

/**
 * Report view bean
 * 
 * @author pansu
 * 
 */
public class ReportBean extends LabFileBean {
	private Report domainReport;
	/**
	 * 
	 */
	public ReportBean() {
		super();
	}

	public ReportBean(Report report) {
		super(report);
		this.domainReport = report;
	}

	public Report getDomainReport() {
		return domainReport;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ReportBean) {
			ReportBean c = (ReportBean) obj;
			Long thisId = domainReport.getId();
			if (thisId != null && thisId.equals(c.getDomainReport().getId())) {
				eq = true;
			}
		}
		return eq;
	}
}
