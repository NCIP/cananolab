package gov.nih.nci.calab.ui.workflow;
/**
 * This class prepares aliquot IDs to be used in the Use Aliquot input form.
 * 
 * @author pansu
 */

/* CVS $Id: LoadAliquotsAction.java,v 1.3 2006-03-08 22:09:12 pansu Exp $ */
import gov.nih.nci.calab.service.workflow.UseAliquotService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class LoadAliquotsAction extends AbstractBaseAction  {
	private static Logger logger=Logger.getLogger(LoadAliquotsAction.class);
	
	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		HttpSession session=request.getSession();
		try {
			/**@todo fill in details for getting aliquot IDs */
			// tmp codes to be replaced.
			DynaValidatorForm loadAliquotsForm = (DynaValidatorForm) form;
			String runId=(String)loadAliquotsForm.get("runId");
			UseAliquotService service=new UseAliquotService();
			List<String> allAliquotIds=service.getAliquots();
			session.setAttribute("allAliquotIds", allAliquotIds);
			session.setAttribute("runId", runId);
			// end of tmp codes
			
			forward=mapping.findForward("success");	
		}
		catch(Exception e) {
			logger.error("Caught exception when loading in aliquot IDs.", e);
			/**@todo fill in details for error handling */
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

