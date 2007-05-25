package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.SortableName;
import java.util.List;
import java.util.ArrayList;
import org.displaytag.decorator.TableDecorator;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
/**
 * This decorator is used to for decorate different properties of a report to be
 * shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class ProtocolDecorator extends TableDecorator {
	public SortableName getEditProtocolURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileId = file.getId();
		String editProtocolURL = "updateProtocol.do?dispatch=setupUpdate&fileId="
				+ fileId;
		String link = "<a href=" + editProtocolURL + ">" + ((ProtocolFileBean)file).getProtocolBean().getName()
				+ "</a>";
		SortableName sortableLink = new SortableName(((ProtocolFileBean)file).getProtocolBean().getName(), link);
		return sortableLink;
	}

	public SortableName getViewProtocolURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileId = file.getId();
		String editProtocolURL = "updateProtocol.do?dispatch=setupView&fileId="
				+ fileId;
		String link = "<a href=" + editProtocolURL + ">" + ((ProtocolFileBean)file).getProtocolBean().getName()
				+ "</a>";
		SortableName sortableLink = new SortableName(((ProtocolFileBean)file).getProtocolBean().getName(), link);
		return sortableLink;
	}

public SortableName getRemoteDownloadURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// TODO add URL
		String downloadURL = "remoteSearchReport.do?dispatch=download&gridNodeHost="+file.getGridNode()+"&fileId="
				+ file.getId() + "&fileName=" + file.getName();
		String link = "<a href=" + downloadURL + ">" + file.getTitle() + "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}}
