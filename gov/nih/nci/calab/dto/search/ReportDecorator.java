package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a report
 * to be shown properly in the view page using display tag lib.
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
		String editReportURL = "updateReport.do?page=0&dispatch=setupUpdate&fileType="
				+ fileType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">"
				+ file.getTitle() + "</a>";
		SortableName sortableLink=new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public SortableName getViewReportURL() {
		LabFileBean file = (LabFileBean) getCurrentRowObject();
		// replace space with special char
		String fileType = file.getType().replace(" ", "%20");
		String fileId = file.getId();
		String editReportURL = "updateReport.do?page=0&dispatch=setupView&fileType="
				+ fileType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">"
				+ file.getTitle() + "</a>";
		SortableName sortableLink=new SortableName(file.getTitle(), link);
		return sortableLink;
	}
}
