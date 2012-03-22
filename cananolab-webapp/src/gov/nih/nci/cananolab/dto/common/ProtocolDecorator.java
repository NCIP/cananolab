package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.UnsupportedEncodingException;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a protocol
 * file to be shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class ProtocolDecorator extends TableDecorator {
	public String getDetailURL() {
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		if (protocol.getUserUpdatable()) {
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append("protocol.do?dispatch=setupUpdate&protocolId=");
			sb.append(protocol.getDomain().getId()).append('>');
			sb.append("Edit").append("</a>");
			return sb.toString();
		} else {
			return "";
		}
	}

	public SortableName getViewName() {
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		String protocolName = protocol.getDomain().getName();
		SortableName sortableLink = new SortableName(protocolName);
		return sortableLink;
	}

	// per app scan, to filter out special characters
	public String getFileDescription() {
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		String description = "";
		if (protocol.getDomain().getFile() != null) {
			String str = protocol.getDomain().getFile().getDescription();
			if (str != null) {
				description = StringUtils.escapeXmlButPreserveLineBreaks(str);
			}
		}
		return description;
	}

	public String getDownloadURL() throws UnsupportedEncodingException {
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		FileBean file = protocol.getFileBean();

		if (file != null && file.getDomainFile() != null) {
			String link = "protocol.do?dispatch=download&fileId="
					+ file.getDomainFile().getId();
			String linkText = "View";
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append(link).append(" target='new'>");
			sb.append(linkText);
			sb.append("</a>");
			return sb.toString();
		} else {
			return "";
		}
	}
}
