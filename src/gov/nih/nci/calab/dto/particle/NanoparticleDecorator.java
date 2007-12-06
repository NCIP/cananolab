package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
		String particleId = particle.getSampleId();
		String editParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleId="
				+ particleId;
		String link = "<a href=" + editParticleURL + ">"
				+ particle.getSampleName() + "</a>";

		SortableName sortableLink = new SortableName(particle.getSampleName(),
				link);
		return sortableLink;
	}

	public SortableName getViewParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String particleId = particle.getSampleId();
		String viewParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupView&particleId="
				+ particleId;
		;
		String link = "<a href=" + viewParticleURL + ">"
				+ particle.getSampleName() + "</a>";
		SortableName sortableLink = new SortableName(particle.getSampleName(),
				link);
		return sortableLink;
	}

	public SortableName getRemoteViewURL() throws UnsupportedEncodingException {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String particleType = URLEncoder.encode(particle.getSampleType(),
				"UTF-8");
		String particleName = URLEncoder.encode(particle.getSampleName(),
				"UTF-8");
		String remoteViewURL = "remoteNanoparticleGeneralInfo.do?dispatch=view&particleType="
				+ particleType
				+ "&particleName="
				+ particleName
				+ "&gridNodeHost=" + particle.getGridNode();
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
