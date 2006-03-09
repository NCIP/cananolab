package gov.nih.nci.calab.ui.workflow;
/**
 * This class saves the association between a run and the user selected aliquot IDs and comments .
 * 
 * @author pansu
 */

/* CVS $Id: UseAliquotAction.java,v 1.2 2006-03-09 17:26:47 pansu Exp $*/

import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

public class UseAliquotAction extends AbstractBaseAction {
	private static Logger logger=Logger.getLogger(LoadAliquotsAction.class);
	
	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
		String runId=(String)theForm.get("runId");
		String[] aliquotIds=(String[])theForm.get("aliquotIds");
		String action=(String)theForm.get("action");
		String comments=(String)theForm.get("comments");
		try {
			/**@todo fill in details for saving aliquot IDs and comments*/
			if (action.equalsIgnoreCase("submit")) {
				ActionMessages msgs=new ActionMessages();
				ActionMessage msg=new ActionMessage("message.useAliquot");
				msgs.add("message", msg);
				saveMessages(request, msgs);
				forward=mapping.findForward("success");	
			}
			else if (action.equalsIgnoreCase("cancel")) {
				forward=mapping.findForward("blank");
			}
			else {
				forward=mapping.getInputForward();
			}
		}
		catch(Exception e) {
			logger.error("Caught exception when saving selected aliquot IDs.", e);
			ActionMessages errors=new ActionMessages();
			ActionMessage error=new ActionMessage("error.useAliquot");
			errors.add("error", error);
			saveMessages(request, errors);
			forward=mapping.getInputForward();
		}
		return forward;
	}
	
	public boolean loginRequired() {
		//temporarily set to false until login module is working
		return false; 
		//return true;
	}

}

