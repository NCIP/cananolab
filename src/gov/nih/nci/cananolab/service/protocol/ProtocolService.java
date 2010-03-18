package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;

import java.util.List;

/**
 * This interface defines methods involved in creating and searching protocols
 *
 * @author pansu
 *
 */
public interface ProtocolService {

	public ProtocolBean findProtocolById(String protocolId, UserBean user)
			throws ProtocolException, NoAccessException;

	/**
	 * Persist a new protocol or update an existing protocol
	 *
	 * @param protocol
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean, UserBean user)
			throws ProtocolException, NoAccessException;

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle,
			UserBean user) throws ProtocolException;

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion, UserBean user)
			throws ProtocolException, NoAccessException;

	public int getNumberOfPublicProtocols() throws ProtocolException;

	public List<String> deleteProtocol(Protocol protocol, UserBean user,
			Boolean removeVisibility) throws ProtocolException,
			NoAccessException;
}
