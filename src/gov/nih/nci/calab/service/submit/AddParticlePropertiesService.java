package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.dto.particle.DendrimerBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;

public class AddParticlePropertiesService {
	public void addParticleProperties(String particleType, ParticleBean particle) {
		if (particleType.equalsIgnoreCase("dendrimer")) {
			DendrimerBean dendrimer=(DendrimerBean)particle;
			//TODO update the dendrimer object in the db.
		}
	}
}
