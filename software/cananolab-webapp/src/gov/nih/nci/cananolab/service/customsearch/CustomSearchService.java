package gov.nih.nci.cananolab.service.customsearch;

import gov.nih.nci.cananolab.exception.CustomSearchException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface CustomSearchService {

	public List<CustomSearchBean> customSearchByKeyword(
			HttpServletRequest httpRequest, String keyword) throws CustomSearchException, NoAccessException;

}
