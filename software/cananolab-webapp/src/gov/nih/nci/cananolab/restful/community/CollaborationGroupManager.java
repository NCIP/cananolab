/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.community;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.service.GroupService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
//import org.apache.struts.validator.DynaValidatorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Methods for DWR Ajax
 *
 * @author pansu
 *
 */
@Component("collaborationGroupManger")
public class CollaborationGroupManager
{
	private Logger logger = Logger.getLogger(CollaborationGroupManager.class);
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private GroupService groupService;

	public CollaborationGroupBean getCollaborationGroupById(HttpServletRequest request, String id)throws Exception
	{
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			return null;
		}
		CollaborationGroupBean group = communityService.findCollaborationGroupById(id);
		request.getSession().setAttribute("group", group);
		return group;
	}

	public CollaborationGroupBean addUserAccess(HttpServletRequest request, AccessControlInfo userAccess) throws Exception
	{
		CollaborationGroupBean group = (CollaborationGroupBean) (request.getSession().getAttribute("group"));
		if (group == null){
			group = new CollaborationGroupBean();
		}
		// check whether user is a valid user
		CollaborationGroupBean bogusGroup=new CollaborationGroupBean();
		CananoUserDetails userDetail = (CananoUserDetails) userDetailsService.loadUserByUsername(userAccess.getRecipient());
		if (userDetail == null)
		{
			bogusGroup.setName("!!invalid user");
			throw new Exception("!!invalid user");
			//return bogusGroup;
		}
		// if the user is already a curator, don't add the user
		else if (userDetail.isCurator()) {
			bogusGroup.setName("!!user is a curator");
			throw new Exception("!!user is a curator");
			//return bogusGroup;
		}
		group.addUserAccess(userAccess);
		return group;
	}

	public CollaborationGroupBean deleteUserAccess(HttpServletRequest request, AccessControlInfo userAccess) throws Exception
	{
		CollaborationGroupBean group = (CollaborationGroupBean) (request.getSession().getAttribute("group"));
		group.removeUserAccess(userAccess);
		groupService.removeGroupMember(Long.valueOf(group.getId()), userAccess.getRecipient());
		return group;
	}

}
