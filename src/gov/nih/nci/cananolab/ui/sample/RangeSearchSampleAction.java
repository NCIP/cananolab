package gov.nih.nci.cananolab.ui.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.28 2008-10-01 18:41:26 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class RangeSearchSampleAction extends AbstractDispatchAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;	
		String[] nanomaterialEntityTypes = new String[0];
		if (theForm != null) {
			nanomaterialEntityTypes = (String[]) theForm
					.get("nanomaterialEntityTypes");
		}
		// convert nanomaterial entity display names into short class names and
		// other types
		List<String> nanomaterialEntityClassNames = new ArrayList<String>();
		List<String> otherNanomaterialEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanomaterialEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getClassName(
					nanomaterialEntityTypes[i], session.getServletContext());
			if (className.length() == 0) {
				className = "OtherNanomaterialEntity";
				otherNanomaterialEntityTypes.add(nanomaterialEntityTypes[i]);
			} else {
				nanomaterialEntityClassNames.add(className);
			}
		}

//		if (samples != null && !samples.isEmpty()) {
//			request.setAttribute("samples", samples);
//			forward = mapping.findForward("success");
//		} else {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage(
//					"message.searchNanoparticle.noresult");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//
//			forward = mapping.getInputForward();
//		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"zAverageUnits", "Z-average", "unit", "otherUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"molecularWeightUnits", "molecular weight", "unit", "otherUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"zetaPotentialUnits", "zeta potential", "unit", "otherUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"lc50Units", "LC50", "unit", "otherUnit", true);
		InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return true;
	}
}
