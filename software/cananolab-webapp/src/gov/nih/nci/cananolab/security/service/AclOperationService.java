package gov.nih.nci.cananolab.security.service;

import java.util.List;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;

public interface AclOperationService
{
	public MutableAcl fetchAclForObject(Class clazz, Long securedObjectId);
	public boolean isOwner(Long securedObjectId, Class clazz, String sid);
	
	public void createAclAndGrantAccess(Long securedObjectId, Class clazz, String recipient, boolean principal, List<Permission> perms, boolean setOwner);
	public void createAclForChildObject(Long parentObjectId, Class parentClass, Long securedObjectId, Class childClass);
	
	public void deleteAccessToSid(String sid, MutableAcl acl);
	public void deletePermission(Long securedObjectId, Class clazz, String sid, boolean principal, Permission perm);
    public void deleteAccessExceptPublicAndDefault(MutableAcl acl);
    
    public void deleteAcl(Long securedObjectId, Class clazz);
    
    public void updateObjectOwner(Long securedObjectId, Class clazz, String sid);
    
}
