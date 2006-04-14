package gov.nih.nci.calab.ui.workflow;

/**
 * This class prepares the data required for the workflow tree
 * 
 * @author pansu
 */

/* CVS $Id: ExecuteWorkflowAction.java,v 1.4 2006-04-14 22:22:54 pansu Exp $*/

import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ExecuteWorkflowAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(ExecuteWorkflowAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();		
		try {			
			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			Map assayMap = executeWorkflowService.getWorkflowAssays();
			if (assayMap.isEmpty()) {
				ActionMessage error = new ActionMessage("error.noworkflow");
				msgs.add("error", error);
				saveMessages(request, msgs);
				forward = mapping.findForward("failure");
			} else {
				int totalAssayCount=0;
				int totalRunCount=0;
				int totalInputFileCount=0;
				int totalOutputFileCount=0;
				int totalAliquotCount=0;
				for (Object entry: assayMap.entrySet()) {
					List assays=(List)(((Map.Entry)entry).getValue());
					totalAssayCount+=assays.size();
					for (Object assay: assays) {
						List runs=(List)((AssayBean)assay).getRunBeans();
						totalRunCount+=runs.size();
						for (Object run: runs) {
							List aliquots=(List)((RunBean)run).getAliquotBeans();
							totalAliquotCount+=aliquots.size();
							List inputFiles=(List)((RunBean)run).getInputFileBeans();
							totalInputFileCount+=inputFiles.size();
							List outputFiles=(List)((RunBean)run).getOutputFileBeans();
							totalOutputFileCount+=outputFiles.size();

						}
					}
				}
				request.setAttribute("assayMap", assayMap);
				request.setAttribute("totalAssayCount", totalAssayCount);
				request.setAttribute("totalRunCount", totalRunCount);
				request.setAttribute("totalAliquotCount", totalAliquotCount);
				request.setAttribute("totalInputFileCount", totalInputFileCount);
				request.setAttribute("totalOutputFileCount", totalOutputFileCount);
				forward = mapping.findForward("success");
			}
		} catch (Exception e) {
			logger.error(
					"Caught exception when searching for existing worklows", e);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.executeWorkflow");
			msgs.add("error", error);
			saveMessages(request, errors);
			forward = mapping.findForward("failure");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

}
