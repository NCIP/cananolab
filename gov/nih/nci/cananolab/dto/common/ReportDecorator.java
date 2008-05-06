package gov.nih.nci.cananolab.dto.common;

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
		String fileId = file.getDomainFile().getId().toString();
		String editReportURL = "submitReport.do?submitType=none&page=0&dispatch=setupUpdate&fileId="
				+ fileId;
		String link = "<a href=" + editReportURL + ">"
				+ file.getDomainFile().getTitle() + "</a>";
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}

	public SortableName getViewReportURL() throws UnsupportedEncodingException {
		ReportBean file = (ReportBean) getCurrentRowObject();
		String fileId = file.getDomainFile().getId().toString();
		String editReportURL = "submitReport.do?submitType=none&page=0&dispatch=setupView&fileId="
				+ fileId;
		String link = "<a href=" + editReportURL + ">"
				+ file.getDomainFile().getTitle() + "</a>";
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}

	public String getParticleNames() {
		ReportBean file = (ReportBean) getCurrentRowObject();
		return StringUtils.sortJoin(Arrays.asList(file.getParticleNames()),
				"<br>");
	}
}
