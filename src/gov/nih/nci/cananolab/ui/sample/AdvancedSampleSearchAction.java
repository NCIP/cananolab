package gov.nih.nci.cananolab.ui.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.28 2008-10-01 18:41:26 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class AdvancedSampleSearchAction extends AbstractDispatchAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) theForm
				.get("searchBean");
		SampleService service = new SampleServiceLocalImpl();
		List<String> sampleNames = service.findSampleNamesByAdvancedSearch(
				searchBean, user);
		// TODO to be implemented
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("advancedSampleSearchForm");
		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return true;
	}
}
