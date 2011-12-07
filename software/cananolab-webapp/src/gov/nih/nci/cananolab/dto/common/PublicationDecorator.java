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
 * @author tanq, pansu
 *
 */
public class PublicationDecorator extends TableDecorator {

	public SortableName getPublicationType()
			throws UnsupportedEncodingException {
		String publicationType = null;
		PublicationBean publicationBean = (PublicationBean) getCurrentRowObject();
		Publication publication = (Publication) publicationBean.getDomainFile();
		String category = publication.getCategory();
		if (!StringUtils.isEmpty(category)) {
			publicationType = publication.getCategory();
		}
		SortableName sortableLink = new SortableName(publicationType);
		return sortableLink;
	}

	public SortableName getResearchArea() throws UnsupportedEncodingException {
		String researchArea = "";
		File file = ((PublicationBean) getCurrentRowObject()).getDomainFile();
		researchArea = ((Publication) file).getResearchArea();
		if (!StringUtils.isEmpty(researchArea)) {
			researchArea = researchArea.replaceAll(";", "\r\n");
		}
		SortableName sortableLink = new SortableName(StringUtils
				.escapeXmlButPreserveLineBreaks(researchArea));
		return sortableLink;
	}

	public String getDetailURL() {
		PublicationBean publication = (PublicationBean) getCurrentRowObject();
		if (publication.getUserUpdatable()) {
			String fileId = publication.getDomainFile().getId().toString();
			StringBuilder sb = new StringBuilder("<a href=");
			sb
					.append("publication.do?page=0&dispatch=setupUpdate&publicationId=");
			sb.append(fileId);
			sb.append(">");
			sb.append("Edit");
			sb.append("</a>");
			return sb.toString();
		} else {
			return "";
		}
	}

	public String getSampleNames() {
		String[] sampleNames = ((PublicationBean) getCurrentRowObject())
				.getSampleNames();
		if (sampleNames == null) {
			return "";
		}
		String sampleNamesStr = StringUtils.sortJoin(
				Arrays.asList(sampleNames), "\r\n");
		return StringUtils.escapeXmlButPreserveLineBreaks(sampleNamesStr);
	}

	public String getDescriptionDetail() {
		PublicationBean publication = (PublicationBean) getCurrentRowObject();
		String description = StringUtils
				.escapeXmlButPreserveLineBreaks(publication.getDomainFile()
						.getDescription());
		if (StringUtils.isEmpty(description)) {
			return null;
		}
		StringBuilder sb = new StringBuilder("<div id=\"descriptionSection"
				+ getListIndex() + "\" style=\"position: relative;\">");
		sb
				.append("<a style=\"display: block\" id=\"viewDetail\" href=\"#\" onmouseOver=");
		sb.append("javascript:show('publicationDescription" + getListIndex()
				+ "'); onmouseOut=");
		sb.append("javascript:hide('publicationDescription" + getListIndex()
				+ "');>");
		sb.append("View");
		sb.append("</a>");
		sb
				.append("<table id=\"publicationDescription"
						+ getListIndex()
						+ "\" style=\"display: none;position: absolute;left: -510px;top: -50px;width: 500px;z-index:5;font-size: 10px;background-color: #FFFFFF;\" class=\"promptbox\">");
		sb.append("<tr><td>");
		sb.append(description);
		sb.append("</td></tr>");
		sb.append("</table></div>");
		return sb.toString();
	}
}
