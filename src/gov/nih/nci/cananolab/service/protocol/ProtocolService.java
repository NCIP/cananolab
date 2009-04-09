package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ProtocolException;

import java.util.List;

/**
 * This interface defines methods involved in creating and searching protocols
 *
 * @author pansu
 *
 */
public interface ProtocolService {

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException;

	/**
	 * Persist a new protocol or update an existing protocol
	 *
	 * @param protocolFile
	 * @throws Exception
	 */
	public void saveProtocol(Protocol protocol, byte[] fileData)
			throws ProtocolException;

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws ProtocolException;

	public Protocol findProtocolBy(String protocolType, String protocolName,
			String protocolVersion) throws ProtocolException;

	public int getNumberOfPublicProtocols() throws ProtocolException;

	public void retrieveVisibility(ProtocolBean protocolBean, UserBean user)
			throws ProtocolException;
}
