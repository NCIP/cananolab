package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a report to be
 * shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class ReportDecorator extends TableDecorator {
	public SortableName getEditReportURL() throws UnsupportedEncodingException {
		ReportBean file = (ReportBean) getCurrentRowObject();
		if (!file.getLocation().equals("local")){
			return getViewName();
		}
		String fileId = file.getDomainFile().getId().toString();
//		String editReportURL = "submitReport.do?submitType=none&page=0&dispatch=setupUpdate&fileId="
//				+ fileId+"&location=local";
//		String link = "<a href=" + editReportURL + ">"
//				+ file.getDomainFile().getTitle() + "</a>";
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("submitReport.do?submitType=none&page=0&dispatch=setupUpdate&fileId=");
		sb.append(fileId);
		sb.append("&location=local>");
		sb.append(file.getDomainFile().getTitle());
		sb.append("</a>");
		String link = sb.toString();
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}

	public SortableName getDownloadURL() throws UnsupportedEncodingException {
		SortableName sortableLink = null;
		ReportBean file = (ReportBean) getCurrentRowObject();
		if (file.getDomainFile().getName() != null) {
//			String downloadURL = "searchReport.do?dispatch=download"
//					+ "&fileId=" + file.getDomainFile().getId() + "&location="
//					+ file.getLocation();
//			String link = "<a href=" + downloadURL + " target='"
//					+ file.getUrlTarget() + "'>"
//					+ file.getDomainFile().getName() + "</a>";
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append("searchReport.do?dispatch=download&fileId=");
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

	public String getParticleNames() {
		ReportBean file = (ReportBean) getCurrentRowObject();
		return StringUtils.sortJoin(Arrays.asList(file.getParticleNames()),
				"<br>");
	}
	
	public SortableName getViewName() {
		ReportBean file = (ReportBean) getCurrentRowObject();
		String reportTitle = file.getDomainFile().getTitle();
		SortableName sortableLink = new SortableName(reportTitle);
		return sortableLink;
	}	
}
