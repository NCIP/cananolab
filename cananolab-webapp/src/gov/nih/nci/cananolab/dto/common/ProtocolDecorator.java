package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.dto.particle.SampleBean;
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

	public SortableName getDownloadURL() throws UnsupportedEncodingException {
		SortableName sortableLink = null;
		ProtocolBean protocol = (ProtocolBean) getCurrentRowObject();
		FileBean file = protocol.getFileBean();

		if (file != null) {
			String fileName = file.getDomainFile().getName();
			if (!StringUtils.isEmpty(file.getDomainFile().getName())) {
				StringBuilder sb = new StringBuilder("<a href=");
				sb.append("searchProtocol.do?dispatch=download&fileId=");
				sb.append(file.getDomainFile().getId()).append(">");
				String fileTitle = file.getDomainFile().getTitle();
				if (StringUtils.isEmpty(fileTitle)) {
					fileTitle = file.getDomainFile().getUri();
				}
				sb.append(fileTitle);
				sb.append("</a>");
				sortableLink = new SortableName(fileName, sb.toString());
			} else {
				String fileTitle = file.getDomainFile().getTitle();
				if (!StringUtils.isEmpty(fileTitle)) {
					sortableLink = new SortableName(fileTitle);
				} else {
					sortableLink = new SortableName("");
				}
			}
		} else {
			sortableLink = new SortableName("");
		}
		return sortableLink;
	}
}
