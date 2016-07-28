package gov.nih.nci.cananolab.restful.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.util.ProtocolUtil;
import gov.nih.nci.cananolab.restful.view.SimpleSearchProtocolBean;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.ui.form.SearchProtocolForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("searchProtocolBO")
public class SearchProtocolBO extends BaseAnnotationBO
{
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private ProtocolService protocolService;
	
	@Autowired
	private CurationService curationServiceDAO;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public List search(SearchProtocolForm form, HttpServletRequest request) throws Exception
	{
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);
		List<String> msgs = new ArrayList<String>();
		List<ProtocolBean> protocolBeans = null;
		// retrieve from session if it's not null and not the first page
		if (session.getAttribute("protocolSearchResults") != null && displayPage > 0) {
			protocolBeans = new ArrayList<ProtocolBean>((List) session.getAttribute("protocolSearchResults"));
		} else {
			protocolBeans = queryProtocols(form, request);
			if (protocolBeans != null && !protocolBeans.isEmpty()) {
				session.setAttribute("protocolSearchResults", protocolBeans);
			} else {
				msgs.add(PropertyUtil.getProperty("protocol", "message.searchProtocol.noresult"));
				return msgs;
			}
		}
		// display 25 protocols at a time
		List<ProtocolBean> protocolBeansPerPage = getProtocolsPerPage(protocolBeans, displayPage, Constants.DISPLAY_TAG_TABLE_SIZE,
				request);

		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		if (userDetails != null) {
			loadUserAccess(request, protocolBeansPerPage);
		}
		// set in sessionScope so user can go back to the result from the sample
		// summary page
		request.getSession().setAttribute("protocols", protocolBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		// set in sessionScope so user can go back to the result from the sample
		// summary page
		request.getSession().setAttribute("resultSize", new Integer(protocolBeans.size()));

		//return protocolBeansPerPage;
		List<SimpleSearchProtocolBean> simpleProtocolBeans = transfertoSimpleProtocolBeans(protocolBeansPerPage);
		return simpleProtocolBeans;
	}

	protected List<SimpleSearchProtocolBean> transfertoSimpleProtocolBeans(List<ProtocolBean> protocolBeansPerPage)
	{
		List<SimpleSearchProtocolBean> simpleBeans = new ArrayList<SimpleSearchProtocolBean>();

		for (ProtocolBean bean : protocolBeansPerPage) {
			SimpleSearchProtocolBean simpleBean = new SimpleSearchProtocolBean();
			simpleBean.transferProtocolBeanForBasicResultView(bean);
			simpleBeans.add(simpleBean);
		}

		return simpleBeans;
	}

	private List<ProtocolBean> getProtocolsPerPage(List<ProtocolBean> protocolBeans, int page, int pageSize, HttpServletRequest request) throws Exception
	{
		List<ProtocolBean> protocolsPerPage = new ArrayList<ProtocolBean>();
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++)
		{
			if (i < protocolBeans.size()) {
				ProtocolBean protocolBean = protocolBeans.get(i);
				if (protocolBean != null) {
					protocolsPerPage.add(protocolBean);
				}
			}
		}
		return protocolsPerPage;
	}

	private List<ProtocolBean> queryProtocols(SearchProtocolForm form, HttpServletRequest request) throws Exception
	{
		//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileTitle = form.getFileTitle();
		// strip wildcards if entered by user
		fileTitle = StringUtils.stripWildcards(fileTitle);
		String titleOperand = form.getTitleOperand();
		if (titleOperand.equals(Constants.STRING_OPERAND_CONTAINS) && !StringUtils.isEmpty(fileTitle)) {
			fileTitle = "*" + fileTitle + "*";
		}
		String protocolType = form.getProtocolType();
		String protocolName = form.getProtocolName();
		// strip wildcards if entered by user
		protocolName = StringUtils.stripWildcards(protocolName);

		String nameOperand = form.getNameOperand();
		if (nameOperand.equals(Constants.STRING_OPERAND_CONTAINS) && !StringUtils.isEmpty(protocolName)) {
			protocolName = "*" + protocolName + "*";
		}
		String protocolAbbreviation = form.getProtocolAbbreviation();
		// strip wildcards if entered by user
		protocolAbbreviation = StringUtils.stripWildcards(protocolAbbreviation);

		String abbreviationOperand = form.getAbbreviationOperand();
		if (abbreviationOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(protocolAbbreviation)) {
			protocolAbbreviation = "*" + protocolAbbreviation + "*";
		}

		List<ProtocolBean> allProtocols = protocolService.findProtocolsBy(protocolType, protocolName, protocolAbbreviation, fileTitle);
		return allProtocols;
	}

	private void loadUserAccess(HttpServletRequest request, List<ProtocolBean> protocolBeans) throws Exception
	{
		List<String> protocolIds = new ArrayList<String>();
		for (ProtocolBean protocolBean : protocolBeans) {
			protocolIds.add(protocolBean.getDomain().getId().toString());
		}
		for (ProtocolBean protocolBean : protocolBeans) {
			boolean isWriteable = springSecurityAclService.currentUserHasWritePermission(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz());
			boolean isDeleteable = springSecurityAclService.currentUserHasDeletePermission(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz());
			protocolBean.setUserUpdatable(isWriteable);
			protocolBean.setUserDeletable(isDeleteable);
		}
	}

	public Map<String, Object> setup(HttpServletRequest request) throws Exception
	{
		InitProtocolSetup.getInstance().setLocalSearchDropdowns(request);
		return ProtocolUtil.reformatLocalSearchDropdownsInSession(request.getSession());
	}

	@Override
	public CurationService getCurationServiceDAO()
	{
		return curationServiceDAO;
	}

	@Override
	public SampleService getSampleService()
	{
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
