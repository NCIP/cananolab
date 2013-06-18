/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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
	public String getFileInfo() {
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		FileBean file = protocol.getFileBean();
		StringBuilder sb = new StringBuilder();
		if (file != null && file.getDomainFile() != null) {
			// file uri is null, not a valid file return empty string
			if (StringUtils.isEmpty(file.getDomainFile().getUri())) {
				return "";
			}
			if (!StringUtils.isEmpty(file.getDomainFile().getTitle())) {
				sb.append("Title:").append("<br>")
						.append(file.getDomainFile().getTitle()).append("<br>");
			}
			String description = protocol.getDomain().getFile()
					.getDescription();
			if (!StringUtils.isEmpty(description)) {
				sb.append("<br>")
						.append("Description:")
						.append("<br>")
						.append(StringUtils
								.escapeXmlButPreserveLineBreaks(description)).append("<br>");
			}
			if (file.getDomainFile().getId() != null) {
				String link = "protocol.do?dispatch=download&fileId="
						+ file.getDomainFile().getId();
				String linkText = "View";
				sb.append("<br>").append("<a href=");
				sb.append(link).append(" target='new'>");
				sb.append(linkText);
				sb.append("</a>");
			}
		}
		return sb.toString();
	}
}
