package gov.nih.nci.cananolab.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;
import gov.nih.nci.cananolab.security.enums.CaNanoPermissionEnum;
import gov.nih.nci.cananolab.util.StringUtils;

public class AccessControlInfo
{
	private String recipient;
	private String accessType;
	private String permStr = "";
	private List<String> perms = new ArrayList<String>();
	
	private List<Permission> permissions = new ArrayList<Permission>();
	
	public String getRecipient() {
		return recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getAccessType() {
		return accessType;
	}
	
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	public String getPermStr() {
		return permStr;
	}

	public void setPermStr(String permStr) {
		this.permStr = permStr;
	}

	public List<String> getPerms() {
		return perms;
	}
	
	public void setPerms(List<String> perms) {
		this.perms = perms;
	}
	
	public void addPerm(BasePermission perm)
	{
		this.permissions.add(perm);
	}
	
	public boolean isPrincipal()
	{
		boolean principalFlag = (AccessTypeEnum.USER.getAccessType().equals(this.accessType)) ? true : false;
		return principalFlag;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public List<Permission> getPermissionFromStr()
	{
		if (!StringUtils.isEmpty(permStr))
		{
			String[] permArray = this.permStr.split(" ");
			for (String permStr: permArray)
			{
				CaNanoPermissionEnum permEnum = CaNanoPermissionEnum.getFromString(permStr);
				if (permEnum != null)
					this.permissions.add(permEnum.getPerm());
			}
		}
		return this.permissions;
	}

	@Override
	public String toString() {
		return "AccessControlInfo [recipient=" + recipient + ", accessType=" + accessType + ", perms= (" + perms.toString() + ")]";
	}

}