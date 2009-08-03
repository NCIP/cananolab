package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		prepareSummary(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

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
		// Call sharing function to prepare CompositionBean for viewing and
		// printing.
		this.prepareSummary(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

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
		// Prepare CompositionBean for viewing and printing.
		this.prepareSummary(mapping, form, request, response);

		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		// Show only the selected type.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			CompositionBean compBean = (CompositionBean) theForm.get("comp");
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

		// Prepare CompositionBean for viewing and printing.
		this.prepareSummary(mapping, form, request, response);

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean compBean = (CompositionBean) theForm.get("comp");

		// Show only the selected type.
		String type = request.getParameter("type");
		String location = request.getParameter(Constants.LOCATION);
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

		// Get sample name for constructing file name.
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
		setupSample(theForm, request, location);
		HttpSession session = request.getSession();
		CompositionService compService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			compService = new CompositionServiceRemoteImpl(serviceUrl);
		}
		CompositionBean compBean = compService.findCompositionBySampleId(
				sampleId, user);
		if (compBean != null) {
			theForm.set("comp", compBean);
		}
		// retain action messages from send redirects
		ActionMessages msgs = (ActionMessages) session
				.getAttribute(ActionMessages.GLOBAL_MESSAGE);
		saveMessages(request, msgs);
		session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
		if (request.getParameter("clearTab") != null
				&& request.getParameter("clearTab").equals("true")) {
			request.getSession().removeAttribute("onloadJavascript");
		}
	}

}
