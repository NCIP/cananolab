package gov.nih.nci.cananolab.security.enums;

import gov.nih.nci.cananolab.util.StringUtils;

public enum CaNanoPermissionEnum
{
	C("CREATE"),
	R("READ"),
	W("WRITE"),
	D("DELETE");
	
	private String permValue;
	
	private CaNanoPermissionEnum(String permValue) {
		this.permValue = permValue;
	}
	
	public String getPermValue() {
		return permValue;
	}
	
	public void setPermValue(String permValue) {
		this.permValue = permValue;
	}
	
	public static CaNanoPermissionEnum getFromStr(String str)
	{
		CaNanoPermissionEnum permEnum = null;
		if (!StringUtils.isEmpty(str))
		{
			if (CaNanoPermissionEnum.C.toString().equalsIgnoreCase(str))
				permEnum = CaNanoPermissionEnum.C;
			else if (CaNanoPermissionEnum.R.toString().equalsIgnoreCase(str))
				permEnum = CaNanoPermissionEnum.R;
			else if (CaNanoPermissionEnum.W.toString().equalsIgnoreCase(str))
				permEnum = CaNanoPermissionEnum.W;
			else if (CaNanoPermissionEnum.D.toString().equalsIgnoreCase(str))
				permEnum = CaNanoPermissionEnum.D;
		}
		return permEnum;
	}

}