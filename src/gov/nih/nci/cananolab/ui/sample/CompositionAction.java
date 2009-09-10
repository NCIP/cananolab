package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceRemoteImpl;
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

import org.apache.commons.lang.ClassUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class CompositionAction extends BaseAnnotationAction {

	// Partial URL for downloading composition report file.
	public static final String DOWNLOAD_URL = "?dispatch=download&location=";

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
		// As form bean is sessional, retrieve it from session to avoid re-querying. 
		DynaValidatorForm theForm = 
			(DynaValidatorForm) request.getSession().getAttribute("compositionForm");
		if (theForm == null) {
			this.prepareSummary(mapping, form, request, response);
			theForm = (DynaValidatorForm) form;
		}
		CompositionBean compBean = (CompositionBean) theForm.get("comp");

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
		// As form bean is sessional, retrieve it from session to avoid re-querying. 
		DynaValidatorForm theForm = 
			(DynaValidatorForm) request.getSession().getAttribute("compositionForm");
		if (theForm == null) {
			this.prepareSummary(mapping, form, request, response);
			theForm = (DynaValidatorForm) form;
		}
		CompositionBean compBean = (CompositionBean) theForm.get("comp");
		if (compBean != null) {
			// Export only the selected type.
			this.filterType(request, compBean);
			
			// Get sample name for constructing file name.
			String type = request.getParameter("type");
			String location = request.getParameter(Constants.LOCATION);
			String fileName = ExportUtils.getExportFileName(compBean.getDomain()
					.getSample().getName(), "CompositionSummaryView", type);
			ExportUtils.prepareReponseForExcell(response, fileName);

			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURL().toString());
			sb.append(DOWNLOAD_URL);
			sb.append(request.getParameter(location));

			CompositionServiceHelper.exportSummary(compBean, sb.toString(),
					response.getOutputStream());
			return null;
		} else {
			return mapping.getInputForward();
		}
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = theForm.getString(SampleConstants.SAMPLE_ID);
		String location = theForm.getString(Constants.LOCATION);
		SampleBean sampleBean = setupSample(theForm, request, location);
		HttpSession session = request.getSession();
		CompositionService compService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			compService = new CompositionServiceRemoteImpl(serviceUrl);
		}
		// TODO remove this
//		compService = new CompositionServiceRemoteImpl(
//				"http://NCI-01738843.nci.nih.gov:8080/wsrf-canano/services/cagrid/CaNanoLabService");

		CompositionBean compBean = compService.findCompositionBySampleId(
				sampleId, user);
		theForm.set("comp", compBean);
		
		// Save result set in session for later use - export/print. 
		if (compBean != null) {
			session.setAttribute(CompositionBean.CHEMICAL_SELECTION, 
					compBean.getChemicalAssociations());
			session.setAttribute(CompositionBean.FILE_SELECTION, 
					compBean.getFiles());
			session.setAttribute(CompositionBean.FUNCTIONALIZING_SELECTION, 
					compBean.getFunctionalizingEntities());
			session.setAttribute(CompositionBean.NANOMATERIAL_SELECTION, 
					compBean.getNanomaterialEntities());
			session.setAttribute("theSample", sampleBean); //for showing title.
		} else {
			session.removeAttribute(CompositionBean.CHEMICAL_SELECTION);
			session.removeAttribute(CompositionBean.FILE_SELECTION);
			session.removeAttribute(CompositionBean.FUNCTIONALIZING_SELECTION);
			session.removeAttribute(CompositionBean.NANOMATERIAL_SELECTION);
			session.removeAttribute("theSample");
		}
		
		// retain action messages from send redirects
		ActionMessages msgs = (ActionMessages) session
				.getAttribute(ActionMessages.GLOBAL_MESSAGE);
		saveMessages(request, msgs);
		session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
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
	}
	
	/**
	 * Shared function for summaryExport() and summaryPrint().
	 * Filter out unselected types when user selected one type for print/export. 
	 * 
	 * @param request
	 * @param compBean
	 */
	private void filterType(HttpServletRequest request, CompositionBean compBean) {
		HttpSession session = request.getSession();
		String type = request.getParameter("type");
		if (compBean != null) {
			List<ChemicalAssociationBean> chemBeans = (List<ChemicalAssociationBean>) 
				session.getAttribute(CompositionBean.CHEMICAL_SELECTION);
			compBean.setChemicalAssociations(chemBeans);
			List<FileBean> fileBeans = (List<FileBean>) 
				session.getAttribute(CompositionBean.FILE_SELECTION);
			compBean.setFiles(fileBeans);
			List<FunctionalizingEntityBean> funcBeans = (List<FunctionalizingEntityBean>) 
				session.getAttribute(CompositionBean.FUNCTIONALIZING_SELECTION);
			compBean.setFunctionalizingEntities(funcBeans);
			List<NanomaterialEntityBean> nanoBeans = (List<NanomaterialEntityBean>) 
				session.getAttribute(CompositionBean.NANOMATERIAL_SELECTION);
			compBean.setNanomaterialEntities(nanoBeans);
		}
		if (!StringUtils.isEmpty(type) && compBean != null) {
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
}
