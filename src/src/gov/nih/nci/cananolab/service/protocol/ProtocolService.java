package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.exception.ProtocolException;

import java.util.List;
import java.util.SortedSet;

/**
 * This interface defines methods involved in creating and searching protocols
 * 
 * @author pansu
 * 
 */
public interface ProtocolService {

	public ProtocolFileBean findProtocolFileById(String fileId)
			throws ProtocolException;

	/**
	 * Persist a new protocol file or update an existing protocol file
	 * 
	 * @param protocolFile
	 * @throws Exception
	 */
	public void saveProtocolFile(ProtocolFile protocolFile, byte[] fileData)
			throws ProtocolException;

	public List<ProtocolFileBean> findProtocolFilesBy(String protocolType,
			String protocolName, String fileTitle) throws ProtocolException;

	public SortedSet<String> getProtocolNames(String protocolType)
			throws ProtocolException;

	public int getNumberOfPublicProtocolFiles() throws ProtocolException;
}
