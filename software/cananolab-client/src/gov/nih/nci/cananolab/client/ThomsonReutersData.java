package gov.nih.nci.cananolab.client;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents the data elements Thomson Reuters would like to
 * retrieve from caNanoLab
 * 
 * @author pansu
 * 
 */
public class ThomsonReutersData {
	private String recordId;
	private String title;
	private String source = "caNanoLab";
	private String sourceURL;
	private String dataDistributer;
	private String authorship;
	private String abstractText;
	private String citation;
	private static String FIELD_SEPARATOR = " , ";
	private static String ENTRY_SEPARATOR = " | ";
	public static String[] headers = new String[] { "RECORD ID", "TITLE",
			"SOURCE", "SOURCE URL", "DATA DISTRIBUTER/PUBLISHER", "AUTHORSHIP",
			"ABSTRACT", "CITATION" };

	private static String CANANOLAB_SOURCE_URL_PREFIX = "https://cananolab.nci.nih.gov/caNanoLab/sample.do?dispatch=summaryView&page=0&sampleId=";

	public ThomsonReutersData(String sampleId, String sampleName,
			PointOfContact poc, Collection<NanomaterialEntity> nanoEntities,
			Collection<Publication> publications) {
		recordId = sampleId;
		title = sampleName;
		sourceURL = CANANOLAB_SOURCE_URL_PREFIX + sampleId;
		// assume organization is loaded
		dataDistributer = this.getOrganizationDisplayName(poc);
		authorship = this.getPersonDisplayName(poc);
		abstractText = this.getNanomaterialEntityDescriptions(nanoEntities);
		citation = this.getPublicationDisplayNames(publications);
	}

	public ThomsonReutersData(String recordId, String title, String source,
			String sourceURL, String dataDistributer, String authorship,
			String abstractText, String citation) {
		super();
		this.recordId = recordId;
		this.title = title;
		this.source = source;
		this.sourceURL = sourceURL;
		this.dataDistributer = dataDistributer;
		this.authorship = authorship;
		this.abstractText = abstractText;
		this.citation = citation;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getTitle() {
		return title;
	}

	public String getSource() {
		return source;
	}

	public String getSourceURL() {
		return sourceURL;
	}

	public String getDataDistributer() {
		return dataDistributer;
	}

	public String getAuthorship() {
		return authorship;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public String getCitation() {
		return citation;
	}

	private String getPersonDisplayName(PointOfContact poc) {
		if (poc == null) {
			return "";
		}
		List<String> nameStrs = new ArrayList<String>();
		nameStrs.add(poc.getFirstName());
		nameStrs.add(poc.getMiddleInitial());
		nameStrs.add(poc.getLastName());
		String name = StringUtils.join(nameStrs, " ");
		nameStrs = new ArrayList<String>();
		nameStrs.add(name);
		nameStrs.add(poc.getEmail());
		nameStrs.add(poc.getPhone());
		return StringUtils.join(nameStrs, FIELD_SEPARATOR);
	}

	private String getOrganizationDisplayName(PointOfContact poc) {
		if (poc == null) {
			return null;
		}
		Organization org = poc.getOrganization();
		if (org == null) {
			return "";
		}
		List<String> orgStrs = new ArrayList<String>();

		orgStrs.add(org.getName());
		orgStrs.add(org.getStreetAddress1());
		orgStrs.add(org.getStreetAddress2());

		List<String> addressStrs = new ArrayList<String>();
		addressStrs.add(org.getCity());
		addressStrs.add(org.getState());
		addressStrs.add(org.getPostalCode());
		addressStrs.add(org.getCountry());

		orgStrs.add(StringUtils.join(addressStrs, " "));
		return StringUtils.join(orgStrs, FIELD_SEPARATOR);
	}

	private String getNanomaterialEntityDescriptions(
			Collection<NanomaterialEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return "";
		}
		List<String> entityStrs = new ArrayList<String>();
		for (NanomaterialEntity entity : entities) {
			String description = entity.getDescription();
			// remove the first sentence in the description that starts with
			// "This particle is described in"
			if (description != null) {
				int index = description.indexOf(".");
				if (index != -1) {
					String firstSentence = description.substring(0, index);
					if (firstSentence
							.startsWith("This nanoparticle is described in")) {
						description = description.substring(index + 1).trim();
					}
				}
			}
			entityStrs.add(description);
		}
		return StringUtils.join(entityStrs, ENTRY_SEPARATOR);
	}

	private String getPublicationDisplayNames(
			Collection<Publication> publications) {
		if (publications == null || publications.isEmpty()) {
			return "";
		}
		List<String> pubStrs = new ArrayList<String>();
		for (Publication pub : publications) {
			pubStrs.add(this.getPublicationDisplayName(pub));
		}
		return StringUtils.join(pubStrs, ENTRY_SEPARATOR);
	}

	private String getAuthorsDisplayName(Publication pub) {
		List<String> strs = new ArrayList<String>();
		for (Author author : pub.getAuthorCollection()) {
			List<String> authorStrs = new ArrayList<String>();
			authorStrs.add(author.getLastName());
			authorStrs.add(author.getInitial());
			strs.add(StringUtils.join(authorStrs, ", "));
		}
		return StringUtils.join(strs, FIELD_SEPARATOR);
	}

	private String getPublishInfoDisplayName(Publication pub) {
		String publishInfo = "";
		if (pub.getYear() != null) {
			publishInfo += pub.getYear().toString() + "; ";
		}
		if (!StringUtils.isEmpty((pub.getVolume()))) {
			publishInfo += pub.getVolume();
		}
		if (!StringUtils.isEmpty(pub.getVolume())
				&& !StringUtils.isEmpty(pub.getStartPage())
				&& !StringUtils.isEmpty(pub.getEndPage())) {
			publishInfo += ":" + pub.getStartPage() + "-" + pub.getEndPage();
		}
		return publishInfo;
	}

	private String getPublicationDisplayName(Publication pub) {
		// standard PubMed journal citation format
		// e.g. Freedman SB, Adler M, Seshadri R, Powell EC. Oral ondansetron
		// for gastroenteritis in a pediatric emergency department. N Engl J
		// Med. 2006 Apr 20;354(16):1698-705. PubMed PMID: 12140307.

		List<String> strs = new ArrayList<String>();
		strs.add(getAuthorsDisplayName(pub));
		// remove last . in the title
		if (pub.getTitle().endsWith(".")) {
			strs.add(pub.getTitle().substring(0, pub.getTitle().length() - 1));
		} else {
			strs.add(pub.getTitle());
		}
		strs.add(pub.getJournalName());
		strs.add(getPublishInfoDisplayName(pub));
		if (pub.getPubMedId() != null) {
			strs.add("PubMed PMID: " + pub.getPubMedId().toString());
		} else if (pub.getDigitalObjectId() != null) {
			strs.add("DOI: " + pub.getDigitalObjectId());
		}
		return StringUtils.join(strs, ". ") + ".";
	}
}
