package gov.nih.nci.calab.ui.workflow;
/**
 * This class prepares aliquot IDs to be used in the Use Aliquot input form.
 * 
 * @author pansu
 */

/* CVS $Id: LoadAliquotsAction.java,v 1.2 2006-03-08 19:31:11 pansu Exp $ */
import java.util.ArrayList;
import java.util.List;

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
			// tmp codes to be replaced.
			List allAliquotIds=new ArrayList();
			allAliquotIds.add("NCL-3-1");
			allAliquotIds.add("NCL-3-2");
			allAliquotIds.add("NCL-3-3");
			request.getSession().setAttribute("allAliquotIds", allAliquotIds);
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

