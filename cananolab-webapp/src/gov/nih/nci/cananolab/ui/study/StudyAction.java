package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh, lethai
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
	public static final String PAGE_TITLE = "pageTitle";

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
	}
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute(PAGE_TITLE, "Submit New Study");
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute(PAGE_TITLE, "Update Study");
		return mapping.findForward("studyEdit");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("summaryEdit");
	}
	
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("summaryView");
	}
}
