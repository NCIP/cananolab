/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionExporter;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
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

public class CompositionBO extends BaseAnnotationBO {

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
	public CompositionBean summaryEdit(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
		// Call shared function to prepare CompositionBean for editing.
		this.prepareSummary(form, request);
		setSummaryTab(request, CompositionBean.ALL_COMPOSITION_SECTIONS.length);
	//	return mapping.findForward("summaryEdit");
		return null;
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
	public CompositionBean summaryView(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
		// Call shared function to prepare CompositionBean for viewing.
		this.prepareSummary(form, request);

		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		setSummaryTab(request, compBean.getCompositionSections().size());
		//return mapping.findForward("summaryView");
		return compBean;
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
	public CompositionBean summaryPrint(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Retrieve compBean from session to avoid re-querying.
		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		if (compBean == null || sampleBean == null) {
			// Call shared function to prepare CompositionBean for viewing.
			this.prepareSummary(form, request);
			compBean = (CompositionBean) request.getSession().getAttribute(
					"compBean");
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
		}
		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		// Show only the selected type.
		this.filterType(request, compBean);

	//	return mapping.findForward("summaryPrint");
		return compBean;
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
	public String summaryExport(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Retrieve compBean from session to avoid re-querying.
		CompositionBean compBean = (CompositionBean) request.getSession()
				.getAttribute("compBean");
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		if (compBean == null || sampleBean == null) {
			// Call shared function to prepare CompositionBean for viewing.
			this.prepareSummary( form, request);
			compBean = (CompositionBean) request.getSession().getAttribute(
					"compBean");
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
		}

		// Export only the selected type.
		this.filterType(request, compBean);

		// Get sample name for constructing file name.
		String type = request.getParameter("type");
		
		//per app scan
		if (!StringUtils.xssValidate(type)) {
			type="";
		}
		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain()
				.getName(), "CompositionSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);

		StringBuilder sb = getDownloadUrl(request);
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
	private void prepareSummary(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
		// Remove previous result from session.
		HttpSession session = request.getSession();
		session.removeAttribute(CompositionBean.CHEMICAL_SELECTION);
		session.removeAttribute(CompositionBean.FILE_SELECTION);
		session.removeAttribute(CompositionBean.FUNCTIONALIZING_SELECTION);
		session.removeAttribute(CompositionBean.NANOMATERIAL_SELECTION);
		session.removeAttribute("theSample");

	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = form.getSampleId();  //Sting(SampleConstants.SAMPLE_ID);
		CompositionService service = this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(sampleId, request);

		CompositionBean compBean = service.findCompositionBySampleId(sampleId);
		form.setComp(compBean);

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
	//	ActionMessages msgs = (ActionMessages) session
	//			.getAttribute(ActionMessages.GLOBAL_MESSAGE);
	//	saveMessages(request, msgs);
	//	session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
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
			if( !type.equals("all") ) {
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

	private CompositionService setServicesInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);

		CompositionService compService = new CompositionServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("compositionService", compService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return compService;
	}

	public String download(String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	//	CompositionService service = setServicesInSession(request);
		BaseService service = setServicesInSession(request);
		return downloadFile(service, fileId, request, response);
	}
}
