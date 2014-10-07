/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.restful.bean.LabelValueBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.util.SampleUtil;
import gov.nih.nci.cananolab.restful.view.SimpleAdvancedSearchResultView;
import gov.nih.nci.cananolab.restful.view.SimpleAdvancedSearchSampleBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionForward;
//import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;
//import org.apache.struts.validator.DynaValidatorForm;

public class AdvancedSampleSearchBO extends BaseAnnotationBO {
	
	private Logger logger = Logger.getLogger(AdvancedSampleSearchBO.class);

	// Partial URL for viewing detailed sample info from Excel report file.
	public static final String VIEW_SAMPLE_URL = "sample.do?dispatch=summaryView&page=0";

	public SimpleAdvancedSearchResultView search(HttpServletRequest request, AdvancedSampleSearchBean searchBean)
			throws Exception {
		
		HttpSession session = request.getSession();

		this.setServiceInSession(request);
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		
		searchBean.updateQueries();
		
		List<AdvancedSampleBean> sampleBeans = querySamples(request, searchBean);
		
		if (sampleBeans == null || sampleBeans.isEmpty()) {
			SimpleAdvancedSearchResultView empty = new SimpleAdvancedSearchResultView();
			List<String> messages = new ArrayList<String>();
			empty.getErrors().add(PropertyUtil.getProperty("sample", "message.advancedSampleSearch.noresult"));
			return empty;

		}
			
		logger.debug("Got " + sampleBeans.size() + " sample ids from adv. queries");
		
		
		//Load full objects
		List<AdvancedSampleBean> loadedSampleBeans = new ArrayList<AdvancedSampleBean>();
		
		int idx = 0;
		for (AdvancedSampleBean sampleBean : sampleBeans) {

			if (idx == 81) {
				String n = "";
				int i = idx;
			}
			String sampleId = sampleBean.getSampleId();
			AdvancedSampleBean loadedAdvancedSample = service
					.findAdvancedSampleByAdvancedSearch(sampleId,
							searchBean);
			loadedSampleBeans.add(loadedAdvancedSample);
			
			logger.debug("Processin #: " + idx++);

		}
		
		// save sample result set in session for printing.
		//session.setAttribute("samplesResultList", sampleBeansPerPage);
		//return mapping.findForward("success");
		SimpleAdvancedSearchResultView resultView = 
				transfertoSimpleSampleBeans(loadedSampleBeans, (UserBean)session.getAttribute("user"),
						searchBean);
		
		return resultView;
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
	public void export(HttpServletRequest request, AdvancedSampleSearchBean searchBean)
			throws Exception {
		// 1.retrieve search bean from session - form.
		HttpSession session = request.getSession();
//		DynaValidatorForm searchForm = (DynaValidatorForm) session
//				.getAttribute("advancedSampleSearchForm");

//		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
//				.get("searchBean");

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
					String sampleId = sample.getSampleId();
					AdvancedSampleBean loadedAdvancedSample = null;
					loadedAdvancedSample = service
							.findAdvancedSampleByAdvancedSearch(sampleId,
									searchBean);
					samplesFullList.add(loadedAdvancedSample);
				}
				// save full sample result set in session for later use.
				session.setAttribute("samplesFullList", samplesFullList);
			}

