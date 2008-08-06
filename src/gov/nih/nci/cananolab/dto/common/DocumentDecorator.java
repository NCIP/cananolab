package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a document to be
 * shown properly in the view page using display tag lib.
 * 
 * @author tanq
 * 
 */
public class DocumentDecorator extends TableDecorator {
	
	public SortableName getPublicationOrReport() throws UnsupportedEncodingException {
		String publicationOrReport = "otherType";
		Object documentBean = getCurrentRowObject();
		if (documentBean instanceof ReportBean){
			publicationOrReport = "report";
		}else if (documentBean instanceof PublicationBean){
			LabFile file = ((PublicationBean)documentBean).getDomainFile();
			publicationOrReport = "publication"+":"+
				((Publication)file).getCategory();
		}
		SortableName sortableLink = new SortableName(publicationOrReport);
		return sortableLink;
	}
	
	public SortableName getResearchArea() throws UnsupportedEncodingException {
		String researchArea = "";
		Object documentBean = getCurrentRowObject();
		if (documentBean instanceof ReportBean){
			researchArea = "";
		}else if (documentBean instanceof PublicationBean){
			LabFile file = ((PublicationBean)documentBean).getDomainFile();
			researchArea = ((Publication)file).getResearchArea();
			if (researchArea!=null) {
				researchArea = researchArea.replaceAll(";", "<br>");
			}
		}
		SortableName sortableLink = new SortableName(researchArea);
		return sortableLink;
	}
		
	public SortableName getEditDocumentURL() throws UnsupportedEncodingException {
		Object documentBean = getCurrentRowObject();
		if (documentBean instanceof ReportBean){
			return getEditReportURL();
		}else if (documentBean instanceof PublicationBean){
			return getEditPublicationURL();
		}else{
			return null;
		}
	}
	
	public SortableName getEditReportURL() throws UnsupportedEncodingException {
		ReportBean file = (ReportBean) getCurrentRowObject();
		if (!file.getLocation().equals("local")){
			return getViewName();
		}
		String fileId = file.getDomainFile().getId().toString();
		//TODO, submitReport or submitPublication
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("submitReport.do?submitType=none&page=0&dispatch=setupUpdate&fileId=");
		sb.append(fileId);
		sb.append("&location=local>");
		if (file.getDomainFile().getTitle().length()>30) {
			sb.append(file.getDomainFile().getTitle().substring(0, 30));
		}else {
			sb.append(file.getDomainFile().getTitle());
		}
		sb.append("</a>");
		String link = sb.toString();
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}
	
	public SortableName getEditPublicationURL() throws UnsupportedEncodingException {
		PublicationBean file = (PublicationBean) getCurrentRowObject();
		if (!file.getLocation().equals("local")){
			return getViewName();
		}
		String fileId = file.getDomainFile().getId().toString();
		//TODO, submitReport or submitPublication
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("submitPublication.do?submitType=none&page=0&dispatch=setupUpdate&fileId=");
		sb.append(fileId);
		sb.append("&location=local>");
		if (file.getDomainFile().getTitle().length()>30) {
			sb.append(file.getDomainFile().getTitle().substring(0, 30));
		}else {
			sb.append(file.getDomainFile().getTitle());
		}		
		sb.append("</a>");
		String link = sb.toString();
		SortableName sortableLink = new SortableName(file.getDomainFile()
				.getTitle(), link);
		return sortableLink;
	}

	public SortableName getDownloadURL() throws UnsupportedEncodingException {
		SortableName sortableLink = null;
		String actionName = null;
		Object documentBean = getCurrentRowObject();
		LabFileBean file = null;
		//TODO, try to not use instanceof??
		if (documentBean instanceof ReportBean){
			actionName = "searchReport.do";
			file = (ReportBean) documentBean;
		}else if (documentBean instanceof PublicationBean){
			actionName = "searchPublication.do";
			file = (PublicationBean) documentBean;
		}				
		if (file.getDomainFile().getName() != null) {
			StringBuilder sb = new StringBuilder("<a href=");
			sb.append(actionName);
			sb.append("?dispatch=download&fileId=");
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
		Object documentBean = getCurrentRowObject();
		String[] particleNames = null;
		if (documentBean instanceof ReportBean){
			particleNames = ((ReportBean) documentBean).getParticleNames();
		}else if (documentBean instanceof PublicationBean){
			particleNames = ((PublicationBean) documentBean).getParticleNames();
		}		
		return StringUtils.sortJoin(Arrays.asList(particleNames),
				"<br>");
	}
	
	public SortableName getViewName() {
		Object documentBean = getCurrentRowObject();
		LabFileBean file = null;
		if (documentBean instanceof ReportBean){
			file = (ReportBean) documentBean;
		}else if (documentBean instanceof PublicationBean){
			file = (PublicationBean) documentBean;
		}		
		String title = file.getDomainFile().getTitle();		
		SortableName sortableLink = new SortableName(title);
		return sortableLink;
	}	
	
	public SortableName getDocumentType_retire() throws UnsupportedEncodingException {
		String type = "";
		Object documentBean = getCurrentRowObject();
		if (documentBean instanceof PublicationBean){
			LabFileBean file = (PublicationBean) documentBean;
			file.getDomainFile().getTitle();		
		}
		SortableName sortableLink = new SortableName(type);		
		return sortableLink;
	}
	
}
