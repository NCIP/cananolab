package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.SortableName;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

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
		String particleId = particle.getParticleSample().getId().toString();
		String particleName = particle.getParticleSample().getName();
		String editParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleId="
				+ particleId;
		String link = "<a href=" + editParticleURL + ">" + particleName
				+ "</a>";

		SortableName sortableLink = new SortableName(particleName, link);
		return sortableLink;
	}

	public SortableName getViewParticleURL() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String particleId = particle.getParticleSample().getId().toString();
		String particleName = particle.getParticleSample().getName();
		String viewParticleURL = "nanoparticleGeneralInfo.do?dispatch=setupView&particleId="
				+ particleId;
		;
		String link = "<a href=" + viewParticleURL + ">" + particleName
				+ "</a>";
		SortableName sortableLink = new SortableName(particleName, link);
		return sortableLink;
	}

	public SortableName getRemoteViewURL() throws UnsupportedEncodingException {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();

		String particleName = URLEncoder.encode(particle.getParticleSample()
				.getName(), "UTF-8");
		String remoteViewURL = "remoteNanoparticleGeneralInfo.do?dispatch=view"
				+ "&particleName=" + particleName + "&gridNodeHost="
				+ particle.getGridNode();
		String link = "<a href=" + remoteViewURL + ">" + particleName + "</a>";
		SortableName sortableLink = new SortableName(particleName, link);
		return sortableLink;
	}

	public String getKeywordStr() {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		String keywordsStr = particle.getKeywordsStr();
		String[] strs = keywordsStr.split("\r\n");
		return StringUtils.join(strs, "<br>");
	}

	public String getCompositionStr() throws CaNanoLabException {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		SortedSet<String> compEntityNames = new TreeSet<String>();
		if (particle.getParticleSample().getSampleComposition() != null) {
			for (NanoparticleEntity entity : particle.getParticleSample()
					.getSampleComposition().getNanoparticleEntityCollection()) {
				String displayName = InitSetup.getInstance().getDisplayName(
						ClassUtils.getShortClassName(entity.getClass()
								.getName()),
						this.getPageContext().getServletContext());
				compEntityNames.add(displayName);
			}
			for (FunctionalizingEntity entity : particle.getParticleSample()
					.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				String displayName = InitSetup.getInstance().getDisplayName(
						ClassUtils.getShortClassName(entity.getClass()
								.getName()),
						this.getPageContext().getServletContext());
				compEntityNames.add(displayName);
			}
		}
		return StringUtils.join(compEntityNames, "<br>");
	}

	public String getFunctionStr() throws CaNanoLabException {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		SortedSet<String> functionNames = new TreeSet<String>();
		NanoparticleSampleService service = new NanoparticleSampleService();
		SortedSet<String> functionClassNames = service
				.getStoredFunctionClassNames(particle);
		for (String name : functionClassNames) {
			String displayName = InitSetup.getInstance().getDisplayName(name,
					this.getPageContext().getServletContext());
			functionNames.add(displayName);
		}
		return StringUtils.join(functionNames, "<br>");
	}

	public String getCharacterizationStr() throws CaNanoLabException {
		ParticleBean particle = (ParticleBean) getCurrentRowObject();
		Collection<Characterization> characterizations = particle
				.getParticleSample().getCharacterizationCollection();

		SortedSet<String> charNames = new TreeSet<String>();
		for (Characterization chara : characterizations) {
			String displayName = InitSetup.getInstance().getDisplayName(
					ClassUtils.getShortClassName(chara.getClass().getName()),
					this.getPageContext().getServletContext());
			charNames.add(displayName);
		}
		return StringUtils.join(charNames, "<br>");
	}
}
