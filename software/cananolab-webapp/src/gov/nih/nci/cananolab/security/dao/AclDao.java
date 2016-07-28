package gov.nih.nci.cananolab.security.dao;

import java.util.List;

public interface AclDao
{
	public List<Long> getIdsOfClassForSid(String clazz, String sid);
	
	public List<Long> getPocOfPublicSamples(String clazz, String sid);
	
	public List<Long> getCountOfPublicCharacterization(String clazz, String sid, List<String> charNames);

}
