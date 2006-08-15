package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
import java.util.List;

public class SearchNanoparticleService {

	public List<ParticleBean> basicSearch(String particleSource,
			String particleType, String functionType,
			String characterizationType, String[] keywords, UserBean user)
			throws Exception {
		// TODO fill in dataabse code
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		ParticleBean particle1 = new ParticleBean("1", "NCL-3", "UMD",
				"Dendrimer", "Organic", "Targeting<br>Therapeutic",
				"Physical<br>In Vitro", "this<br>is<br> a <br>test");
		ParticleBean particle2 = new ParticleBean("2", "NCL-14", "UMD",
				"Dendrimer", "Organic",
				"Targeting<br>Therapeutic<br>Diagnostic Imaging",
				"Physical<br>In Vitro", "this<br> is<br> another<br> test");
		particles.add(particle1);
		particles.add(particle2);

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		List<ParticleBean> filteredParticles = userService
				.getFilteredParticles(user, particles);

		return filteredParticles;
	}

}
