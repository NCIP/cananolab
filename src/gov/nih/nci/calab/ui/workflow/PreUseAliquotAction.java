package gov.nih.nci.calab.ui.workflow;
/**
 * This class prepares aliquot IDs to be used in the Use Aliquot input form.
 * 
 * @author pansu
 */

/* CVS $Id: PreUseAliquotAction.java,v 1.2 2006-03-20 21:53:13 pansu Exp $ */
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorActionForm;

public class PreUseAliquotAction extends AbstractBaseAction  {
	private static Logger logger=Logger.getLogger(PreUseAliquotAction.class);
	
	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		try {
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String runId=(String)theForm.get("runId");
			LookupService service=new LookupService();
			List<String> allAliquotIds=service.getAliquots();
			//use to populate the drop-down list.
			request.getSession().setAttribute("allAliquotIds", allAliquotIds);
			theForm.set("runId", runId);
			
			forward=mapping.findForward("success");	
		}
		catch(Exception e) {
			ActionMessages errors=new ActionMessages();
			ActionMessage error=new ActionMessage("error.preUseAliquot");
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error("Caught exception when loading in aliquot IDs.", e);
			forward=mapping.findForward("failure");
		}
		return forward;
	}
	
	public boolean loginRequired() {
		//temporarily set to false until login module is working
		return false; 
		//return true;
	}
}

