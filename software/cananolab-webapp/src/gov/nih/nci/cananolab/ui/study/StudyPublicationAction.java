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

public class StudyPublicationAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
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
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Submit Study");
		return mapping.getInputForward();
	}
	
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Add Study Publication");
		return mapping.findForward("add");
	}
}
