/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.workspace;

/**
 * Action class for Transfer Ownership section.
 *
 * @author lethai, pansu
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceItem;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.admin.impl.BatchOwnershipTransferProcess;
import gov.nih.nci.cananolab.service.admin.impl.OwnershipTransferServiceImpl;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class WorkspaceManager {
	
	private static Logger logger = Logger.getLogger(WorkspaceManager.class);
	
	CurationService curationService;


	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request)
			throws Exception {
		
		logger.info("In getWorkspaceItems");
		
		//return createDummy(new SimpleWorkspaceBean());
		
		SecurityService securityService = getSecurityService(request);
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		SimpleWorkspaceBean simpleWorkspace = new SimpleWorkspaceBean();
		
		List<SimpleWorkspaceItem> sampleItems = getSampleItems(request, securityService, user);
		simpleWorkspace.setSamples(sampleItems);
		
		List<SimpleWorkspaceItem> pubItems = getPublicationItems(request, securityService, user);
		simpleWorkspace.setPublications(pubItems);
	
		List<SimpleWorkspaceItem> protoItems = getProtocolItems(request, securityService, user);
		simpleWorkspace.setProtocols(protoItems);
		
		return simpleWorkspace;
	}
	
	protected List<SimpleWorkspaceItem> getPublicationItems(HttpServletRequest request,
			SecurityService securityService, UserBean user) 
			throws Exception {
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		
		PublicationService service = this.getPublicationServiceInSession(request, securityService);
		List<String> publicationIds = service.findPublicationIdsByOwner(user.getLoginName());
		if (publicationIds == null)
			return items;
		
		for (String id : publicationIds) {
			PublicationBean pubBean = service.findPublicationById(id, true);
			if (pubBean == null) continue;
			
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName(pubBean.getDomainFile().getTitle());
			item.setId(pubBean.getDomainFile().getId());
			item.setCreatedDate(pubBean.getDomainFile().getCreatedDate());
			item.getActions().add("View");
			
			item.setSubmisstionStatus("Approved"); //default
			DataReviewStatusBean reviewBean =  this.curationService.findDataReviewStatusBeanByDataId(id, securityService);
			if (reviewBean != null) {
				item.setSubmisstionStatus(reviewBean.getReviewStatus());
			}
			
			List<AccessibilityBean> groupAccesses = pubBean.getGroupAccesses();
			List<AccessibilityBean> userAccesses = pubBean.getUserAccesses();
			boolean isOwner = (user.getLoginName().equals(pubBean.getDomainFile().getCreatedBy())) ? true : false;
			String access = this.getAccessString(groupAccesses, userAccesses, user.getLoginName(), isOwner);
			item.setAccess(access);
			
			items.add(item);
		}
		
		return items;
	}
	
	protected List<SimpleWorkspaceItem> getProtocolItems(HttpServletRequest request,
			SecurityService securityService, UserBean user) 
			throws Exception {
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		ProtocolService protocolService = getProtocolServiceInSession(request, securityService);
		List<String> protoIds = protocolService.findProtocolIdsByOwner(user.getLoginName());
		
		if (protoIds == null)
			return items;
		
		for (String id : protoIds) {
			ProtocolBean protoBean = protocolService.findProtocolById(id);
		
			if (protoBean == null) continue;
			
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName(protoBean.getDomain().getName());
			item.setId(protoBean.getDomain().getId());
			item.setCreatedDate(protoBean.getDomain().getCreatedDate());
			
			if( protoBean.getFileBean().getDomainFile() != null && protoBean.getFileBean().getDomainFile().getId() != null && !StringUtils.isEmpty(protoBean.getFileBean().getDomainFile().getUri())) {
				item.setFileId(protoBean.getFileBean().getDomainFile().getId().longValue());
			}
			item.getActions().add("View");
			
			item.setSubmisstionStatus("Approved"); //default
			DataReviewStatusBean reviewBean =  this.curationService.findDataReviewStatusBeanByDataId(id, securityService);
			if (reviewBean != null) {
				item.setSubmisstionStatus(reviewBean.getReviewStatus());
			}
			
			List<AccessibilityBean> groupAccesses = protoBean.getGroupAccesses();
			List<AccessibilityBean> userAccesses = protoBean.getUserAccesses();
			boolean isOwner = (user.getLoginName().equals(protoBean.getDomain().getCreatedBy())) ? true : false;
			String access = this.getAccessString(groupAccesses, userAccesses, user.getLoginName(), isOwner);
			item.setAccess(access);
			
			items.add(item);
		}
		return items;
	}

	
	protected List<SimpleWorkspaceItem> getSampleItems(HttpServletRequest request,
			SecurityService securityService, UserBean user) 
			throws Exception {
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		SampleService sampleService = this.getSampleServiceInSession(request, securityService);
		String loginUser = user.getLoginName();
		
		List<String> sampleIds = sampleService.findSampleIdsByOwner(loginUser);
		if (sampleIds == null)
			return items;
	
		for (String id : sampleIds) {
			SampleBean sampleBean = sampleService.findSampleById(id, true);
			if (sampleBean == null) continue;
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName(sampleBean.getDomain().getName());
			item.setId(sampleBean.getDomain().getId());
			item.setCreatedDate(sampleBean.getDomain().getCreatedDate());
			item.getActions().add("View");
			
			item.setSubmisstionStatus("Approved"); //default
			
			DataReviewStatusBean reviewBean =  this.curationService.findDataReviewStatusBeanByDataId(id, securityService);
			if (reviewBean != null) {
				item.setSubmisstionStatus(reviewBean.getReviewStatus());
			}
			
			if (item.getName().equals("QA-Demo3")) {
				int i = 0;
				i++;
			}
			
			List<AccessibilityBean> groupAccesses = sampleBean.getGroupAccesses();
			List<AccessibilityBean> userAccesses = sampleBean.getUserAccesses();
			boolean isOwner = (loginUser.equals(sampleBean.getDomain().getCreatedBy())) ? true : false;
			String access = this.getAccessString(groupAccesses, userAccesses, user.getLoginName(), isOwner);
			
			item.setAccess(access);
					
			items.add(item);
		}
		
		return items;
	}
	
	protected String getAccessString(List<AccessibilityBean> groupAccesses, List<AccessibilityBean> userAccesses, 
			String loginUser, boolean isOwner) {
		StringBuilder sb = new StringBuilder();
		
		if (groupAccesses != null) {
			sb.append("Shared by: ");
			
			for (AccessibilityBean access : groupAccesses) {
				sb.append(access.getGroupName()).append(", ");
			}
		}
		
		if (userAccesses != null) {
			for (AccessibilityBean access : userAccesses) {
				UserBean ubean = access.getUserBean();
				if (!loginUser.equals(ubean.getLoginName()))
					sb.append(ubean.getLoginName()).append(", ");
					//sb.append(ubean.getFirstName()).append(" ").append(ubean.getLastName()).append(", ");
			}
		}
		
		String accessString = isOwner ? "(Owner, " : "(";
		accessString += sb.subSequence(0, sb.lastIndexOf(","));
		accessString += ")";
		
		return accessString;
	}

	private SecurityService getSecurityService(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");

		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		return securityService;
	}
	
	protected SimpleWorkspaceBean createDummy(SimpleWorkspaceBean workspaceBean) {
		
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		for (int i = 0; i < 5; i++) {
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName("Protocol " + i);
			item.setId(495959 + i);
			item.setCreatedDate(new Date());
			item.setSubmisstionStatus("Submitted for Public Access");
			List<String> actions = new ArrayList<String>();
			actions.add("View");
			actions.add("Edit");
			actions.add("Delete");
			item.setActions(actions);
			item.setAccess("Read Write Delete (Owner)");
			
			items.add(item);
		}
		
		workspaceBean.setProtocols(items);
		
		items = new ArrayList<SimpleWorkspaceItem>();
		for (int i = 0; i < 4; i++) {
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName("Publication " + i);
			item.setId(495959 + i);
			item.setCreatedDate(new Date());
			item.setSubmisstionStatus("In Review");
			List<String> actions = new ArrayList<String>();
			actions.add("View");
			actions.add("Edit");
			actions.add("Delete");
			item.setActions(actions);
			item.setAccess("Read Write Delete (Owner)");
			
			item.setPubMedId("1868677" + i);
			
			items.add(item);
		}
		
		workspaceBean.setPublications(items);
		
		
		return workspaceBean;
	}
	
	private SampleService getSampleServiceInSession(HttpServletRequest request, SecurityService securityService) {
		
		SampleService sampleService = (SampleService)request.getSession().getAttribute("sampleService");
		if (sampleService == null) {	
			sampleService = new SampleServiceLocalImpl(securityService);
			request.getSession().setAttribute("sampleService", sampleService);
		}
		return sampleService;

	}
	
	private PublicationService getPublicationServiceInSession(HttpServletRequest request, SecurityService securityService)
			throws Exception {
		PublicationService publicationService = (PublicationService)request.getSession().getAttribute("publicationService");
			
		if (publicationService == null) {
			publicationService = new PublicationServiceLocalImpl(securityService);
			request.getSession().setAttribute("publicationService", publicationService);
		}
		return publicationService;
	}
	
	private ProtocolService getProtocolServiceInSession(HttpServletRequest request, SecurityService securityService)
			throws Exception {
		
		ProtocolService protocolService = (ProtocolService)request.getSession().getAttribute("protocolService");
		if (protocolService == null) {	
			protocolService =	new ProtocolServiceLocalImpl(securityService);
			request.getSession().setAttribute("protocolService", protocolService);
		}	
		return protocolService;
	}

	public CurationService getCurationService() {
		return curationService;
	}

	public void setCurationService(CurationService curationService) {
		this.curationService = curationService;
	}
}
