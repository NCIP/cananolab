package gov.nih.nci.cananolab.security.enums;

public enum AccessTypeEnum
{
	USER("user"),
	GROUP("group"),
	ROLE("role");
	
	private String accessType;
	
	private AccessTypeEnum(String accessType) {
		this.accessType = accessType;
	}
	
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	public static AccessTypeEnum fromString(String str)
	{
		for (AccessTypeEnum currEnum: AccessTypeEnum.values())
			if (currEnum.accessType.equalsIgnoreCase(str))
				return currEnum;
		
		return null;
	}

}
