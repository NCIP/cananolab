/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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

public class StudyProtocolAction extends BaseAnnotationAction {
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
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Submit Study Protocol");
		return mapping.getInputForward();
	}
}
