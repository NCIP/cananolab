package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user entered new aliquot information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateAliquotAction.java,v 1.2 2006-07-05 21:22:59 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.inventory.ManageAliquotService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
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

public class CreateAliquotAction extends AbstractBaseAction {
	
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();		
		
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		boolean fromAliquot = ((String) theForm.get("fromAliquot")).equals("true") ? true
				: false;
		String sampleName = (String) theForm.get("sampleName");
		String containerName=sampleName+(String)theForm.get("containerName");
		String parentAliquotName = (String) theForm.get("parentAliquotName");
		String parentName=(fromAliquot)?parentAliquotName:containerName;
		String fullParentName = (!fromAliquot) ? "Sample Container "
				+ containerName : "Aliquot " + parentAliquotName;
		request.setAttribute("fullParentName", fullParentName);
		if (session.getAttribute("aliquotMatrix") != null) {
			List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>(
					(List<? extends AliquotBean[]>) session
							.getAttribute("aliquotMatrix"));
			ManageAliquotService manageAliquotService = new ManageAliquotService();
			manageAliquotService.saveAliquots(fromAliquot, parentName,
					aliquotMatrix);
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("message.createAliquot");
			msgs.add("message", msg);
			saveMessages(request, msgs);

			// set a flag to indicate that new aliquots have been created so
			// session can
			// be refreshed in initSession.do
			session.setAttribute("newAliquotCreated", "yes");

			forward = mapping.findForward("success");
		} else {
			throw new CalabException("Can't find the aliquot matrix to save.  Please click on 'Update Aliquots' button before submitting");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
