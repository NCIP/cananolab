package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.particle.ParticleBean;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for display a nanoparticle with a URL on the name
 * 
 * @author pansu
 * 
 */
public class NanoparticleDecorator extends TableDecorator {
	public String getEditParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String particleType = particle.getSampleType();
		String particleName = particle.getSampleName();
		String viewParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleType="
				+ particleType + "&particleName=" + particleName;
		String link = "<a href=" + viewParticleURL + ">"
				+ particle.getSortableName() + "</a>";
		return link;
	}

	public String getViewParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String particleType = particle.getSampleType();
		String particleName = particle.getSampleName();
		String viewParticleURL = "nanoparticleGeneralInfo.do?dispatch=view&particleType="
				+ particleType + "&particleName=" + particleName;
		String link = "<a href=" + viewParticleURL + ">"
				+ particle.getSortableName() + "</a>";
		return link;
	}
}
