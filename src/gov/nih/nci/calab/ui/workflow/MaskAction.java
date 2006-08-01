package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.service.workflow.MaskService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * The MastAction sets is a generalized MaskAction class that is designed to
 * Mask any caLAB object. The strMaskType checks the hidden field on a Mask form
 * and uses this to determine the appropriate action.
 * 
 * @author doswellj, pansu
 */
public class MaskAction extends AbstractDispatchAction {

	public ActionForward mask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;

		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String strMaskType = (String) theForm.get("maskType");
		String strDescription = theForm.getString("description");
		String strId=theForm.getString("itemId");
		String strName=theForm.getString("itemName");
		
		// 1.Call MaskService to mask caLab component based on type(e.g.,
		// Aliquot, File, etc.)
		// 2.Display message that masking was successful
		MaskService maskservice = new MaskService();

		strDescription = strDescription + "   Masked by "
				+ session.getAttribute("creator");
		maskservice.setMask(strMaskType, strId, strDescription);
		
		if (strMaskType.equals("aliquot")) {
			ActionMessages msgs=new ActionMessages();
			ActionMessage msg = new ActionMessage("message.maskAliquot", strName);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			session.setAttribute("newAliquotCreated", "true");
			forward = mapping.findForward("success");
		}
		if (strMaskType.equals("file")) {		
			ActionMessages msgs=new ActionMessages();
			ActionMessage msg = new ActionMessage("message.maskFile", strName);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.findForward("success");
		}
		session.setAttribute("newRunCreated", "true");
		forward = mapping.findForward("success");
		return forward;
	}
	
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}
	
	public boolean loginRequired() {
		return true;
	}

}
