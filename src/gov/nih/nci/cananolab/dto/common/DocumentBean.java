/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

/**
 * Document view bean
 * 
 * @author tanq
 * 
 */
public class DocumentBean extends LabFileBean {
	private String[] particleNames;

	/**
	 * 
	 */
	public DocumentBean() {
		super();
		domainFile = new LabFile();
		domainFile.setUriExternal(false);
	}

	public DocumentBean(LabFile document) {
		super(document);
		this.domainFile = document;
		Publication publication = (Publication) document;
		particleNames = new String[publication
				.getNanoparticleSampleCollection().size()];
		int i = 0;
		for (NanoparticleSample particle : publication
				.getNanoparticleSampleCollection()) {
			particleNames[i] = particle.getName();
			i++;
		}
	}

	public DocumentBean(Publication publication, boolean loadSamples) {
		super(publication);
		this.domainFile = publication;
		if (loadSamples) {
			particleNames = new String[publication.getNanoparticleSampleCollection()
					.size()];
			int i = 0;
			for (NanoparticleSample particle : publication
					.getNanoparticleSampleCollection()) {
				particleNames[i] = particle.getName();
				i++;
			}
		}
	}
	

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof DocumentBean) {
			DocumentBean c = (DocumentBean) obj;
			Long thisId = domainFile.getId();
			if (thisId != null && thisId.equals(c.getDomainFile().getId())) {
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
}
