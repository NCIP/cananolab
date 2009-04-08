package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Local implementation of ProtocolService
 *
 * @author pansu
 *
 */
public class ProtocolServiceLocalImpl implements ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceLocalImpl.class);
	private ProtocolServiceHelper helper = new ProtocolServiceHelper();

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException {
		ProtocolBean protocolBean = null;
		try {
			Protocol pf = helper.findProtocolById(protocolId);
			protocolBean = new ProtocolBean(pf);
			return protocolBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol by id: " + protocolId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 *
	 * @param protocol
	 * @throws Exception
	 */
	public void saveProtocol(Protocol protocol, byte[] fileData)
			throws ProtocolException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(protocol.getFile());

			Protocol dbProtocol = findProtocolBy(protocol.getType(), protocol
					.getName());
			if (dbProtocol != null) {
				protocol = dbProtocol;
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocol);

			// save to the file system fileData is not empty
			fileService.writeFile(protocol.getFile(), fileData);

		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	// also used for Ajax
	public SortedSet<String> getProtocolNames(String protocolType)
			throws ProtocolException {
		try {
			return helper.getProtocolNames(protocolType);
		} catch (Exception e) {
			String err = "Problem finding protocols base on protocol type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public Protocol findProtocolBy(String protocolType, String protocolName)
			throws ProtocolException {
		try {
			return helper.findProtocolBy(protocolType, protocolName);
		} catch (Exception e) {
			String err = "Problem finding protocol by name and type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String fileTitle) throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			List<Protocol> protocols = helper.findProtocolsBy(protocolType,
					protocolName, fileTitle);

			for (Protocol pf : protocols) {
				ProtocolBean pfb = new ProtocolBean(pf);
				protocolBeans.add(pfb);
			}
			return protocolBeans;
		} catch (Exception e) {
			String err = "Problem finding protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocols() throws ProtocolException {
		try {
			int count = helper.getNumberOfPublicProtocols();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);

		}
	}
}
