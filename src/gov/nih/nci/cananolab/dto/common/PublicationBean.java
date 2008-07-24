/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

import java.util.Collection;

/**
 * Publication view bean
 * 
 * @author tanq
 * 
 */
public class PublicationBean extends LabFileBean {
	private String[] particleNames;
	private String[] authors;

	/**
	 * 
	 */
	public PublicationBean() {
		super();
		domainFile = new Publication();
		domainFile.setUriExternal(false);
	}

	public PublicationBean(Publication publication) {
		super(publication);
		this.domainFile = publication;
		particleNames = new String[publication.getNanoparticleSampleCollection()
				.size()];	
		int i = 0;
		for (NanoparticleSample particle : publication
				.getNanoparticleSampleCollection()) {
			particleNames[i] = particle.getName();
			i++;
		}
		Collection<DocumentAuthor> authorCollection = 
			publication.getDocumentAuthorCollection();
		if (authorCollection!=null && authorCollection.size()>0) {
			authors = new String[authorCollection.size()];
			i = 0;
			for (DocumentAuthor author : authorCollection) {
				authors[i] = author.getFirstName();
				i++;
			}
		}else {
			authors = null;
		}
	}

	public PublicationBean(Publication publication, boolean loadSamples) {
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
		if (obj instanceof PublicationBean) {
			PublicationBean c = (PublicationBean) obj;
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

	/**
	 * @return the authors
	 */
	public String[] getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String[] authors) {
		this.authors = authors;
	}
}
