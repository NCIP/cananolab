package gov.nih.nci.cananolab.security.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.dao.AclDao;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("aclOperationService")
//@Transactional(propagation=Propagation.REQUIRED)
public class AclOperationServiceImpl implements AclOperationService
{
	private static Logger logger = Logger.getLogger(AclOperationServiceImpl.class);

	@Autowired
	private MutableAclService aclService;
	
	@Override
	public MutableAcl fetchAclForObject(Class clazz, Long securedObjectId)
	{
		ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObjectId);
		MutableAcl acl = null;
		if (oid != null)
		{
			try {
				acl = (MutableAcl) aclService.readAclById(oid);
			} catch (NotFoundException nfe) {
				acl = aclService.createAcl(oid);
			}
		}
		return acl;
	}

	@Override
	public void deletePermission(Long securedObjectId, Class clazz, String recipient, boolean principal, Permission perm) 
	{
		logger.debug("Remove the requested permission for the recipient.");
		MutableAcl acl = fetchAclForObject(clazz, securedObjectId);

		List<AccessControlEntry> entries = acl.getEntries();

		int i = 0;
		if (entries != null)
		{
			for (AccessControlEntry entry : entries)
			{
				if (entry.getSid().equals(recipient) && entry.getPermission().equals(perm))
					acl.deleteAce(i);
				else
					i++;
			}
		}

		aclService.updateAcl(acl);

		if (logger.isDebugEnabled()) {
			logger.debug("Deleted securedObject " + securedObjectId + " ACL permissions for recipient " + recipient);
		}
	}
	
	@Override
	public void deleteAccessToSid(String sid, MutableAcl acl)
	{
		if (!StringUtils.isEmpty(sid))
		{
			List<AccessControlEntry> entries = acl.getEntries();
			if (entries != null)
			{
				int i = 0;
				for (AccessControlEntry entry : entries)
				{
					String aclSid = "";
					if (entry.getSid() instanceof GrantedAuthoritySid)
					{
						GrantedAuthoritySid gaSid = (GrantedAuthoritySid) entry.getSid();
						aclSid = gaSid.getGrantedAuthority();
					}
					else
					{
						PrincipalSid pSid = (PrincipalSid) entry.getSid();
						aclSid = pSid.getPrincipal();
					}
					if (sid.equals(aclSid)) 
						acl.deleteAce(i);
					else
						i++;
				}
				aclService.updateAcl(acl);
			}
		}
	}

	@Override
	public boolean isOwner(Long securedObjectId, Class clazz, String sid)
	{
		boolean isOwner = false;
		
		if (!StringUtils.isEmpty(sid))
		{
			ObjectIdentity oid = new ObjectIdentityImpl(clazz.getName(), securedObjectId);
			MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
			Sid ownerSid = acl.getOwner();
			if (ownerSid != null && ownerSid instanceof PrincipalSid)
				isOwner = sid.equals(((PrincipalSid) ownerSid).getPrincipal());
		}
		
		return isOwner;
	}

	@Override
	public void createAclAndGrantAccess(Long securedObjectId, Class clazz, String recipient, boolean principal, List<Permission> perms, boolean setOwner)
	{
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, securedObjectId);
		
		//	Create or update the relevant ACL
		MutableAcl acl = null;
		try {
			acl = (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			acl = aclService.createAcl(oi);
		}
		
		Sid sid = (principal) ? new PrincipalSid(recipient) : new GrantedAuthoritySid(recipient);
		for (Permission perm : perms)
			acl.insertAce(acl.getEntries().size(), perm, sid, true);
		
		if (setOwner)
			acl.setOwner(sid);
		
		aclService.updateAcl(acl);
	}
	
	@Override
	public void createAclForChildObject(Long parentObjectId, Class parentClass, Long securedObjectId, Class childClass)
	{
		MutableAcl parentAcl = fetchAclForObject(parentClass, parentObjectId);
		
		ObjectIdentity oi = new ObjectIdentityImpl(childClass, securedObjectId);
		
		//Create or update the relevant child object ACL
		MutableAcl acl = null;
		try {
			acl = (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			acl = aclService.createAcl(oi);
		}
		if (parentAcl != null)
		{
			acl.setParent(parentAcl);
			acl.setEntriesInheriting(true);
			acl.setOwner(parentAcl.getOwner());
		}
		aclService.updateAcl(acl);
	}

	@Override
	public void deleteAccessExceptPublicAndDefault(MutableAcl acl)
	{
		if (acl != null)
		{
			String owner = ((PrincipalSid)acl.getOwner()).getPrincipal();
			List<AccessControlEntry> entries = acl.getEntries();
			if (entries != null)
			{
				int i = 0;
				for (AccessControlEntry entry : entries)
				{
					String aclSid = "";
					if (entry.getSid() instanceof GrantedAuthoritySid)
					{
						GrantedAuthoritySid gaSid = (GrantedAuthoritySid) entry.getSid();
						aclSid = gaSid.getGrantedAuthority();
					}
					else
					{
						PrincipalSid pSid = (PrincipalSid) entry.getSid();
						aclSid = pSid.getPrincipal();
					}
					if (!aclSid.equalsIgnoreCase(CaNanoRoleEnum.ROLE_ANONYMOUS.toString()) &&
						!aclSid.equalsIgnoreCase(CaNanoRoleEnum.ROLE_CURATOR.toString()) &&
						!aclSid.equalsIgnoreCase(CaNanoRoleEnum.ROLE_RESEARCHER.toString()) &&
						!aclSid.equalsIgnoreCase(owner)) 
						acl.deleteAce(i);
					else
						i++;
				}
				aclService.updateAcl(acl);
			}
		}	
		
	}

	@Override
	public void updateObjectOwner(Long securedObjectId, Class clazz, String sid)
	{
		MutableAcl acl = fetchAclForObject(clazz, securedObjectId);
		Sid newSid = new PrincipalSid(sid);
		acl.setOwner(newSid);
		aclService.updateAcl(acl);
		
	}

	@Override
	public void deleteAcl(Long securedObjectId, Class clazz) {
		ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObjectId);
		aclService.deleteAcl(oid, true);
		
	}

}