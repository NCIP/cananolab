package gov.nih.nci.cananolab.restful.customsearch;

import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.customsearch.CustomSearchService;
import gov.nih.nci.cananolab.service.sample.SampleService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("customSearchBO")
public class CustomSearchBO extends BaseAnnotationBO
{
	private Logger logger = Logger.getLogger(SearchSampleBO.class);
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private CurationService curationServiceDAO;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private CustomSearchService customSearchService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public List<CustomSearchBean> search(HttpServletRequest httpRequest, String keyword)
	{
		List<CustomSearchBean> searchResults = new ArrayList<CustomSearchBean>();
		try {
			searchResults = customSearchService.customSearchByKeyword(httpRequest, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResults;
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return sampleService;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

	@Override
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
}
