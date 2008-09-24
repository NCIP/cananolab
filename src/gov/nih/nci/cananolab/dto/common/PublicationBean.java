/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Publication view bean
 * 
 * @author tanq
 * 
 */
public class PublicationBean extends LabFileBean {
	private static final String delimiter = ";";
	
	private String[] particleNames;
	private String[] researchAreas;
	private List<Author> authors = new ArrayList<Author>(20);

	private boolean foundPubMedArticle = false;

	public PublicationBean() {
		super();
		domainFile = new Publication();
		domainFile.setUriExternal(false);
		authors.add(new Author());
	}

	public PublicationBean(Publication publication) {
		this(publication, true);
	}

	public PublicationBean(Publication publication, boolean loadSamples) {
		super(publication);
		this.domainFile = publication;
		Collection<Author> authorCollection =
			publication.getAuthorCollection();
		if (authorCollection!=null &&
				authorCollection.size()>0) {
			List<Author> authorslist = new ArrayList<Author>(authorCollection);
			Collections.sort(authorslist, 
					new Comparator<Author>() {
			    public int compare(Author o1, Author o2) {
			        return (int)(o2.getCreatedDate().compareTo(o1.getCreatedDate()));
			    }});
			authors = authorslist;
		}
		String researchAreasStr = 
			publication.getResearchArea();
		if (researchAreasStr!=null && researchAreasStr.length()>0) {
			researchAreas = researchAreasStr.split(delimiter);
		}else {
			researchAreas = null;
		}		

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
			Long thisId = this.domainFile.getId();
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
	 * @return the researchAreas
	 */
	public String[] getResearchAreas() {
		return researchAreas;
	}

	/**
	 * @param researchAreas the researchAreas to set
	 */
	public void setResearchAreas(String[] researchAreas) {
		this.researchAreas = researchAreas;
	}

	/**
	 * @return the authors
	 */
	public List<Author> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

//	/**
//	 * @return the authorsStr
//	 */
//	public String getAuthorsStr() {
//		return authorsStr;
//	}
//
//	/**
//	 * @param authorsStr the authorsStr to set
//	 */
//	public void setAuthorsStr(String authorsStr) {
//		this.authorsStr = authorsStr;
//	}
	
	
	public void addAuthor() {
		authors.add(new Author());
	}

	public void removeAuthor(int ind) {
		authors.remove(ind);
	}

	public boolean isFoundPubMedArticle() {
		return foundPubMedArticle;
	}

	public void setFoundPubMedArticle(boolean foundPubMedArticle) {
		this.foundPubMedArticle = foundPubMedArticle;
	}

	
	
}
