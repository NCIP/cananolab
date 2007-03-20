package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.SortableName;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a report to be
 * shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class ReportDecorator extends TableDecorator {
	public SortableName getEditReportURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileType = file.getType().replace(" ", "%20");
		String fileId = file.getId();
		String editReportURL = "updateReport.do?submitType=none&page=0&dispatch=setupUpdate&fileType="
				+ fileType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public SortableName getViewReportURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileType = file.getType().replace(" ", "%20");
		String fileId = file.getId();
		String editReportURL = "updateReport.do?submitType=none&page=0&dispatch=setupView&fileType="
				+ fileType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
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
