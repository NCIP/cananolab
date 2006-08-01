package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves the association between a run and the user selected aliquot IDs and comments .
 * 
 * @author pansu
 */

/* CVS $Id: UseAliquotAction.java,v 1.16 2006-08-01 19:47:24 pansu Exp $*/

import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class UseAliquotAction extends AbstractDispatchAction {
	public ActionForward use(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		String[] assignedAliquots = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String runId = (String) theForm.get("runId");
		assignedAliquots = (String[]) theForm.get("assignedAliquots");
		String comments = (String) theForm.get("comments");

		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		executeWorkflowService.saveRunAliquots(runId, assignedAliquots,
				comments, (String) session.getAttribute("creator"),
				(String) session.getAttribute("creationDate"));
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.useAliquot");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		session.setAttribute("newRunCreated", "true");
		forward = mapping.findForward("success");
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session=request.getSession();
		RunBean currentRun = (RunBean) session.getAttribute("currentRun");
		if (currentRun == null) {
			throw new InvalidSessionException(
					"Can't operate outside the run tree");
		}
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setSampleSourceUnmaskedAliquots(session);
	
		//initialize sample list
		Map sampleSources=(Map)session.getAttribute("sampleSourceSamplesWithUnmaskedAliquots");
		Set samples=(Set)sampleSources.get(currentRun.getSampleSourceName());
		String[] sampleNames=new String[samples.size()];
		int i=0;
		for (Object obj: samples) {			
			sampleNames[i]=((SampleBean)obj).getSampleName();
			i++;
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("sampleNames", sampleNames);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
