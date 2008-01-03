package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
		// replace space with special char
		String fileInstanceType = URLEncoder.encode(file.getInstanceType(),
				"UTF-8");
		String fileId = file.getId();
		String editReportURL = "updateReport.do?submitType=none&page=0&dispatch=setupUpdate&fileType="
				+ fileInstanceType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public SortableName getViewReportURL() throws UnsupportedEncodingException {
		ReportBean file = (ReportBean) getCurrentRowObject();
		// replace space with special char
		String fileInstanceType = URLEncoder.encode(file.getInstanceType(),
				"UTF-8");
		String fileId = file.getId();
		String editReportURL = "updateReport.do?submitType=none&page=0&dispatch=setupView&fileType="
				+ fileInstanceType + "&fileId=" + fileId;
		String link = "<a href=" + editReportURL + ">" + file.getTitle()
				+ "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public SortableName getRemoteDownloadURL()
			throws UnsupportedEncodingException {
		ReportBean file = (ReportBean) getCurrentRowObject();
		String gridNode = URLEncoder.encode(file.getGridNode(), "UTF-8");
		String fileName = URLEncoder.encode(file.getName(), "UTF-8");
		String downloadURL = "remoteSearchReport.do?dispatch=download&gridNodeHost="
				+ gridNode
				+ "&fileId="
				+ file.getId()
				+ "&fileName="
				+ fileName;
		String link = "<a href=" + downloadURL + ">" + file.getTitle() + "</a>";
		SortableName sortableLink = new SortableName(file.getTitle(), link);
		return sortableLink;
	}

	public String getParticleIds() {
		ReportBean file = (ReportBean) getCurrentRowObject();
		List<String> particleIdList = new ArrayList<String>();
		for (ParticleBean particle : file.getParticleList()) {
			particleIdList.add(particle.getSampleName());
		}
		return StringUtils.sortJoin(particleIdList, "<br>");
	}
}
