package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a nanoparticle
 * to be shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class NanoparticleDecorator extends TableDecorator {
	public SortableName getEditParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		// replace space with special char
		String particleType = particle.getSampleType().replace(" ", "%20");
		String particleName = particle.getSampleName();
		String editParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleType="
				+ particleType + "&particleName=" + particleName;
		String link = "<a href=" + editParticleURL + ">"
				+ particle.getSampleName() + "</a>";
		SortableName sortableLink = new SortableName(particle.getSampleName(),
				link);
		return sortableLink;
	}

	public SortableName getViewParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		// replace space with special char
		String particleType = particle.getSampleType().replace(" ", "%20");
		String particleName = particle.getSampleName();
		String viewParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupView&particleType="
				+ particleType + "&particleName=" + particleName;
		String link = "<a href=" + viewParticleURL + ">"
				+ particle.getSampleName() + "</a>";
		SortableName sortableLink = new SortableName(particle.getSampleName(),
				link);
		return sortableLink;
	}

	public SortableName getRemoteViewURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		// replace space with special char
		String particleType = particle.getSampleType().replace(" ", "%20");
		String particleName = particle.getSampleName();
		String remoteViewURL = "remoteNanoparticleGeneralInfo.do?dispatch=view&particleType="
				+ particleType + "&particleName=" + particleName+"&gridNodeHost="+particle.getGridNode();
		String link = "<a href=" + remoteViewURL + ">"
				+ particle.getSampleName() + "</a>";
		SortableName sortableLink = new SortableName(particle.getSampleName(),
				link);
		return sortableLink;
	}

	public String getKeywordsStr() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		return StringUtils.join(particle.getKeywords(), "<br>");
	}

	public String getFunctionTypesStr() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		return StringUtils.join(particle.getFunctionTypes(), "<br>");
	}

	public String getCharacterizationsStr() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		return StringUtils.join(particle.getCharacterizations(), "<br>");
	}
}
