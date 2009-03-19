/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;

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
public class PublicationBean extends FileBean {
	private static final String delimiter = ";";

	private String[] sampleNames;
	private String[] researchAreas;
	private List<Author> authors = new ArrayList<Author>(20);

	private boolean foundPubMedArticle = false;

	private String bibliographyInfo = "";;

	public PublicationBean() {
		super();
		domainFile = new Publication();
		domainFile.setUriExternal(false);
		authors.add(new Author());
	}

	public PublicationBean(Publication publication) {
		super(publication);
		this.domainFile = publication;

		String researchAreasStr = publication.getResearchArea();
		if (researchAreasStr != null && researchAreasStr.length() > 0) {
			researchAreas = researchAreasStr.split(delimiter);
		} else {
			researchAreas = null;
		}
		Collection<Author> authorCollection = publication.getAuthorCollection();
		if (authorCollection != null && authorCollection.size() > 0) {
			List<Author> authorslist = new ArrayList<Author>(authorCollection);
			Collections.sort(authorslist, new Comparator<Author>() {
				public int compare(Author o1, Author o2) {
					return (int) (o1.getCreatedDate().compareTo(o2
							.getCreatedDate()));
				}
			});
			authors = authorslist;
		}
	}

	public PublicationBean(Publication publication, String[] sampleNames) {
		this(publication);
		this.sampleNames = sampleNames;
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

	public String[] getSampleNames() {
		return sampleNames;
	}

	public void setSampleNames(String[] sampleNames) {
		this.sampleNames = sampleNames;
	}

	/**
	 * @return the researchAreas
	 */
	public String[] getResearchAreas() {
		return researchAreas;
	}

	/**
	 * @param researchAreas
	 *            the researchAreas to set
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
	 * @param authors
	 *            the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	// /**
	// * @return the authorsStr
	// */
	// public String getAuthorsStr() {
	// return authorsStr;
	// }
	//
	// /**
	// * @param authorsStr the authorsStr to set
	// */
	// public void setAuthorsStr(String authorsStr) {
	// this.authorsStr = authorsStr;
	// }

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

	public String getBibliographyInfo() {
		Publication pub = (Publication) domainFile;

		if (pub.getJournalName() != null) {
			bibliographyInfo = pub.getJournalName();
		}
		if (pub.getYear() != null) {
			bibliographyInfo += ", " + pub.getYear();
		}
		if (pub.getVolume() != null) {
			bibliographyInfo += ". Volume " + pub.getVolume();
		}
		if (pub.getStartPage() != null && pub.getEndPage() != null) {
			bibliographyInfo += ", pp." + pub.getStartPage() + "-"
					+ pub.getEndPage();
		}

		return bibliographyInfo;
	}
}
