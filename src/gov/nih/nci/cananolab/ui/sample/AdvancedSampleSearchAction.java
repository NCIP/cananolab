package gov.nih.nci.cananolab.ui.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.28 2008-10-01 18:41:26 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleExporter;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
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

public class AdvancedSampleSearchAction extends BaseAnnotationAction {

	// Partial URL for viewing detailed sample info from Excel report file.
	public static final String VIEW_SAMPLE_URL = "sample.do?dispatch=summaryView&page=0";

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// get the page number from request
		int displayPage = getDisplayPage(request);

		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) theForm
				.get("searchBean");
		this.setServiceInSession(request);
		searchBean.updateQueries();
		// retrieve from session if it's not null
		List<AdvancedSampleBean> sampleBeans = (List<AdvancedSampleBean>) session
				.getAttribute("advancedSampleSearchResults");
		if (sampleBeans == null || displayPage <= 0) {
			sampleBeans = querySamples(form, request);
			if (sampleBeans != null && !sampleBeans.isEmpty()) {
				session
						.setAttribute("advancedSampleSearchResults",
								sampleBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.advancedSampleSearch.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		// load advancedSampleBean details 25 at a time for displaying
		// pass in page and size
		List<AdvancedSampleBean> sampleBeansPerPage = getSamplesPerPage(
				sampleBeans, displayPage, Constants.DISPLAY_TAG_TABLE_SIZE,
				request, searchBean);
		request.setAttribute("advancedSamples", sampleBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", Integer
				.valueOf((sampleBeans.size())));

		// save sample result set in session for printing.
		session.setAttribute("samplesResultList", sampleBeansPerPage);

		return mapping.findForward("success");
	}

	/**
	 * Export full advance sample search report in excel file.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward export(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 1.retrieve search bean from session - form.
		HttpSession session = request.getSession();
		DynaValidatorForm searchForm = (DynaValidatorForm) session
				.getAttribute("advancedSampleSearchForm");

		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");

		// 2.retrieve full list of sample name from session.
		List<AdvancedSampleBean> sampleSearchResult = (List<AdvancedSampleBean>) session
				.getAttribute("advancedSampleSearchResults");

		if (searchBean != null && sampleSearchResult != null
				&& !sampleSearchResult.isEmpty()) {
			// Load all samples by name & location if they are not loaded
			// previously.
			List<AdvancedSampleBean> samplesFullList = (List<AdvancedSampleBean>) session
					.getAttribute("samplesFullList");
			if (samplesFullList == null) {
				samplesFullList = new ArrayList<AdvancedSampleBean>(
						sampleSearchResult.size());
				SampleService service = this.setServiceInSession(request);
				for (AdvancedSampleBean sample : sampleSearchResult) {
					String location = sample.getLocation();
					String sampleName = sample.getSampleName();
					AdvancedSampleBean loadedAdvancedSample = null;
					if (!StringUtils.isEmpty(location)
							&& !Constants.LOCAL_SITE.equals(location)) {
						String serviceUrl = InitSetup.getInstance()
								.getGridServiceUrl(request, location);
						service = new SampleServiceRemoteImpl(serviceUrl);
					}
					loadedAdvancedSample = service
							.findAdvancedSampleByAdvancedSearch(sampleName,
									searchBean);
					loadedAdvancedSample.setLocation(location);
					samplesFullList.add(loadedAdvancedSample);
				}
				// save full sample result set in session for later use.
				session.setAttribute("samplesFullList", samplesFullList);
			}

			// Export all advanced sample search report.
			ExportUtils.prepareReponseForExcel(response, getExportFileName());
			SampleExporter.exportSummary(searchBean, samplesFullList,
					getViewSampleURL(request), response.getOutputStream());
			return null;
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.session.expired",
					"Session has timed out, please run your search again");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
	}

	/**
	 * Print current page advance sample search report.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward print(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// 1.Get total sample list from session for total result size.
		List<AdvancedSampleBean> sampleTotalList = (List<AdvancedSampleBean>) session
				.getAttribute("advancedSampleSearchResults");

		// 2.Get current page sample list from session for display tab.
		List<AdvancedSampleBean> sampleResultList = (List<AdvancedSampleBean>) session
				.getAttribute("samplesResultList");

		if (sampleTotalList != null && sampleResultList != null) {
			request.setAttribute("resultSize", Integer.valueOf((sampleTotalList
					.size())));
			request.setAttribute("advancedSamples", sampleResultList);
			request.setAttribute("printView", Boolean.TRUE);

			return mapping.findForward("print");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.session.expired",
					"Session has timed out, please run your search again");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);

			return mapping.getInputForward();
		}
	}

	private List<AdvancedSampleBean> querySamples(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) theForm
				.get("searchBean");
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		List<String> sampleNames = service
				.findSampleNamesByAdvancedSearch(searchBean);
		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
		for (String name : sampleNames) {
			AdvancedSampleBean sampleBean = new AdvancedSampleBean(name,
					Constants.APP_OWNER);
			sampleBeans.add(sampleBean);
		}
		return sampleBeans;
	}

	private List<AdvancedSampleBean> getSamplesPerPage(
			List<AdvancedSampleBean> sampleBeans, int page, int pageSize,
			HttpServletRequest request, AdvancedSampleSearchBean searchBean)
			throws Exception {
		List<AdvancedSampleBean> loadedSampleBeans = new ArrayList<AdvancedSampleBean>();
		SampleService service = null;
		if (request.getSession().getAttribute("sampleService") != null) {
			service = (SampleService) request.getSession().getAttribute(
					"sampleService");
		} else {
			service = this.setServiceInSession(request);
		}
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < sampleBeans.size()) {
				String location = sampleBeans.get(i).getLocation();
				String sampleName = sampleBeans.get(i).getSampleName();
				if (!StringUtils.isEmpty(location)
						&& !location.equals(Constants.LOCAL_SITE)) {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);
					service = new SampleServiceRemoteImpl(serviceUrl);
				}
				AdvancedSampleBean loadedAdvancedSample = service
						.findAdvancedSampleByAdvancedSearch(sampleName,
								searchBean);
				loadedAdvancedSample.setLocation(location);
				loadedSampleBeans.add(loadedAdvancedSample);
			}
		}
		return loadedSampleBeans;
	}

	public ActionForward validateSetup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// clear the search results and start over
		request.getSession().removeAttribute("advancedSampleSearchResults");
		request.getSession().removeAttribute("samplesResultList");
		request.getSession().removeAttribute("samplesFullList");
		request
				.setAttribute(
						"onloadJavascript",
						"displaySampleQueries(); displayCompositionQueries(); displayCharacterizationQueries()");
		return mapping.findForward("inputForm");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("advancedSampleSearchForm");
		InitCharacterizationSetup.getInstance()
				.getDecoratedCharacterizationTypes(request);
		request.getSession().removeAttribute("advancedSampleSearchResults");
		request.getSession().removeAttribute("samplesResultList");
		request.getSession().removeAttribute("samplesFullList");
		return mapping.getInputForward();
	}

	/**
	 * Get file name for exporting report as an Excel file.
	 *
	 * @param sampleName
	 * @param viewType
	 * @param subType
	 * @return
	 */
	private static String getExportFileName() {
		StringBuilder sb = new StringBuilder("Advanced_Sample_Search_Report_");
		sb.append(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		return sb.toString();
	}

	/**
	 * Get view sample URL.
	 *
	 * @param sample
	 * @return
	 */
	private static String getViewSampleURL(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String requestUrl = request.getRequestURL().toString();
		int index = requestUrl.lastIndexOf("/");
		sb.append(requestUrl.substring(0, index + 1));
		sb.append(VIEW_SAMPLE_URL);

		return sb.toString();
	}

	private SampleService setServiceInSession(HttpServletRequest request)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleService sampleService = new SampleServiceLocalImpl(user);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
	}
}
