package gov.nih.nci.cananolab.service.customsearch.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.exception.CustomSearchException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.customsearch.CustomSearchService;
import gov.nih.nci.cananolab.service.customsearch.helper.CustomSearchServiceHelper;

@Component("customSearchService")
public class CustomSearchServiceLocalImpl extends BaseServiceLocalImpl implements CustomSearchService
{
	private static Logger logger = Logger.getLogger(CustomSearchServiceLocalImpl.class);
	
	@Autowired
	private CustomSearchServiceHelper customSearchServiceHelper;
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Override
	public List<CustomSearchBean> customSearchByKeyword(HttpServletRequest httpRequest, String keyword) throws CustomSearchException, NoAccessException
	{
		List<CustomSearchBean> results = new ArrayList<CustomSearchBean>();
		List<CustomSearchBean> protocolResults = customSearchServiceHelper.customSearchByKeywordByProtocol(httpRequest, keyword);
		for(int i = 0; i < protocolResults.size(); i++){
			if(protocolResults.get(i).getName()!=null)
				results.add(protocolResults.get(i));
		}
		List<CustomSearchBean> sampleResults = customSearchServiceHelper.customSearchByKeywordBySample(httpRequest, keyword);
		for(int i = 0; i < sampleResults.size(); i++){
			if(sampleResults.get(i).getName()!=null)
				results.add(sampleResults.get(i));
		}
		List<CustomSearchBean> pubResults = customSearchServiceHelper.customSearchByKeywordByPub(httpRequest, keyword);
		for(int i = 0; i < pubResults.size(); i++){
			if(pubResults.get(i).getName()!=null)
				results.add(pubResults.get(i));
		}
		return results;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

}
