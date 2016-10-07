package gov.nih.nci.cananolab.datamigration.service;

import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

public interface MigrateDataService
{
	public void migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum dataType);
	
	public void migratePublicAccessDataFromCSMToSpring(SecureClassesEnum dataType);
	
	public void migrateUserAccountsFromCSMToSpring();

	public void grantCuratorRoleToAccounts();
	
	public void migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum dataType);
	
	public void migrateReadUserAccessFromCSMToSpring(SecureClassesEnum dataType);
	
}
