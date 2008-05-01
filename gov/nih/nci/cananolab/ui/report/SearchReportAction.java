package gov.nih.nci.cananolab.ui.report;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;
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

/**
 * This class searches nanoparticle report based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchReportAction.java,v 1.2 2008-05-01 19:32:21 pansu Exp $ */

public class SearchReportAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportTitle = (String) theForm.get("reportTitle");
		String reportCategory = (String) theForm.get("reportCategory");
		String[] nanoparticleEntityTypes = (String[]) theForm
				.get("nanoparticleEntityTypes");
		// convert nanoparticle entity display names into short class names
		String[] nanoparticleEntityClassNames = new String[nanoparticleEntityTypes.length];
		for (int i = 0; i < nanoparticleEntityTypes.length; i++) {
			nanoparticleEntityClassNames[i] = InitSetup.getInstance()
					.getObjectName(nanoparticleEntityTypes[i],
							session.getServletContext());
		}
		String[] functionalizingEntityTypes = (String[]) theForm
				.get("functionalizingEntityTypes");
		// convert functionalizing entity display names into short class names
		String[] functionalizingEntityClassNames = new String[functionalizingEntityTypes.length];
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			functionalizingEntityClassNames[i] = InitSetup.getInstance()
					.getObjectName(functionalizingEntityTypes[i],
							session.getServletContext());
		}
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		// convert function display names into short class names
		String[] functionClassNames = new String[functionTypes.length];
		for (int i = 0; i < functionTypes.length; i++) {
			functionClassNames[i] = InitSetup.getInstance().getObjectName(
					functionTypes[i], session.getServletContext());
		}

		ReportService service = new ReportService();
		List<ReportBean> reports = service.findReportsBy(reportTitle,
				reportCategory, nanoparticleEntityClassNames,
				functionalizingEntityClassNames, functionClassNames);

		if (reports != null && !reports.isEmpty()) {
			request.getSession().setAttribute("reports", reports);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchReport.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitReportSetup.getInstance().getReportCategories(request);
		InitCompositionSetup.getInstance().getNanoparticleEntityTypes(request);
		InitCompositionSetup.getInstance().getFunctionalizingEntityTypes(
				request);
		InitCompositionSetup.getInstance().getFunctionTypes(request);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}
}
