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
	 * Handle Composition Summary Edit request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception if error occurred.
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
	 * @throws Exception if error occurred.
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
			CompositionBean compositionBean = 
				(CompositionBean) theForm.get("comp");
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
	protected void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		if (location == null) {
			location = (String)request.getAttribute("location");
		}
		String sampleId = request.getParameter("sampleId");
		HttpSession session = request.getSession();
		CompositionService compService = null;
		if (Constants.LOCAL.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			// TODO update grid service
			// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
			//		request, location);
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
			assocBean.updateType(InitSetup.getInstance()
					.getClassNameToDisplayNameLookup(
							session.getServletContext()));
			compService.retrieveVisibility(assocBean, user);
		}
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
		return mapping.findForward("summaryEdit");
	}
}
