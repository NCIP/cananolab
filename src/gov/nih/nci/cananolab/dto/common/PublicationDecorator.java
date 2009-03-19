package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a publication
 * to be shown properly in the view page using display tag lib.
 *
 * @author tanq
 *
 */
public class PublicationDecorator extends TableDecorator {

	public SortableName getPublicationType()
			throws UnsupportedEncodingException {
		String publicationType = null;
		PublicationBean publicationBean = (PublicationBean) getCurrentRowObject();
		Publication publication = (Publication) publicationBean.getDomainFile();
		String category = publication.getCategory();
		if (category != null) {
			publicationType = publication.getCategory();
		}
		SortableName sortableLink = new SortableName(publicationType);
		return sortableLink;
	}

	public SortableName getResearchArea() throws UnsupportedEncodingException {
		String researchArea = "";
		File file = ((PublicationBean) getCurrentRowObject()).getDomainFile();
		researchArea = ((Publication) file).getResearchArea();
		if (researchArea != null) {
			researchArea = researchArea.replaceAll(";", "<br>");
		}

		SortableName sortableLink = new SortableName(researchArea);
		return sortableLink;
	}

	public SortableName getEditPublicationURL()
			throws UnsupportedEncodingException {
		PublicationBean file = (PublicationBean) getCurrentRowObject();
		String fileId = file.getDomainFile().getId().toString();
		StringBuilder sb = new StringBuilder("<a href=");
		sb
				.append("publication.do?page=0&dispatch=detailView&publicationId=");
		sb.append(fileId);
		sb.append("&location=");
		sb.append(file.getLocation());
		sb.append(">");
		if (file.getDomainFile().getTitle().length() > 30) {
			sb.append(file.getDomainFile().getTitle().substring(0, 30));
		} else {
			sb.append(file.getDomainFile().getTitle());
		}
		sb.append("</a>");
		String link = sb.toString();
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}

	public SortableName getDownloadURL() throws UnsupportedEncodingException {
		SortableName sortableLink = null;
		String actionName = null;
		FileBean file = null;
		actionName = "searchPublication.do";
		file = (PublicationBean) getCurrentRowObject();
		;

		if (file.getDomainFile().getName() != null) {
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append(actionName);
			sb.append("?dispatch=download&fileId=");
			sb.append(file.getDomainFile().getId());
			sb.append("&location=");
			sb.append(file.getLocation());
			sb.append(" target='");
			sb.append(file.getUrlTarget());
			sb.append("'>");
			sb.append(file.getDomainFile().getName());
			sb.append("</a>");
			String link = sb.toString();
			sortableLink = new SortableName(file.getDomainFile().getName(),
					link);
		} else {
			sortableLink = new SortableName("");
		}
		return sortableLink;
	}

	public String getSampleNames() {
		String[] sampleNames = ((PublicationBean) getCurrentRowObject())
				.getSampleNames();
		if (sampleNames == null) {
			return "";
		}
		return StringUtils.sortJoin(Arrays.asList(sampleNames), "<br>");
	}

	public SortableName getViewName() {
		FileBean file = null;
		file = (PublicationBean) getCurrentRowObject();
		String title = file.getDomainFile().getTitle();
		SortableName sortableLink = new SortableName(title);
		return sortableLink;
	}
}
