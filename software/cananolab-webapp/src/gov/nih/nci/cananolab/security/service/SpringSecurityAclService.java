package gov.nih.nci.cananolab.security.service;

import java.util.List;

import org.springframework.security.acls.model.Permission;

import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

public interface SpringSecurityAclService
{
	public boolean checkObjectPublic(Long securedObjectId, Class clazz);

	public boolean currentUserHasWritePermission(Long securedObjectId, Class clazz);

	public boolean currentUserHasDeletePermission(Long securedObjectId, Class clazz);
	
	public boolean currentUserHasReadPermission(Long securedObjectId, Class clazz);
	
	
	public void retractObjectFromPublic(Long securedObjectId, Class clazz);

	public void retractAccessToObjectForSid(Long securedObjectId, Class clazz, String sid);
	

	public void saveDefaultAccessForNewObject(Long securedObjectId, Class clazz);

	public void savePublicAccessForObject(Long securedObjectId, Class clazz);

	public void saveAccessForObject(Long securedObjectId, Class clazz, String recipient, boolean principal, List<Permission> perms);
	
	public void saveAccessForChildObject(Long parentObjectId, Class parentClass, Long securedObjectId, Class childClass);

	
	public boolean isOwnerOfObject(Long securedObjectId, Class clazz);

	public String getAccessString(Long securedObjectId, Class clazz);

	public void loadAccessControlInfoForObject(Long securedObjectId, Class clazz, SecuredDataBean accessControlData);

	public void deleteAllAccessExceptPublicAndDefault(Long securedObjectId, Class clazz);
	
	public void updateObjectOwner(Long securedObjectId, Class clazz, String newOwner);
	
	public void deleteAccessObject(Long securedObjectId, Class clazz);

	public AccessControlInfo fetchAccessControlInfoForObjectForUser(Long securedObjectId, Class clazz, String recipient);

}
