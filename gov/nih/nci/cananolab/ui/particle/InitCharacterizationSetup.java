package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for characterization forms.
 * 
 * @author pansu
 * 
 */
public class InitCharacterizationSetup {
	private NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();

	public static InitCharacterizationSetup getInstance() {
		return new InitCharacterizationSetup();
	}

	public void setCharacterizationSources(HttpServletRequest request)
			throws Exception {
		SortedSet<String> charSources = charService
				.getAllCharacterizationSources();
		request.getSession().setAttribute("characterizationSources",
				charSources);
	}
}
