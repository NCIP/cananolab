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
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.dto.particle.SampleBasicBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceItem;
import gov.nih.nci.cananolab.service.common.helper.CommonServiceHelper;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class WorkspaceManager extends BaseAnnotationBO{

	private static Logger logger = Logger.getLogger(WorkspaceManager.class);

	CurationService curationService;
	
	private DataAvailabilityService dataAvailabilityService;
	
	CommonServiceHelper helper = new CommonServiceHelper();


	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request)
			throws Exception {

		logger.info("In getWorkspaceItems");

		//return createDummy(new SimpleWorkspaceBean());

		SecurityService securityService = getSecurityService(request);
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		SimpleWorkspaceBean simpleWorkspace = new SimpleWorkspaceBean();

		List<SimpleWorkspaceItem> sampleItems =  getSampleItems(request, securityService, user);
		simpleWorkspace.setSamples(sampleItems);

		List<SimpleWorkspaceItem> pubItems = getPublicationItems(request, securityService, user);
		simpleWorkspace.setPublications(pubItems);

		List<SimpleWorkspaceItem> protoItems = getProtocolItems(request, securityService, user);
		simpleWorkspace.setProtocols(protoItems);

		return simpleWorkspace;
	}
	
	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request, String type)
			throws Exception {

		logger.info("In getWorkspaceItems with type: " + type);

		SecurityService securityService = getSecurityService(request);
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		SimpleWorkspaceBean simpleWorkspace = new SimpleWorkspaceBean();

		List<SimpleWorkspaceItem> items = null;
		if (type.equals("sample")) {
			items =  getSampleItems(request, securityService, user);
			simpleWorkspace.setSamples(items);
		} else if (type.equals("protocol")) {
			items = getProtocolItems(request, securityService, user);
			simpleWorkspace.setProtocols(items);
		} else if (type.equals("publication")) {
			items = getPublicationItems(request, securityService, user);
			simpleWorkspace.setPublications(items);
		}
		
		return simpleWorkspace;
	}

	protected List<SimpleWorkspaceItem> getPublicationItems(HttpServletRequest request,
			SecurityService securityService, UserBean user)
			throws Exception {
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();

		PublicationService service = this.getPublicationServiceInSession(request, securityService);
		List<String> publicationIds = service.findPublicationIdsByOwner(user.getLoginName());
		List<String> Ids = helper.findSharedPublications(user.getLoginName());
		if (publicationIds == null)
			return items;
		List<String> publicationIdList = new ArrayList<String>();
		for(String pubId : publicationIds){
			if(!Ids.contains(pubId))
				publicationIdList.add(pubId);
		}
		for(String Id : Ids){
			publicationIdList.add(Id);
		}
		for (String id : publicationIds) {
			PublicationBean pubBean = service.findPublicationById(id, true);
			if (pubBean == null) continue;

			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName(pubBean.getDomainFile().getTitle());
			item.setId(pubBean.getDomainFile().getId());
			item.setCreatedDate(pubBean.getDomainFile().getCreatedDate());
			Publication pub = (Publication) pubBean.getDomainFile();
			
			String pubId = (pub.getPubMedId() != null) ? pub.getPubMedId().toString() : "";
			String val = "";
			if (pubId.length() > 0) {
				val = "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/" + pubId + "\" target=\"_blank\">" + pubId + "</a>";
			}
			
			val = (pub.getDigitalObjectId()!=null) ? val + "<br>" + pub.getDigitalObjectId().toString() : val;
			item.setPubMedDOIId(val);
			
			setCommonDataFields(id, item, pubBean, securityService, user);

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
		List<String> Id = helper.findSharedProtocols(user.getLoginName());
		
		List<String> protoIdList = new ArrayList<String>();
		
		for(String ids : protoIds){
			if(!Id.contains(ids))
			protoIdList.add(ids);
		}
		for(String pid : Id){
			protoIdList.add(pid);
		}
		if (protoIds == null)
			return items;

		for (String id : protoIds) {
			//User this method so it won't check user read permission, since user owns this item
			//Performance turning
			ProtocolBean protoBean = protocolService.findWorkspaceProtocolById(id);

			if (protoBean == null) continue;

			SimpleWorkspaceItem item = new SimpleWorkspaceItem();
			item.setName(protoBean.getDomain().getName());
			item.setId(protoBean.getDomain().getId());
			item.setCreatedDate(protoBean.getDomain().getCreatedDate());

			File domainFile = protoBean.getFileBean().getDomainFile();
			if( domainFile != null &&
					domainFile.getId() != null &&
					!StringUtils.isEmpty(domainFile.getUri())) {
				item.setFileId(protoBean.getFileBean().getDomainFile().getId().longValue());
			}

			File file = protoBean.getDomain().getFile();
			if (file != null && file.getUriExternal())
				item.setExternalURL(file.getUri());

			setCommonDataFields(id, item, protoBean, securityService, user);

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
		List<String> sharedByIds = helper.findSharedSampleIds(user.getLoginName());
		
		for (String sharedById : sharedByIds) {
			if (!sampleIds.contains(sharedById))
				sampleIds.add(sharedById);
		}
		if (sampleIds == null)
			return items;

		for (String id : sampleIds) {
			//Use this method so it won't check user read permission, since user owns this item
			//Performance turning
			SampleBasicBean sampleBean = sampleService.findSWorkspaceSampleById(id, true);
			if (sampleBean == null) continue;
			SimpleWorkspaceItem item = new SimpleWorkspaceItem();

			item.setName(sampleBean.getDomain().getName());
			item.setId(sampleBean.getDomain().getId());
			item.setCreatedDate(sampleBean.getDomain().getCreatedDate());
			
			setCommonDataFields(id, item, sampleBean, securityService, user);

			items.add(item);
		}

		return items;
	}

	protected String getAccessString(List<AccessibilityBean> groupAccesses, List<AccessibilityBean> userAccesses,
			String loginUser, boolean isOwner) {
		StringBuilder sb = new StringBuilder();

		if (groupAccesses != null) {
		if(isOwner){
				sb.append("Shared with: ");
			}else{
				sb.append("Shared by:");
			}

			for (AccessibilityBean access : groupAccesses) {
				sb.append(access.getGroupName()).append(", ");
			}
		}

		if (userAccesses != null) {
			for (AccessibilityBean access : userAccesses) {
				UserBean ubean = access.getUserBean();
				if(isOwner){
					if (!loginUser.equals(ubean.getLoginName()))
						sb.append(ubean.getLoginName()).append(", ");
						//sb.append(ubean.getFirstName()).append(" ").append(ubean.getLastName()).append(", ");
				}
//				if (!loginUser.equals(ubean.getLoginName()))
//					sb.append(ubean.getLoginName()).append(", ");
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
			item.setAccess("Read Write Delete (Owner)");
			item.setEditable(false);
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

			item.setAccess("Read Write Delete (Owner)");
			item.setEditable(false);
			//item.setPubMedId("1868677" + i);

			items.add(item);
		}

		workspaceBean.setPublications(items);


		return workspaceBean;
	}

	protected void setCommonDataFields(String itemId, SimpleWorkspaceItem item,
			SecuredDataBean dataBean, SecurityService securityService, UserBean user) {

		item.setEditable(dataBean.getUserUpdatable());

		if (dataBean.getPublicStatus())
			item.setSubmisstionStatus("Approved");
		else
			item.setSubmisstionStatus("In Draft");

		try {
			DataReviewStatusBean reviewBean =  this.curationService.findDataReviewStatusBeanByDataId(itemId, securityService);
			if (reviewBean != null) {
				item.setSubmisstionStatus(StringUtils.getCamelCaseFormatInWords(reviewBean.getReviewStatus()));
			}
		} catch (Exception e) {
			logger.debug("Exception while finding data review status due to curator role restriction. Ignore for now");
		}

		List<AccessibilityBean> groupAccesses = dataBean.getGroupAccesses();
		List<AccessibilityBean> userAccesses = dataBean.getUserAccesses();

		String access = this.getAccessString(groupAccesses, userAccesses, user.getLoginName(), dataBean.getUserIsOwner());
		item.setAccess(access);
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
	public String deleteSample(String sampleId, HttpServletRequest request)
			throws Exception {
		
		SecurityService securityService = getSecurityService(request);
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		SampleService sampleService = this.getSampleServiceInSession(request, securityService);
		
		SampleBean sampleBean = sampleService.findSampleById(sampleId, true);
		if (sampleBean == null)
			return "Error: unable to find a valid sample in session with id . Sample deletion failed";
		
		String sampleName = sampleBean.getDomain().getName();

		// remove all access associated with sample takes too long. Set up the
		// delete job in scheduler
		InitSampleSetup.getInstance().updateCSMCleanupEntriesInContext(
				sampleBean.getDomain(), request);

		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				sampleBean.getDomain().getId().toString(), sampleBean
						.getDomain().getName(), "sample");
		if (sampleBean.getHasDataAvailability()) {
			dataAvailabilityService.deleteDataAvailability(sampleBean
					.getDomain().getId().toString(), securityService);
		}
		sampleService.deleteSample(sampleBean.getDomain().getName());
		request.getSession().removeAttribute("theSample");
		
		String msg = PropertyUtil.getPropertyReplacingToken("sample", "message.deleteSample", "0", sampleName);
		
		return msg;
	}
	
	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}
	
}