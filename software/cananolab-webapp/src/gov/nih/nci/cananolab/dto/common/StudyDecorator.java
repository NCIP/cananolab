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

		StudyBean study = (StudyBean)getCurrentRowObject();
		String studyId = study.getDomain().getId().toString();
		String dispatch = "summaryView";
		String linkLabel = "View";
		if (user!= null) {
			dispatch = "summaryEdit";
			linkLabel = "Edit";
		}
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("study.do?dispatch=").append(dispatch).append(
				"&page=0&studyId=");
		sb.append(studyId).append('>');
		sb.append(linkLabel).append("</a>");
		String link = sb.toString();

		return link;
	}

	public SortableName getStudyName() {
		StudyBean study = (StudyBean)getCurrentRowObject();		
		String studyName = study.getDomain().getName(); //"MIT_MGH-KKellyIB";
		return new SortableName(studyName);
	}
	public SortableName getSampleName() {
		//SampleBean sample = (SampleBean) getCurrentRowObject();
		String sampleName = "MIT_MGH-KKellyIB2009-01<br>MIT_MGH-KKellyIB2009-02<br>"; //sample.getDomain().getName();
		return new SortableName(sampleName);
	}

	public String getStudyTitle() {
		StudyBean study = (StudyBean)getCurrentRowObject();		
		String studyTitle = study.getDomain().getTitle();
		return studyTitle; 
		// "Unbiased discovery of in vivo imaging probes through in vitro profiling of nanoparticle libraries";
	}

	public String getOwnerStr() throws BaseException {
		StudyBean study = (StudyBean)getCurrentRowObject();
		String studyOwner = study.getDomain().getCreatedBy();
		return studyOwner; //"michal";
	}

	public String getDiseasesStr() throws BaseException {
		StudyBean study = (StudyBean)getCurrentRowObject();
		String diseases = study.getDiseaseNames();
		return diseases; //"lung cancer";
	}

	public String getPointOfContactName() throws BaseException {
		StudyBean study = (StudyBean)getCurrentRowObject();
		String poc = study.getPrimaryPOCBean().getDisplayName();
		return poc; // "MIT_MGH (Stanley Y Shaw)";
	}
}
