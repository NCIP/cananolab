package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

/**
 * This interface defines methods involved in creating and searching protocols
 *
 * @author pansu
 *
 */
public interface ProtocolService extends BaseService {

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException, NoAccessException;

	/**
	 * Persist a new protocol or update an existing protocol
	 *
	 * @param protocol
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean)
			throws ProtocolException, NoAccessException;

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTite)
			throws ProtocolException;

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion)
			throws ProtocolException, NoAccessException;

	public int getNumberOfPublicProtocols() throws ProtocolException;

	public void deleteProtocol(Protocol protocol) throws ProtocolException,
			NoAccessException;

	public void assignAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException;

	public void removeAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException;
}