			// Export all advanced sample search report.
//			ExportUtils.prepareReponseForExcel(response, getExportFileName());
//			SampleExporter.exportSummary(searchBean, samplesFullList,
//					getViewSampleURL(request), response.getOutputStream());
//			return null;
		} else {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage("error.session.expired",
//					"Session has timed out, please run your search again");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//			return mapping.getInputForward();
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
	public void print(HttpServletRequest request)
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

			//return mapping.findForward("print");
		} else {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage("error.session.expired",
//					"Session has timed out, please run your search again");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//
//			return mapping.getInputForward();
		}
	}

	private List<AdvancedSampleBean> querySamples(HttpServletRequest request, AdvancedSampleSearchBean searchBean) 
			throws Exception {
		
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		
		List<String> sampleNames = service
				.findSampleIdsByAdvancedSearch(searchBean);
		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
		for (String name : sampleNames) {
			AdvancedSampleBean sampleBean = new AdvancedSampleBean(name);
			sampleBeans.add(sampleBean);
		}
		return sampleBeans;
		
//		Map<String, String> sampleIdNames = service.findSampleIdNamesByAdvancedSearch(searchBean);
//		
//		logger.debug("Advanced search returned " + sampleIdNames.size() + " sampleId and name pairs");
//		
//		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
//		
//		Iterator<String> ite = sampleIdNames.keySet().iterator();
//		while (ite.hasNext()) {
//			String id = ite.next();
//			String name = sampleIdNames.get(id);
//			AdvancedSampleBean sampleBean = new AdvancedSampleBean(id);
//			sampleBean.setSampleName(name);
//			
//			//TODO: Need to get access
//			service.loadAccessesForSampleBean(sampleBean);
//			
//			sampleBeans.add(sampleBean);
//		}
		
		//return sampleBeans;
	}

//	private List<AdvancedSampleBean> loadSamples(List<AdvancedSampleBean> sampleBeans, int page, int pageSize,
//			HttpServletRequest request, AdvancedSampleSearchBean searchBean)
//			throws Exception {
//		List<AdvancedSampleBean> loadedSampleBeans = new ArrayList<AdvancedSampleBean>();
//		SampleService service = null;
//		
//		
//		if (request.getSession().getAttribute("sampleService") != null) {
//			service = (SampleService) request.getSession().getAttribute(
//					"sampleService");
//		} else {
//			service = this.setServiceInSession(request);
//		}
//		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
//			if (i < sampleBeans.size()) {
//				String sampleId = sampleBeans.get(i).getSampleId();
//				
//				//TODO: check on the query. Don't need much
//				
//				AdvancedSampleBean loadedAdvancedSample = service
//						.findAdvancedSampleByAdvancedSearch(sampleId,
//								searchBean, true);
//				loadedSampleBeans.add(loadedAdvancedSample);
//			}
//		}
//		return loadedSampleBeans;
//	}

	public void validateSetup(HttpServletRequest request)
			throws Exception {
		// clear the search results and start over
		request.getSession().removeAttribute("advancedSampleSearchResults");
		request.getSession().removeAttribute("samplesResultList");
		request.getSession().removeAttribute("samplesFullList");
		request
				.setAttribute(
						"onloadJavascript",
						"displaySampleQueries(); displayCompositionQueries(); displayCharacterizationQueries()");
		//return mapping.findForward("inputForm");
	}

//	public void setup(HttpServletRequest request)
//			throws Exception {
//		
//		request.getSession().removeAttribute("advancedSampleSearchForm");
//		InitCharacterizationSetup.getInstance()
//				.getDecoratedCharacterizationTypes(request);
//		request.getSession().removeAttribute("advancedSampleSearchResults");
//		request.getSession().removeAttribute("samplesResultList");
//		request.getSession().removeAttribute("samplesFullList");
//		//return mapping.getInputForward();
//	}
	
	public Map<String, Object> setup(HttpServletRequest request)
			throws Exception {
		request.getSession().removeAttribute("advancedSampleSearchForm");
		
		request.getSession().removeAttribute("advancedSampleSearchResults");
		request.getSession().removeAttribute("samplesResultList");
		request.getSession().removeAttribute("samplesFullList");
		
		List<LabelValueBean> charTypes = InitCharacterizationSetup.getInstance()
				.getDecoratedCharacterizationTypes(request);
		
		List<String> nanomaterialTypes = InitSampleSetup.getInstance().getNanomaterialEntityTypes(request);
		List<String> fetypes = InitSampleSetup.getInstance().getFunctionalizingEntityTypes(request);
		List<String> functionTypes = InitSampleSetup.getInstance().getFunctionTypes(request);
		
		request.getSession().removeAttribute("sampleSearchResults");
		
		Map<String, Object> mixedMap = new HashMap<String, Object>(); 
		mixedMap.put("functionTypes", functionTypes);
		mixedMap.put("nanomaterialEntityTypes", nanomaterialTypes);
		mixedMap.put("functionalizingEntityTypes", fetypes);
		mixedMap.put("characterizationTypes", charTypes);
		
		return mixedMap;
		
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
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);

		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
	}
	
	/**
	 * 
	 */
	protected SimpleAdvancedSearchResultView transfertoSimpleSampleBeans(List<AdvancedSampleBean> sampleBeans,
			UserBean user, AdvancedSampleSearchBean searchBean) {
		
		SimpleAdvancedSearchResultView resultView = new SimpleAdvancedSearchResultView();
		resultView.createColumnTitles(searchBean.getQueryAsColumnNames());
	
		List<SimpleAdvancedSearchSampleBean> simpleBeans = new ArrayList<SimpleAdvancedSearchSampleBean>();
		
		for (AdvancedSampleBean bean : sampleBeans) {
			
			SimpleAdvancedSearchSampleBean simpleBean = new SimpleAdvancedSearchSampleBean();
			simpleBean.transferAdvancedSampleBeanForResultView(bean, user, searchBean, resultView.getColumnTitles());
			simpleBeans.add(simpleBean);
		}
		
		resultView.setSamples(simpleBeans);
		resultView.transformToTableView();
		return resultView;
	}
	
}
