package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudyAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
	}
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Submit Study");
		return mapping.getInputForward();
	}

	public ActionForward studyEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Update Study");
		return mapping.findForward("studyEdit");
	}

	public ActionForward sampleSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("sampleSummary");
	}

	public ActionForward sampleAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("sampleAdd");
	}

	public ActionForward animalSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("animalSummary");
	}
	
	public ActionForward charSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("charSummary");
	}
	
	public ActionForward charEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("charEdit");
	}
	
	public ActionForward protocolSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("protocolSummary");
	}
	
	public ActionForward publicationSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("publicationSummary");
	}
	
}
