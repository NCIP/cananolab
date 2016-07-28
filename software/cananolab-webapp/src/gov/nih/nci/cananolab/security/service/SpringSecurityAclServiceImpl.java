package gov.nih.nci.cananolab.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;
import gov.nih.nci.cananolab.security.enums.CaNanoPermissionEnum;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.Constants;

@Component("springSecurityAclService")
public class SpringSecurityAclServiceImpl implements SpringSecurityAclService
{
	private static Logger logger = Logger.getLogger(AclOperationServiceImpl.class);

	@Autowired
	private AclOperationService aclOperationService;
	
	@Autowired
	private PermissionEvaluator permissionEvaluator;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public boolean checkObjectPublic(Long securedObjectId, Class clazz)
	{
		logger.debug("Checking if secure objectId: " + securedObjectId + " of class: " + clazz.getName() + " is Public.");
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		List<AccessControlEntry> entries = acl.getEntries();
		if (entries != null)
		{
			for (AccessControlEntry entry : entries)
			{
				if (entry.getSid() instanceof GrantedAuthoritySid)
				{
					GrantedAuthoritySid gaSid = (GrantedAuthoritySid) entry.getSid();
					if (CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equals(gaSid.getGrantedAuthority())) 
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public void retractObjectFromPublic(Long securedObjectId, Class clazz)
	{
		logger.debug("Retract public access to secure objectId: " + securedObjectId + " of class: " + clazz.getName());
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		aclOperationService.deleteAccessToSid(CaNanoRoleEnum.ROLE_ANONYMOUS.toString(), acl);
		logger.debug("Deleted public access for securedObject " + securedObjectId + " of class " + clazz.getName());
		
	}
	
	@Override
	public boolean currentUserHasWritePermission(Long securedObjectId, Class clazz)
	{
		//TODO:test to see if isCurator check is also needed or hasPermission will handle checking against a user's
		//Curator role also if user has the role.
		boolean writePermission = permissionEvaluator.hasPermission(SpringSecurityUtil.getAuthentication(), securedObjectId, clazz.getName(), BasePermission.WRITE);
		return writePermission;
	}
	
	@Override
	public boolean currentUserHasDeletePermission(Long securedObjectId, Class clazz)
	{
		//TODO:test to see if isCurator check is also needed or hasPermission will handle checking against a user's
		//Curator role also if user has the role -Curator has all privileges
		boolean deletePermission = permissionEvaluator.hasPermission(SpringSecurityUtil.getAuthentication(), securedObjectId, clazz.getName(), BasePermission.DELETE);
		return deletePermission;
	}
	
	@Override
	public boolean currentUserHasReadPermission(Long securedObjectId, Class clazz)
	{
		boolean readPermission = permissionEvaluator.hasPermission(SpringSecurityUtil.getAuthentication(), securedObjectId, clazz.getName(), BasePermission.READ);
		return readPermission;
	}
	
	@Override
	public void retractAccessToObjectForSid(Long securedObjectId, Class clazz, String sid)
	{
		logger.debug("Retract public access to secure objectId: " + securedObjectId + " of class: " + clazz.getName());
		
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		aclOperationService.deleteAccessToSid(sid, acl);
		logger.debug("Deleted public access for securedObject " + securedObjectId + " of class " + clazz.getName());
	}
	
	@Override
	public boolean isOwnerOfObject(Long securedObjectId, Class clazz)
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		return userDetails.isCurator() || aclOperationService.isOwner(securedObjectId, clazz, userDetails.getUsername());
	}
	
	@Override
	public String getAccessString(Long securedObjectId, Class clazz)
	{
		boolean isOwner = isOwnerOfObject(securedObjectId, clazz);
		
		StringBuilder sb = isOwner ? new StringBuilder("(Owner, Shared with: ") : new StringBuilder("(Shared by: ") ;

		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		List<AccessControlEntry> entries = acl.getEntries();
		if (entries != null)
		{
			for (AccessControlEntry entry : entries)
			{
				if (entry.getSid() instanceof GrantedAuthoritySid)
				{
					GrantedAuthoritySid gaSid = (GrantedAuthoritySid) entry.getSid();
					sb.append(gaSid.getGrantedAuthority()).append(", ");
				}
				else
				{
					PrincipalSid pSid = (PrincipalSid) entry.getSid();
					if (isOwner && !SpringSecurityUtil.getLoggedInUserName().equals(pSid.getPrincipal()))
						sb.append(pSid.getPrincipal()).append(", ");
				}
			}
		}

		return sb.substring(0, sb.lastIndexOf(",")) + ")";
	}
	
	@Override
	public void saveDefaultAccessForNewObject(Long securedObjectId, Class clazz)
	{
		List<Permission> perms = new ArrayList<Permission>();
		perms.add(BasePermission.CREATE);
		perms.add(BasePermission.READ);
		perms.add(BasePermission.WRITE);
		perms.add(BasePermission.DELETE);
		aclOperationService.createAclAndGrantAccess(securedObjectId, clazz, SpringSecurityUtil.getLoggedInUserName(), true, perms, true);
		aclOperationService.createAclAndGrantAccess(securedObjectId, clazz, CaNanoRoleEnum.ROLE_CURATOR.toString(), false, perms, false);
		
	}
	
	@Override
	public void savePublicAccessForObject(Long securedObjectId, Class clazz)
	{
		List<Permission> perms = new ArrayList<Permission>();
		perms.add(BasePermission.READ);
		aclOperationService.createAclAndGrantAccess(securedObjectId, clazz, CaNanoRoleEnum.ROLE_ANONYMOUS.toString(), false, perms, false);
		
	}
	
	@Override
	public void saveAccessForChildObject(Long parentObjectId, Class parentClass, Long securedObjectId, Class childClass)
	{
		aclOperationService.createAclForChildObject(parentObjectId, parentClass, securedObjectId, childClass);
	}
	
	@Override
	public void saveAccessForObject(Long securedObjectId, Class clazz, String recipient, boolean principal, List<Permission> perms)
	{
		CananoUserDetails userDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(recipient);
		
		//grant explicit permission for a user only if he/she is already not a curator.
		if (userDetails != null & !userDetails.isCurator())
			aclOperationService.createAclAndGrantAccess(securedObjectId, clazz, recipient, principal, perms, false);
	}
	
	@Override
	public void loadAccessControlInfoForObject(Long securedObjectId, Class clazz, SecuredDataBean accessControlData)
	{
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		CananoUserDetails loggedInUser = SpringSecurityUtil.getPrincipal();
		List<String> loggedInUserGroups = new ArrayList<String>();
		List<String> loggedInUserRoles = new ArrayList<String>();
		if (loggedInUser != null)
		{
			loggedInUserGroups = loggedInUser.getGroups();
			loggedInUserRoles = loggedInUser.getRoles();
		}
		Map<String, AccessControlInfo> userAccesses = new HashMap<String, AccessControlInfo>();
		Map<String, AccessControlInfo> groupAccesses = new HashMap<String, AccessControlInfo>();
		List<AccessControlEntry> entries = acl.getEntries();
		for (AccessControlEntry entry : entries)
		{
			String aclSid = "";
			AccessTypeEnum accessType = null;
			AccessControlInfo info = null;
			if (entry.getSid() instanceof GrantedAuthoritySid)
			{
				GrantedAuthoritySid gaSid = (GrantedAuthoritySid) entry.getSid();
				aclSid = gaSid.getGrantedAuthority();
				accessType = (aclSid.startsWith(Constants.ROLE_PREFIX)) ? AccessTypeEnum.ROLE : AccessTypeEnum.GROUP;
				info = groupAccesses.get(aclSid);
			}
			else
			{
				PrincipalSid pSid = (PrincipalSid) entry.getSid();
				aclSid = pSid.getPrincipal();
				accessType = AccessTypeEnum.USER;
				info = userAccesses.get(aclSid);
			}
			
			if (info == null)
			{
				info = new AccessControlInfo();
				info.setRecipient(aclSid);
				Map<String, AccessControlInfo> map = (accessType == AccessTypeEnum.USER) ? userAccesses : groupAccesses;
				map.put(aclSid, info);
				info.setAccessType(accessType.getAccessType());
			}
			BasePermission permission = (BasePermission) entry.getPermission();
			info.addPerm(permission);
			info.setPermStr(info.getPermStr() + " " + CaNanoPermissionEnum.getFromPermission(permission).toString());
		}

		for (String recipient: userAccesses.keySet())
			accessControlData.addUserAccess(userAccesses.get(recipient));
		
		//add only those groups/roles to which the logged in user is a member of
		for (String recipient: groupAccesses.keySet())
		{
			if ((loggedInUserRoles != null && loggedInUserRoles.contains(recipient)) ||
				(loggedInUserGroups != null && loggedInUserGroups.contains(recipient)))
				accessControlData.addGroupAccess(groupAccesses.get(recipient));
		}
	}
	
	@Override
	public AccessControlInfo fetchAccessControlInfoForObjectForUser(Long securedObjectId, Class clazz, String recipient)
	{
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		AccessControlInfo userAccess = new AccessControlInfo();
		userAccess.setRecipient(recipient);
		userAccess.setAccessType(AccessTypeEnum.USER.getAccessType());

		List<AccessControlEntry> entries = acl.getEntries();
		for (AccessControlEntry entry : entries)
		{
			String aclSid = "";
			if (entry.getSid() instanceof PrincipalSid)
			{
				PrincipalSid pSid = (PrincipalSid) entry.getSid();
				aclSid = pSid.getPrincipal();
				if (userAccess != null && recipient.equals(aclSid))
				{
					userAccess.addPerm((BasePermission) entry.getPermission());
					userAccess.setPermStr(userAccess.getPermStr() + " " + entry.getPermission().toString());
				}
			}
		}
		return userAccess;
	}
	
	@Override
	public void deleteAllAccessExceptPublicAndDefault(Long securedObjectId, Class clazz)
	{
		logger.debug("Delete all access to object except for Public and Default.");
		MutableAcl acl = aclOperationService.fetchAclForObject(clazz, securedObjectId);
		aclOperationService.deleteAccessExceptPublicAndDefault(acl);
	}

	@Override
	public void updateObjectOwner(Long securedObjectId, Class clazz, String newOwner) {
		logger.debug("Update owner of " + clazz + ": " + securedObjectId + " to new owner: " + newOwner);
		aclOperationService.updateObjectOwner(securedObjectId, clazz, newOwner);
	}

	@Override
	public void deleteAccessObject(Long securedObjectId, Class clazz) {
		aclOperationService.deleteAcl(securedObjectId, clazz);
		
	}
	
}
