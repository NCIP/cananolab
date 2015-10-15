package gov.nih.nci.cananolab.restful.customsearch;

import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.service.customsearch.CustomSearchService;
import gov.nih.nci.cananolab.service.customsearch.impl.CustomSearchServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CustomSearchBO extends BaseAnnotationBO {
	private Logger logger = Logger.getLogger(SearchSampleBO.class);

	public List<CustomSearchBean> search(HttpServletRequest httpRequest,
			String keyword) {
		List<CustomSearchBean> searchResults = new ArrayList<CustomSearchBean>();
		searchResults = this.customSearchByKeyword(httpRequest, keyword);
		return searchResults;
	}

	private List<CustomSearchBean> customSearchByKeyword(
			HttpServletRequest httpRequest, String keyword) {

		List<CustomSearchBean> searchResults = new ArrayList<CustomSearchBean>();
		CustomSearchService service = this.setServiceInSession(httpRequest);
		try {
			searchResults = service.customSearchByKeyword(httpRequest, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResults;
	}

	private CustomSearchService setServiceInSession(
			HttpServletRequest httpRequest) {
		try{
		SecurityService securityService = super
				.getSecurityServiceFromSession(httpRequest);
		CustomSearchService customSearchService = new CustomSearchServiceLocalImpl(
				securityService);
		httpRequest.getSession().setAttribute("customSearchService",
				customSearchService);
		return customSearchService;
		}catch (SecurityException e) {
			logger.error("Unable to get SecurityServiceFromSession: " + e.getMessage());
			return null;
		}

	}
}
