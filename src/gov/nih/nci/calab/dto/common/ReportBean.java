package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.particle.ParticleBean;

import java.util.ArrayList;
import java.util.List;

public class ReportBean extends LabFileBean {
	List<ParticleBean> particleList = new ArrayList<ParticleBean>();
	public ReportBean(Report report) {
		super(report);
		for (Nanoparticle particle : report.getParticleCollection()) {
			particleList.add(new ParticleBean(particle));
		}
	}

	public ReportBean(AssociatedFile associatedFile) {
		super(associatedFile);
		for (Nanoparticle particle : associatedFile.getParticleCollection()) {
			particleList.add(new ParticleBean(particle));
		}
	}

	public List<ParticleBean> getParticleList() {
		return particleList;
	}

	public void setParticleList(List<ParticleBean> particleList) {
		this.particleList = particleList;
	}
}
