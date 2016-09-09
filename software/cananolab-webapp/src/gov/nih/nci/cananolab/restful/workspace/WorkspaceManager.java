/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.workspace;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Action class for Transfer Ownership section.
 *
 * @author lethai, pansu
 */
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.dto.particle.SampleBasicBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceItem;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.common.helper.CommonServiceHelper;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("workspaceManager")
public class WorkspaceManager extends BaseAnnotationBO
{
	private static Logger logger = Logger.getLogger(WorkspaceManager.class);

	@Autowired
	private SpringSecurityAclService springSecurityAclService;

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private CurationService curationServiceDAO;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private PublicationService publicationService;

	@Autowired
	private UserDetailsService userDetailsService;

	CommonServiceHelper helper = new CommonServiceHelper();

	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request) throws Exception
	{
		logger.info("In getWorkspaceItems");
		SimpleWorkspaceBean simpleWorkspace = new SimpleWorkspaceBean();

		List<SimpleWorkspaceItem> sampleItems =  getSampleItems(request);
		simpleWorkspace.setSamples(sampleItems);

		List<SimpleWorkspaceItem> pubItems = getPublicationItems(request);
		simpleWorkspace.setPublications(pubItems);

		List<SimpleWorkspaceItem> protoItems = getProtocolItems(request);
		simpleWorkspace.setProtocols(protoItems);

		return simpleWorkspace;
	}

	public SimpleWorkspaceBean getWorkspaceItems(HttpServletRequest request, String type) throws Exception
	{
		logger.info("In getWorkspaceItems with type: " + type);

		SimpleWorkspaceBean simpleWorkspace = new SimpleWorkspaceBean();

		List<SimpleWorkspaceItem> items = null;
		if (type.equals("sample")) {
			items =  getSampleItems(request);
			simpleWorkspace.setSamples(items);
		} else if (type.equals("protocol")) {
			items = getProtocolItems(request);
			simpleWorkspace.setProtocols(items);
		} else if (type.equals("publication")) {
			items = getPublicationItems(request);
			simpleWorkspace.setPublications(items);
		}

		return simpleWorkspace;
	}

	protected List<SimpleWorkspaceItem> getPublicationItems(HttpServletRequest request) throws Exception
	{
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();

		List<String> publicationIds = publicationService.findPublicationIdsByOwner(userDetails.getUsername());
		List<String> Ids = new ArrayList<String>();
		if(!userDetails.isCurator()){
			Ids = helper.findSharedPublications(userDetails.getUsername());
			for (String pubId : Ids){
				if (!publicationIds.contains(pubId))
					publicationIds.add(pubId);
			}
		}

		if (publicationIds == null)
			return items;

		for (String id : publicationIds) {
			PublicationBean pubBean = publicationService.findPublicationByIdWorkspace(id, true);
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

			setCommonDataFields(id, item, pubBean, SecureClassesEnum.PUBLICATION);

			items.add(item);
		}

		return items;
	}

	protected List<SimpleWorkspaceItem> getProtocolItems(HttpServletRequest request) throws Exception
	{
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		List<String> protoIds = protocolService.findProtocolIdsByOwner(userDetails.getUsername());

		if(!userDetails.isCurator()){
			List<String> Id = helper.findSharedProtocols(userDetails.getUsername());
			for(String ids : Id){
				if(!protoIds.contains(ids))
					protoIds.add(ids);
			}
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

			setCommonDataFields(id, item, protoBean, SecureClassesEnum.PROTOCOL);

			items.add(item);
		}
		return items;
	}


	protected List<SimpleWorkspaceItem> getSampleItems(HttpServletRequest request) throws Exception
	{
		List<SimpleWorkspaceItem> items = new ArrayList<SimpleWorkspaceItem>();
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();

		List<String> sampleIds = sampleService.findSampleIdsByOwner(userDetails.getUsername());
		List<String> sharedByIds = new ArrayList<String>();
		//Only Researchers have shared items, not curators.
		if(!userDetails.isCurator()){
			sharedByIds = helper.findSharedSampleIds(userDetails.getUsername());
			for (String sharedById : sharedByIds) {
				if (!sampleIds.contains(sharedById))
					sampleIds.add(sharedById);
			}
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

			setCommonDataFields(id, item, sampleBean, SecureClassesEnum.SAMPLE);

			items.add(item);
		}

		return items;
	}

	protected void setCommonDataFields(String itemId, SimpleWorkspaceItem item, SecuredDataBean dataBean, SecureClassesEnum type)
	{
		boolean isWriteable = springSecurityAclService.currentUserHasWritePermission(Long.valueOf(itemId), type.getClazz());
		item.setEditable(isWriteable);
		Class clazz = type.getClazz();	
		item.setOwner(springSecurityAclService.isOwnerOfObject(Long.valueOf(itemId), clazz));

		if (springSecurityAclService.checkObjectPublic(Long.valueOf(itemId), clazz))
			item.setSubmisstionStatus("Approved");
		else
			item.setSubmisstionStatus("In Draft");

		try {
			DataReviewStatusBean reviewBean = curationServiceDAO.findDataReviewStatusBeanByDataId(itemId);
			if (reviewBean != null) {
				item.setSubmisstionStatus(StringUtils.getCamelCaseFormatInWords(reviewBean.getReviewStatus()));
			}
		} catch (Exception e) {
			logger.debug("Exception while finding data review status due to curator role restriction. Ignore for now");
		}

		String access = springSecurityAclService.getAccessString(Long.valueOf(itemId), type.getClazz());
		item.setAccess(access);
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