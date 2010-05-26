package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionExporter;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class CompositionAction extends BaseAnnotationAction {

	/**
	 * Handle Composition Summary Edit request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Call shared function to prepare CompositionBean for editing.
		this.prepareSummary(mapping, form, request, response);
		// forward to appropriate tab
		String tab = (String) getValueFromRequest(request, "tab");
		if (tab == null) {
			tab = "ALL"; // default tab to all;
		}
		if (tab.equals("ALL")) {
			request.getSession().removeAttribute("onloadJavascript");
			request.getSession().removeAttribute("tab");
		} else {
			request.getSession().setAttribute(
					"onloadJavascript",
					"showSummary('" + tab + "', "
							+ CompositionBean.ALL_COMPOSITION_SECTIONS.length
							+ ")");
		}
		return mapping.findForward("summaryEdit");
	}

	/**
	 * Handle Composition Summary View request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Call shared function to prepare CompositionBean for viewing.
		this.prepareSummary(mapping, form, request, response);

		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		// forward to appropriate tab
		String tab = (String) getValueFromRequest(request, "tab");
		if (tab == null) {
			tab = "ALL"; // default tab to all;
		}
		if (tab.equals("ALL")) {
			request.getSession().removeAttribute("onloadJavascript");
			request.getSession().removeAttribute("tab");
		} else {
			request.getSession().setAttribute(
					"onloadJavascript",
					"showSummary('" + tab + "', "
							+ compBean.getCompositionSections().size() + ")");
		}
		return mapping.findForward("summaryView");
	}

	/**
	 * Handle Composition Summary Print request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Retrieve compBean from session to avoid re-querying.
		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		if (compBean == null || sampleBean == null) {
			// Call shared function to prepare CompositionBean for viewing.
			this.prepareSummary(mapping, form, request, response);
			compBean = (CompositionBean) request.getSession().getAttribute(
					"compBean");
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
		}
		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		// Show only the selected type.
		this.filterType(request, compBean);

		return mapping.findForward("summaryPrint");
	}

	/**
	 * Handle Composition Summary Export request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Retrieve compBean from session to avoid re-querying.
		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		if (compBean == null || sampleBean == null) {
			// Call shared function to prepare CompositionBean for viewing.
			this.prepareSummary(mapping, form, request, response);
			compBean = (CompositionBean) request.getSession().getAttribute(
					"compBean");
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
		}

		// Export only the selected type.
		this.filterType(request, compBean);

		// Get sample name for constructing file name.
		String type = request.getParameter("type");
		String location = request.getParameter(Constants.LOCATION);
		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain()
				.getName(), "CompositionSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);

		String serviceUrl = null;
		if (!Constants.LOCAL_SITE.equals(location)) {
			serviceUrl = InitSetup.getInstance().getGridServiceUrl(request,
					location);
		}
		StringBuilder sb = getDownloadUrl(request, serviceUrl, location);
		CompositionExporter.exportSummary(compBean, sb.toString(), response
				.getOutputStream());

		return null;
	}

	/**
	 * Shared function for summaryView() and summaryPrint(). Prepare
	 * CompositionBean for displaying based on SampleId and location.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Remove previous result from session.
		HttpSession session = request.getSession();
		session.removeAttribute(CompositionBean.CHEMICAL_SELECTION);
		session.removeAttribute(CompositionBean.FILE_SELECTION);
		session.removeAttribute(CompositionBean.FUNCTIONALIZING_SELECTION);
		session.removeAttribute(CompositionBean.NANOMATERIAL_SELECTION);
		session.removeAttribute("theSample");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = theForm.getString(SampleConstants.SAMPLE_ID);
		String location = theForm.getString(Constants.LOCATION);
		CompositionService service = this.setServicesInSession(request);
		SampleBean sampleBean = setupSample(theForm, request, location);
		/*if (!StringUtils.isEmpty(location)
				&& !Constants.LOCAL_SITE.equals(location)) {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new CompositionServiceRemoteImpl(serviceUrl);
		}*/
		CompositionBean compBean = service.findCompositionBySampleId(sampleId);
		theForm.set("comp", compBean);

		// Save result bean in session for later use - export/print.
		session.setAttribute("compBean", compBean);
		session.setAttribute("theSample", sampleBean); // for showing title.
		if (compBean != null) {
			session.setAttribute(CompositionBean.CHEMICAL_SELECTION, compBean
					.getChemicalAssociations());
			session.setAttribute(CompositionBean.FILE_SELECTION, compBean
					.getFiles());
			session.setAttribute(CompositionBean.FUNCTIONALIZING_SELECTION,
					compBean.getFunctionalizingEntities());
			session.setAttribute(CompositionBean.NANOMATERIAL_SELECTION,
					compBean.getNanomaterialEntities());
		}

		// retain action messages from send redirects
		ActionMessages msgs = (ActionMessages) session
				.getAttribute(ActionMessages.GLOBAL_MESSAGE);
		saveMessages(request, msgs);
		session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
	}

	/**
	 * Shared function for summaryExport() and summaryPrint(). Filter out
	 * unselected types when user selected one type for print/export.
	 *
	 * @param request
	 * @param compBean
	 */
	private void filterType(HttpServletRequest request, CompositionBean compBean) {
		// 1. Restore all data first.
		HttpSession session = request.getSession();
		List<ChemicalAssociationBean> chemBeans = (List<ChemicalAssociationBean>) session
				.getAttribute(CompositionBean.CHEMICAL_SELECTION);
		compBean.setChemicalAssociations(chemBeans);
		List<FileBean> fileBeans = (List<FileBean>) session
				.getAttribute(CompositionBean.FILE_SELECTION);
		compBean.setFiles(fileBeans);
		List<FunctionalizingEntityBean> funcBeans = (List<FunctionalizingEntityBean>) session
				.getAttribute(CompositionBean.FUNCTIONALIZING_SELECTION);
		compBean.setFunctionalizingEntities(funcBeans);
		List<NanomaterialEntityBean> nanoBeans = (List<NanomaterialEntityBean>) session
				.getAttribute(CompositionBean.NANOMATERIAL_SELECTION);
		compBean.setNanomaterialEntities(nanoBeans);

		// 2. Filter out unselected type.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			if (!type.equals(CompositionBean.CHEMICAL_SELECTION)) {
				compBean.setChemicalAssociations(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.FILE_SELECTION)) {
				compBean.setFiles(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.FUNCTIONALIZING_SELECTION)) {
				compBean.setFunctionalizingEntities(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.NANOMATERIAL_SELECTION)) {
				compBean.setNanomaterialEntities(Collections.EMPTY_LIST);
			}
		}
	}

	private CompositionService setServicesInSession(HttpServletRequest request)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		CompositionService compService = new CompositionServiceLocalImpl(user);
		request.getSession().setAttribute("compositionService", compService);
		SampleService sampleService = new SampleServiceLocalImpl(user);
		request.getSession().setAttribute("sampleService", sampleService);
		return compService;
	}
}
