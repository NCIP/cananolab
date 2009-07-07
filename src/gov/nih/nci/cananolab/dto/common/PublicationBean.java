/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Publication view bean
 * 
 * @author tanq, pansu
 * 
 */
public class PublicationBean extends FileBean {
	private static final String delimiter = ";";

	private String[] sampleNames;
	private String[] researchAreas;
	private List<Author> authors = new ArrayList<Author>();
	private Author theAuthor = new Author();

	private String displayName = "";;

	public PublicationBean() {
		domainFile = new Publication();
		domainFile.setUriExternal(false);
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

	private String getAuthorsDisplayName() {
		List<String> strs = new ArrayList<String>();
		for (Author author : authors) {
			String authorDisplayName = "";
			List<String> authorStrs = new ArrayList<String>();
			authorStrs.add(author.getLastName());
			authorStrs.add(author.getInitial());
			authorDisplayName = StringUtils.join(authorStrs, ", ");
			strs.add(authorDisplayName);
		}
		return StringUtils.join(strs, ", ");
	}

	private String getPublishInfoDisplayName() {
		Publication pub = (Publication) domainFile;
		String publishInfo = "";
		if (pub.getYear()!=null) {
			publishInfo += pub.getYear().toString() + "; ";
		}
		if (!StringUtils.isEmpty((pub.getVolume()))) {
			publishInfo += pub.getVolume() + ":";
		}
		if (pub.getVolume() != null && pub.getStartPage() != null
				&& pub.getEndPage() != null) {
			publishInfo += pub.getStartPage() + "-" + pub.getEndPage();
		}
		return publishInfo;
	}

	public String getDisplayName() {
		// standard PubMed journal citation format
		// e.g. Freedman SB, Adler M, Seshadri R, Powell EC. Oral ondansetron
		// for gastroenteritis in a pediatric emergency department. N Engl J
		// Med. 2006 Apr 20;354(16):1698-705. PubMed PMID: 12140307.
		Publication pub = (Publication) domainFile;
		List<String> strs = new ArrayList<String>();
		strs.add(getAuthorsDisplayName());
		// remove last . in the title
		if (pub.getTitle().endsWith(".")) {
			pub.getTitle().substring(0, pub.getTitle().length() - 2);
		}
		strs.add(pub.getTitle());
		strs.add(pub.getJournalName());
		strs.add(getPublishInfoDisplayName());
		displayName = StringUtils.join(strs, ". ") + ".";
		return displayName;
	}

	public void setupDomain(String internalUriPath, String createdBy, int index)
			throws Exception {
		super.setupDomainFile(internalUriPath, createdBy, 0);
		Publication domain = (Publication) domainFile;
		if (domain.getPubMedId() != null && domain.getPubMedId() == 0) {
			domain.setPubMedId(null);
		}
		if (domain.getYear() != null && domain.getYear() == 0) {
			domain.setYear(null);
		}
		if (researchAreas != null && researchAreas.length > 0) {
			String researchAreasStr = StringUtils.join(researchAreas, ";");
			domain.setResearchArea(researchAreasStr);
		}

		for (Author author : authors) {
			if (!StringUtils.isEmpty(author.getFirstName())
					|| !StringUtils.isEmpty(author.getLastName())
					|| !StringUtils.isEmpty(author.getInitial())) {
				if (author.getCreatedDate() == null) {
					author.setCreatedDate(DateUtils.addSecondsToCurrentDate(1));
				}
				if (author.getCreatedBy() == null
						|| author.getCreatedBy().trim().length() == 0) {
					author.setCreatedBy(createdBy);
				}
			} else {
				author = null;
			}
		}
	}

	public Author getTheAuthor() {
		return theAuthor;
	}

	public void setTheAuthor(Author theAuthor) {
		this.theAuthor = theAuthor;
	}

	public void addAuthor(Author author) {
		// if an old one exists, remove it first
		int index = authors.indexOf(author);
		if (index != -1) {
			authors.remove(author);
			// retain the original order
			authors.add(index, author);
		} else {
			authors.add(author);
		}
	}

	public void removeAuthor(Author author) {
		authors.remove(author);
	}
}
