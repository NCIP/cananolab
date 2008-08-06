/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
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
	private List<DocumentAuthor> authors = new ArrayList<DocumentAuthor>(20);
	
	private String pubmedId;
	private String journal;
	private String volume;
	private String title;
	private String abstractText;
	private String year;
	private int startPage;
	private int endPage;

	public PublicationBean() {
		super();
		domainFile = new Publication();
		domainFile.setUriExternal(false);
		authors.add(new DocumentAuthor());
	}

	public PublicationBean(Publication publication) {
		this(publication, true);
	}

	public PublicationBean(Publication publication, boolean loadSamples) {
		super(publication);
		this.domainFile = publication;
		Collection<DocumentAuthor> documentAuthorCollection =
			publication.getDocumentAuthorCollection();
		if (documentAuthorCollection!=null &&
				documentAuthorCollection.size()>0) {
			List<DocumentAuthor> authorslist = new ArrayList<DocumentAuthor>(documentAuthorCollection);
			Collections.sort(authorslist, 
					new Comparator<DocumentAuthor>() {
			    public int compare(DocumentAuthor o1, DocumentAuthor o2) {
			        return (int)(o1.getId() - o2.getId());
			    }});
			authors = authorslist;
		}
		String researchAreasStr = 
			publication.getResearchArea();
		//System.out.println("########## researchAreasStr "+researchAreasStr);
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

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getPubmedId() {
		return pubmedId;
	}

	public void setPubmedId(String pubmedId) {
		this.pubmedId = pubmedId;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
	public List<DocumentAuthor> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<DocumentAuthor> authors) {
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
		authors.add(new DocumentAuthor());
	}

	public void removeAuthor(int ind) {
		authors.remove(ind);
	}

	
	
}
