package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.util.SortableName;

import java.io.UnsupportedEncodingException;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a protocol
 * file to be shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class ProtocolFileDecorator extends TableDecorator {
	public SortableName getEditURL() {
		ProtocolFileBean file = (ProtocolFileBean) getCurrentRowObject();
		if (!file.getLocation().equals("local")){
			return getViewName();
		}
		// replace space with special char
		String fileId = file.getDomainFile().getId().toString();
//		String editProtocolURL = "submitProtocol.do?dispatch=setupUpdate&fileId="
//				+ fileId+"&location=local";
//		String link = "<a href="
//				+ editProtocolURL
//				+ ">"
//				+ ((ProtocolFile) (file.getDomainFile())).getProtocol()
//						.getName() + "</a>";
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("submitProtocol.do?dispatch=setupUpdate&fileId=");
		sb.append(fileId);
		sb.append("&location=local>");
		sb.append(((ProtocolFile) (file.getDomainFile())).getProtocol().getName());
		sb.append("</a>");
		String link = sb.toString();
		SortableName sortableLink = new SortableName(((ProtocolFile) (file
				.getDomainFile())).getProtocol().getName(), link);
		return sortableLink;
	}

	public SortableName getViewName() {
		ProtocolFileBean file = (ProtocolFileBean) getCurrentRowObject();
		String protocolName = ((ProtocolFile) (file.getDomainFile()))
				.getProtocol().getName();
		SortableName sortableLink = new SortableName(protocolName);
		return sortableLink;
	}

	public SortableName getDownloadURL() throws UnsupportedEncodingException {
		SortableName sortableLink = null;
		ProtocolFileBean file = (ProtocolFileBean) getCurrentRowObject();
		if (file.getDomainFile().getName() != null) {
//			String downloadURL = "searchProtocol.do?dispatch=download"
//					+ "&fileId=" + file.getDomainFile().getId() + "&location="
//					+ file.getLocation();
//			String link = "<a href=" + downloadURL + ">"
//					+ file.getDomainFile().getName() + "</a>";
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append("searchProtocol.do?dispatch=download&fileId=");
			sb.append(file.getDomainFile().getId());
			sb.append("&location=");
			sb.append(file.getLocation());
			sb.append(">");
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
}
