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
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CompositionAction extends BaseAnnotationAction {

	/**
	 * Handle Composition Summary View request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error happened.
	 */
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Call sharing function to prepare CompositionBean for viewing and
		// printing.
		this.prepareSummary(mapping, form, request, response);

		/**
		 * Added by houyh for implementing Print/Export report page.
		 */
		request.setAttribute("actionName", request.getRequestURL().toString());

		return mapping.findForward("summaryView");
	}

	/**
	 * Shared function for summaryView() and summaryPrint(). Prepare
	 * CompositionBean for displaying based in SampleId and location.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error happened.
	 */
	protected void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		if (location==null) {
			location=(String)request.getAttribute("location");
		}
		String sampleId = request.getParameter("sampleId");
		HttpSession session = request.getSession();
		CompositionService compService = null;
		if (location.equals("local")) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			// TODO update grid service
			// compService = new CompositionServiceRemoteImpl(
			// serviceUrl);
		}
		CompositionBean compositionBean = compService
				.findCompositionBySampleId(sampleId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("comp", compositionBean);
		// set entity type and association type and retrieve visibility
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
			compService.retrieveVisibility(entityBean, user);
		}
		for (FunctionalizingEntityBean entityBean : compositionBean
				.getFunctionalizingEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
			compService.retrieveVisibility(entityBean, user);
		}
		for (ChemicalAssociationBean assocBean : compositionBean
				.getChemicalAssociations()) {
			assocBean.setType(InitSetup.getInstance().getDisplayName(
					assocBean.getClassName(), session.getServletContext()));
			compService.retrieveVisibility(assocBean, user);
		}
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
	 *             if error happened.
	 */
	public ActionForward summaryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Call sharing function to prepare CompositionBean for viewing and
		// printing.
		this.prepareSummary(mapping, form, request, response);

		// Marker that indicates page is for printing (do not show tabs, links,
		// etc).
		request.setAttribute("printView", Boolean.TRUE);

		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			CompositionBean compositionBean = (CompositionBean) theForm
					.get("comp");
			if (!type.equals(CompositionBean.CHEMICAL_SELECTION)) {
				compositionBean.setChemicalAssociations(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.FILE_SELECTION)) {
				compositionBean.setFiles(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.FUNCTIONALIZING_SELECTION)) {
				compositionBean
						.setFunctionalizingEntities(Collections.EMPTY_LIST);
			}
			if (!type.equals(CompositionBean.NANOMATERIAL_SELECTION)) {
				compositionBean.setNanomaterialEntities(Collections.EMPTY_LIST);
			}
		}
		return mapping.findForward("summaryPrint");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		prepareSummary(mapping, form, request, response);
		return mapping.findForward("summaryEdit");
	}
}
