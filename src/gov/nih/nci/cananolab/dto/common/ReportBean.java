/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

/**
 * Report view bean
 * 
 * @author pansu
 * 
 */
public class ReportBean extends LabFileBean {
	private String[] particleNames;
	private Report domainReport=new Report();
	/**
	 * 
	 */
	public ReportBean() {
		super();
		domainReport = new Report();
	}

	public ReportBean(Report report) {
		super(report);
		this.domainFile = report;
		particleNames = new String[report.getNanoparticleSampleCollection()
				.size()];
		int i = 0;
		for (NanoparticleSample particle : report
				.getNanoparticleSampleCollection()) {
			particleNames[i] = particle.getName();
			i++;
		}
		domainReport=(Report)domainFile;
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

	public String[] getParticleNames() {
		return particleNames;
	}

	public void setParticleNames(String[] particleNames) {
		this.particleNames = particleNames;
	}

	public Report getDomainReport() {
		return domainReport;
	}
}
