package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchStudyAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StudyBean study1 = new StudyBean();
		study1.setName("study 1");
		//SampleBean studySample = new SampleBean();
		//studySample.se
		study1.setStudySample(new SampleBean());
		study1.setOwnerName("Guest");
		study1.setPointOfContact("Guest");

		StudyBean study2 = new StudyBean();
		study1.setName("study 1");
		//SampleBean studySample = new SampleBean();
		//studySample.se
		study2.setStudySample(new SampleBean());
		study2.setOwnerName("Guest2");
		study2.setPointOfContact("Guest2");
		List<StudyBean> studyBeansPerPage = new ArrayList<StudyBean>();
		studyBeansPerPage.add(study1);
		studyBeansPerPage.add(study2);
		request.setAttribute("studies", studyBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", new Integer(20));
		return mapping.findForward("searchResult");
	}

}
