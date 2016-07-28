package gov.nih.nci.cananolab.security.enums;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public enum CaNanoPermissionEnum
{
	CREATE(BasePermission.CREATE),
	READ(BasePermission.READ),
	WRITE(BasePermission.WRITE),
	DELETE(BasePermission.DELETE);

	private Permission perm;
	
	private CaNanoPermissionEnum(Permission perm) {
		this.perm = perm;
	}

	public Permission getPerm() {
		return perm;
	}

	public void setPerm(Permission perm) {
		this.perm = perm;
	}
	
	public static CaNanoPermissionEnum getFromPermission(BasePermission perm)
	{
		CaNanoPermissionEnum retEnum = null;
		for (CaNanoPermissionEnum permEnum : CaNanoPermissionEnum.values())
		{
			if (permEnum.getPerm() == perm)
			{
				retEnum = permEnum;
				break;
			}
		}
		return retEnum;
	}
	
	public static CaNanoPermissionEnum getFromString(String perm)
	{
		CaNanoPermissionEnum retEnum = null;
		for (CaNanoPermissionEnum permEnum : CaNanoPermissionEnum.values())
		{
			if (permEnum.getPerm().toString().equalsIgnoreCase(perm))
			{
				retEnum = permEnum;
				break;
			}
		}
		return retEnum;
	}

}