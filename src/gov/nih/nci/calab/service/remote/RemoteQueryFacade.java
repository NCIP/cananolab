package gov.nih.nci.calab.service.remote;

import gov.nih.nci.calab.exception.CaNanoLabSecurityException;

import java.util.List;

/**
 * This interface defines methods available to http clients.
 * 
 * @author pansu
 * 
 */
public interface RemoteQueryFacade {

	public boolean isPublicId(String dataId) throws CaNanoLabSecurityException;

	public List<String> getPublicDataIds(String[] dataIds)
			throws CaNanoLabSecurityException;

	public byte[] retrievePublicFileContent(Long fileId)
			throws CaNanoLabSecurityException;
}