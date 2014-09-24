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
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.bean.LabelValueBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.util.SampleUtil;
import gov.nih.nci.cananolab.restful.view.SimpleSearchSampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionForward;
//import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;
//import org.apache.struts.validator.DynaValidatorForm;

public class AdvancedSampleSearchBO extends BaseAnnotationBO {

	// Partial URL for viewing detailed sample info from Excel report file.
	public static final String VIEW_SAMPLE_URL = "sample.do?dispatch=summaryView&page=0";

	public List search(HttpServletRequest request, AdvancedSampleSearchBean searchBean)
			throws Exception {
		
		List<SimpleSearchSampleBean> simpleBeans = this.createDummyData();
				
		if (simpleBeans.size() > 0)
			return simpleBeans;
		
		// get the page number from request
		int displayPage = getDisplayPage(request);

		HttpSession session = request.getSession();

		this.setServiceInSession(request);
		searchBean.updateQueries();
		
		// retrieve from session if it's not null
		List<AdvancedSampleBean> sampleBeans = (List<AdvancedSampleBean>) session
				.getAttribute("advancedSampleSearchResults");
		
		if (sampleBeans == null || displayPage <= 0) {
			sampleBeans = querySamples(request, searchBean);
			if (sampleBeans != null && !sampleBeans.isEmpty()) {
				session
						.setAttribute("advancedSampleSearchResults",
								sampleBeans);
			} else {
				List<String> messages = new ArrayList<String>();
				messages.add(PropertyUtil.getProperty("sample", "message.advancedSampleSearch.noresult"));
				return messages;

			}
		}
		// load advancedSampleBean details 25 at a time for displaying
		// pass in page and size
		List<AdvancedSampleBean> sampleBeansPerPage = getSamplesPerPage(
				sampleBeans, displayPage, Constants.DISPLAY_TAG_TABLE_SIZE,
				request, searchBean);
		// set in sessionScope so user can go back to the result from the sample
				// summary page
		request.getSession().setAttribute("advancedSamples", sampleBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		
		// set in sessionScope so user can go back to the result from the sample
				// summary page
		request.getSession().setAttribute("resultSize", Integer
				.valueOf((sampleBeans.size())));

		// save sample result set in session for printing.
		session.setAttribute("samplesResultList", sampleBeansPerPage);
		//return mapping.findForward("success");
		List<SimpleSearchSampleBean> simpleBeanss = transfertoSimpleSampleBeans(sampleBeansPerPage, (UserBean)session.getAttribute("user"));
		
		return simpleBeans;
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
		//DynaValidatorForm theForm = (DynaValidatorForm) form;
		//AdvancedSampleSearchBean searchBean = null;
				
//				(AdvancedSampleSearchBean) theForm
//				.get("searchBean");
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		List<String> sampleIds = service
				.findSampleIdsByAdvancedSearch(searchBean);
		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
		for (String id : sampleIds) {
			AdvancedSampleBean sampleBean = new AdvancedSampleBean(id);
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
				String sampleId = sampleBeans.get(i).getSampleId();
				
				//TODO: check on the query. Don't need much
				
				AdvancedSampleBean loadedAdvancedSample = service
						.findAdvancedSampleByAdvancedSearch(sampleId,
								searchBean);
				loadedSampleBeans.add(loadedAdvancedSample);
			}
		}
		return loadedSampleBeans;
	}

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
		
		//////////////////////////
		//From simple search setup

		InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		request.getSession().removeAttribute("sampleSearchResults");
		
		Map<String, List<String>> listMap = SampleUtil.reformatLocalSearchDropdownsInSession(request.getSession());
		
		Map<String, Object> mixedMap = new HashMap<String, Object>();
		mixedMap.put("functionTypes", listMap.get("functionTypes"));
		mixedMap.put("nanomaterialEntityTypes", listMap.get("nanomaterialEntityTypes"));
		mixedMap.put("functionalizingEntityTypes", listMap.get("functionalizingEntityTypes"));
		mixedMap.put("characterizationTypes", charTypes);
		
		List<String> numberOperands = new ArrayList<String>() ;
		numberOperands.add("=");
		numberOperands.add(">");
		numberOperands.add(">=");
		numberOperands.add("<=");

		mixedMap.put("numberOperands", numberOperands);
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
	protected List<SimpleSearchSampleBean> transfertoSimpleSampleBeans(List<AdvancedSampleBean> sampleBeans,
			UserBean user) {
		List<SimpleSearchSampleBean> simpleBeans = new ArrayList<SimpleSearchSampleBean>();
		
		for (AdvancedSampleBean bean : sampleBeans) {
			
			SimpleSearchSampleBean simpleBean = new SimpleSearchSampleBean();
			simpleBean.transferAdvancedSampleBeanForResultView(bean, user);
			simpleBeans.add(simpleBean);
		}
		
		return simpleBeans;
	}
	
	protected List<SimpleSearchSampleBean> createDummyData() {
		List<SimpleSearchSampleBean> simpleBeans = new ArrayList<SimpleSearchSampleBean>();

		SimpleSearchSampleBean simpleBean = new SimpleSearchSampleBean();
		simpleBean.setSampleId(66846720);
		simpleBean.setSampleName("ncl-14-1-Copy");
		simpleBean.setEditable(true);
		simpleBeans.add(simpleBean);
		
		simpleBean = new SimpleSearchSampleBean();
		simpleBean.setSampleId(20917504);
		simpleBean.setSampleName("NCL-20-1");
		simpleBean.setEditable(true);
		simpleBeans.add(simpleBean);
		
		simpleBean = new SimpleSearchSampleBean();
		simpleBean.setSampleId(20917505);
		simpleBean.setSampleName("NCL-21-1");
		simpleBean.setEditable(false);
		simpleBeans.add(simpleBean);
		
		simpleBean = new SimpleSearchSampleBean();
		simpleBean.setSampleId(20917507);
		simpleBean.setSampleName("NCL-23-1");
		simpleBean.setEditable(false);
		simpleBeans.add(simpleBean);
		
		return simpleBeans;
	}
}
