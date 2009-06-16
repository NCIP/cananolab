package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
		this.prepareSummary(mapping, form, request, response);

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
		String location = request.getParameter("location");
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
		String fileName = this.getExportFileName(compBean.getDomain()
				.getSample().getName(), "CompositionSummaryView");
		ExportUtils.prepareReponseForExcell(response, fileName);
		CompositionService service = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			service = new CompositionServiceLocalImpl();
		} else {
			// TODO: Implement remote service.
		}
		service.exportSummary(compBean, request, response.getOutputStream());

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
		String sampleId = theForm.getString("sampleId");
		String location = theForm.getString("location");
		HttpSession session = request.getSession();
		CompositionService compService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			// TODO update grid service
			// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
			// request, location);
			// compService = new CompositionServiceRemoteImpl(
			// serviceUrl);
		}
		CompositionBean compBean = compService.findCompositionBySampleId(
				sampleId, user);
		theForm.set("comp", compBean);
		// set entity type and association type and retrieve visibility

		for (NanomaterialEntityBean entityBean : compBean
				.getNanomaterialEntities()) {
			entityBean.updateType(InitSetup.getInstance()
					.getClassNameToDisplayNameLookup(
							session.getServletContext()));
		}
		for (FunctionalizingEntityBean entityBean : compBean
				.getFunctionalizingEntities()) {
			entityBean.updateType(InitSetup.getInstance()
					.getClassNameToDisplayNameLookup(
							session.getServletContext()));
		}
		for (ChemicalAssociationBean assocBean : compBean
				.getChemicalAssociations()) {
			assocBean.updateType(InitSetup.getInstance()
					.getClassNameToDisplayNameLookup(
							session.getServletContext()));
		}

		// retain action messages from send redirects
		ActionMessages msgs = (ActionMessages) session
				.getAttribute(ActionMessages.GLOBAL_MESSAGE);
		saveMessages(request, msgs);
		session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
	}

	/**
	 * Get file name for exporting report as an Excell file.
	 *
	 * @param sampleName
	 * @param viewType
	 * @param charClass
	 * @return
	 */
	private String getExportFileName(String sampleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(sampleName);
		nameParts.add(viewType);
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
}
