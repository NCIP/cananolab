package gov.nih.nci.cananolab.security.enums;

public enum CaNanoRoleEnum 
{
	ROLE_ANONYMOUS("Public"),
	ROLE_RESEARCHER("Researcher"),
	ROLE_CURATOR("Curator"),
	ROLE_ADMIN("Admin");
	
	private String roleName;
	
	private CaNanoRoleEnum(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}	
}
