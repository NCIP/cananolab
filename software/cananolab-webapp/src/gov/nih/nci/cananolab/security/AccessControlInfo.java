package gov.nih.nci.cananolab.security;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessControlInfo
{
	private String recipient;
	private String accessType;
	private String roleName = "";
	private String roleDisplayName = "";
	//private List<String> perms = new ArrayList<String>();
	
	//private List<BasePermission> permissions = new ArrayList<BasePermission>();
	
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
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public boolean isPrincipal()
	{
		boolean principalFlag = (AccessTypeEnum.USER.getAccessType().equals(this.accessType)) ? true : false;
		return principalFlag;
	}
	
	/*public List<String> getPerms() {
		return perms;
	}
	
	public void setPerms(List<String> perms) {
		this.perms = perms;
	}
	
	public List<BasePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<BasePermission> permissions) {
		this.permissions = permissions;
	}
	
	public List<BasePermission> getPermissionFromStr()
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
	}*/

	public String getRoleDisplayName() {
		return roleDisplayName;
	}

	public void setRoleDisplayName(String roleDisplayName) {
		this.roleDisplayName = roleDisplayName;
	}

	@Override
	public String toString() {
		return "AccessControlInfo [recipient=" + recipient + ", accessType=" + accessType + ", permStr=" + roleName + ", roleDisplayName=" + roleDisplayName + "]";
	}

}