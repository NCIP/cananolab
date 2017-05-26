/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
	
	public int getNumberOfPublicProtocolsForJob() throws ProtocolException;

	public void deleteProtocol(Protocol protocol) throws ProtocolException,
			NoAccessException;

	public void assignAccessibility(AccessControlInfo access, Protocol protocol)
			throws ProtocolException, NoAccessException;

	public void removeAccessibility(AccessControlInfo access, Protocol protocol)
			throws ProtocolException, NoAccessException;

	public List<String> findProtocolIdsByOwner(String currentOwner) throws ProtocolException;
	
	public List<String> findProtocolIdsSharedWithUser(CananoUserDetails userDetails) throws ProtocolException;

	public ProtocolBean findWorkspaceProtocolById(String protocolId)
			throws ProtocolException, NoAccessException;
	
	public ProtocolServiceHelper getHelper();
	
	public List<ProtocolBean> getProtocolsByChar(HttpServletRequest request,
			String characterizationType) throws Exception;
}
