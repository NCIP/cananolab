package gov.nih.nci.calab.ui.administration;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.calab.ui.core.*;

public class CreateSampleAction extends AbstractBaseAction  {
	private static Logger logger=Logger.getLogger(CreateSampleAction.class);
	
	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		try {
			/**@todo fill in details for sample information */
			forward=mapping.findForward("success");	
		}
		catch(Exception e) {
			logger.error("", e);
			forward=mapping.getInputForward();
		}
		return forward;
	}
}
