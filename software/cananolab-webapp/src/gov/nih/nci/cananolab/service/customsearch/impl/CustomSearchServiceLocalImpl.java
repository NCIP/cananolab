package gov.nih.nci.cananolab.service.customsearch.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.exception.CustomSearchException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.customsearch.CustomSearchService;
import gov.nih.nci.cananolab.service.customsearch.helper.CustomSearchServiceHelper;
import gov.nih.nci.cananolab.service.favorites.helper.FavoritesServiceHelper;
import gov.nih.nci.cananolab.service.favorites.impl.FavoritesServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

public class CustomSearchServiceLocalImpl extends BaseServiceLocalImpl implements CustomSearchService{

	private static Logger logger = Logger
			.getLogger(FavoritesServiceLocalImpl.class);
	private CustomSearchServiceHelper helper;

	public CustomSearchServiceLocalImpl() {
		super();
		helper = new CustomSearchServiceHelper(this.securityService);
	}

	public CustomSearchServiceLocalImpl(UserBean user) {
		super(user);
		helper = new CustomSearchServiceHelper(this.securityService);
	}

	public CustomSearchServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new CustomSearchServiceHelper(this.securityService);
	}
	@Override
	public List<CustomSearchBean> customSearchByKeyword(
			HttpServletRequest httpRequest, String keyword) throws CustomSearchException, NoAccessException {
		List<CustomSearchBean> results = new ArrayList<CustomSearchBean>();
		List<CustomSearchBean> protocolResults = helper.customSearchByKeywordByProtocol(httpRequest, keyword);
		for(int i = 0; i < protocolResults.size(); i++){
			if(protocolResults.get(i).getName()!=null)
				results.add(protocolResults.get(i));
		}
		List<CustomSearchBean> sampleResults = helper.customSearchByKeywordBySample(httpRequest, keyword);
		for(int i = 0; i < sampleResults.size(); i++){
			if(sampleResults.get(i).getName()!=null)
				results.add(sampleResults.get(i));
		}
		List<CustomSearchBean> pubResults = helper.customSearchByKeywordByPub(httpRequest, keyword);
		for(int i = 0; i < pubResults.size(); i++){
			if(pubResults.get(i).getName()!=null)
				results.add(pubResults.get(i));
		}
		return results;
	}

}
