package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.SortableName;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a study to be
 * shown properly in the view page using display tag lib.
 *
 * @author lethai
 *
 */
public class StudyDecorator extends TableDecorator {
 
	public String getDetailURL() {
		UserBean user = (UserBean)this.getPageContext().getSession().getAttribute("user");
		
		
		//SampleBean sample = (SampleBean) getCurrentRowObject();
		//String sampleId = sample.getDomain().getId().toString();
		String dispatch = "summaryView";
		String linkLabel = "View";
		if (user!= null) {
			dispatch = "summaryEdit";
			linkLabel = "Edit";
		}
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("study.do?dispatch=").append(dispatch).append(
				"&page=0&studyId=");
		sb.append("1").append('>');
		sb.append(linkLabel).append("</a>");
		String link = sb.toString();
		
		return link;
	}

	public SortableName getStudyName() {
		String studyName = "Efficacy of nanoparticle";
		return new SortableName(studyName);
	}
	public SortableName getSampleName() {
		//SampleBean sample = (SampleBean) getCurrentRowObject();
		String sampleName = "NCL-23-1<br>NCL-23-2"; //sample.getDomain().getName();
		return new SortableName(sampleName);
	}
	
	public String getOwnerStr() throws BaseException {
		
		return "Guest";
	}

	public String getDiseasesStr() throws BaseException {
		
		return "Lung Cancer<br>Heart cancer";
	}

	public String getPointOfContactName() throws BaseException {
		//SampleBean sample = (SampleBean) getCurrentRowObject();
		return "leth";
	}
}
