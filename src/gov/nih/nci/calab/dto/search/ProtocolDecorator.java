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
	private static String TABLE_START = "<table width=\"100%\" align=\"center\"><tr><td>";
	private static String TR_TD_START = "<tr><td>";
	private static String TR_TD_END = "</td></tr>";
	private static String TD_START = "<td>";
	private static String TD_END = "</td>";
	private static String TD_START_END = "</td><td>";
	private static String TABLE_END = "</table>";
	public SortableName getEditProtocolURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileId = file.getId();
		String editProtocolURL = "updateProtocol.do?dispatch=setupUpdate&fileId="
				+ fileId;
		String link = "<a href=" + editProtocolURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public SortableName getViewProtocolURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileId = file.getId();
		String editProtocolURL = "updateProtocol.do?dispatch=setupView&fileId="
				+ fileId;
		String link = "<a href=" + editProtocolURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}
	/*
	public String getViewProtocolURL() {
		ProtocolBean pb = (ProtocolBean)getCurrentRowObject();
		List<ProtocolFileBean> files = pb.getFileBeanList();
		// replace space with special char
		StringBuffer sb = new StringBuffer(TABLE_START);
		boolean isStart = true;
		for (ProtocolFileBean file : files ){
			if (!isStart){
				sb.append(TR_TD_START);
			}
			sb.append(file.getVersion());
			sb.append(TD_START_END);
			sb.append(file.getTitle());
			sb.append(TD_START_END);
			sb.append(file.getDescription());
			sb.append(TR_TD_END);
			isStart = false;
		}
		sb.append(TABLE_END);

		return sb.toString();
	}
	*/
public SortableName getRemoteDownloadURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// TODO add URL
		String downloadURL = "remoteSearchReport.do?dispatch=download&gridNodeHost="+file.getGridNode()+"&fileId="
				+ file.getId() + "&fileName=" + file.getName();
		String link = "<a href=" + downloadURL + ">" + file.getTitle() + "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}}
