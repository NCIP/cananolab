package gov.nih.nci.calab.ui.workflow;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.calab.ui.core.*;

public class LoadAliquotsAction extends AbstractBaseAction  {
	private static Logger logger=Logger.getLogger(LoadAliquotsAction.class);
	
	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		try {
			/**@todo fill in details for getting aliquot IDs */
			forward=mapping.findForward("success");	
		}
		catch(Exception e) {
			logger.error("Caught exception when loading in aliquot IDs.", e);
			/**@todo fill in details for error handling */
			forward=mapping.findForward("failure");
		}
		return forward;
	}
}

