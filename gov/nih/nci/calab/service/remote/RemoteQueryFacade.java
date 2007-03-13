package gov.nih.nci.calab.service.remote;

import java.util.List;

/**
 * This interface defines methods available to http clients.
 * 
 * @author pansu
 * 
 */
public interface RemoteQueryFacade {

	public boolean isPublicId(String dataId) throws Exception;

	public List<String> getPublicDataIds(String[] dataIds) throws Exception;

	public byte[] retrievePublicFileContent(Long fileId) throws Exception;
}