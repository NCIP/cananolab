package gov.nih.nci.cananolab.datamigration.dao;

import java.util.AbstractMap;
import java.util.List;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

public interface MigrateDataDAO
{
	public List<CananoUserDetails> getUsersFromCSM();
	
	public List<String> getCuratorUsersFromCSM();
	
	public Long getDataSize(SecureClassesEnum dataType);
	
	public Long getPublicDataSize(SecureClassesEnum dataType);
	
	public List<AbstractMap.SimpleEntry<Long, String>> getDataPage(long rowMin, long rowMax, SecureClassesEnum dataType);
	
	public List<AbstractMap.SimpleEntry<Long, String>> getPublicDataPage(long rowMin, long rowMax, SecureClassesEnum dataType);
	
	public List<AbstractMap.SimpleEntry<Long, String>> getRWDAccessDataForUsers(SecureClassesEnum dataType);
	
	public List<AbstractMap.SimpleEntry<Long, String>> getReadAccessDataForUsers(SecureClassesEnum dataType);
	
	public Long getCharDataSize();
	
	public List<AbstractMap.SimpleEntry<Long, Long>> getAllCharacterizations(long rowMin, long rowMax);
	
	public List<Long> getAllOrganizations();
	
	public List<AbstractMap.SimpleEntry<Long, Long>> getPOCsForOrgs();

}
