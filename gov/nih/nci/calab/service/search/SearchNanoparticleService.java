package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
import java.util.List;

public class SearchNanoparticleService {

	public List<ParticleBean> basicSearch(String particleSource,
			String particleType, String[] functionTypes,
			String[] characterizations, String[] keywords, UserBean user)
			throws Exception {
		// TODO fill in database code
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		ParticleBean particle1 = new ParticleBean("1", "NCL-3", "UMD",
				"Dendrimer", "Organic", new String[] { "Targeting",
						"Therapeutic" }, new String[] { "Physical:Composition",
						"Physical:Size" }, new String[] { "this", "is", "a",
						"test" });
		ParticleBean particle2 = new ParticleBean("2", "NCL-14", "UMD",
				"Dendrimer", "Organic", new String[] { "Targeting",
						"Therapeutic" }, new String[] { "Physical:Composition",
						"Physical:Size" }, new String[] { "this", "is",
						"another", "test" });
		particles.add(particle1);
		particles.add(particle2);

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		List<ParticleBean> filteredParticles = userService
				.getFilteredParticles(user, particles);

		return filteredParticles;
	}

	public ParticleBean getGeneralInfo(String particleName) {
		// TODO add database code
		String[] keywords = new String[] { "This", "is" };
		String[] visibilityGroup = new String[] {"CCNE_Researcher" };
		ParticleBean particle1 = new ParticleBean("1", "NCL-3", "UMD",
				"Dendrimer", "Organic", new String[] { "Targeting",
						"Therapeutic" }, new String[] { "Physical:Composition",
						"Physical:Size" }, keywords);
		particle1.setVisibilityGroups(visibilityGroup);
		
		return particle1;
	}
}
