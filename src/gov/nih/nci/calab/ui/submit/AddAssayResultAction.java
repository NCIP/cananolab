package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a particle.  
 *  
 * @author pansu
 */

/* CVS $Id: AddAssayResultAction.java,v 1.3 2006-08-31 15:29:05 pansu Exp $ */

import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class AddAssayResultAction extends AbstractDispatchAction {
	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String fileId = (String) theForm.get("fileId");
		String title = (String) theForm.get("title");
		String description = (String) theForm.get("description");
		String comments = (String) theForm.get("comments");
		String keywords = (String) theForm.get("keywords");
		String[] keywordList = keywords.split("\r\n");

		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.saveAssayResult(particleName, fileId, title, description,
				comments, keywordList);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addAssayResult", fileId);
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllSampleContainers(session);
		
//		SubmitNanoparticleService service = new SubmitNanoparticleService();
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		String particleName = (String) theForm.get("particleName");
//		List<FileBean> runFiles = service.getAllRunFiles(particleName);
//		request.setAttribute("particleRunFiles", runFiles);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
