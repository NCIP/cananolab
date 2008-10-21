package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
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

	public ProtocolFileBean findProtocolFileById(String fileId)
			throws ProtocolException {
		ProtocolFileBean protocolFileBean = null;
		try {
			ProtocolFile pf = helper.findProtocolFileById(fileId);
			protocolFileBean = new ProtocolFileBean(pf);
			return protocolFileBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol file by id: " + fileId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 * 
	 * @param protocolFile
	 * @throws Exception
	 */
	public void saveProtocolFile(ProtocolFile protocolFile, byte[] fileData)
			throws ProtocolException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(protocolFile);

			Protocol dbProtocol = findProtocolBy(protocolFile.getProtocol()
					.getType(), protocolFile.getProtocol().getName());
			if (dbProtocol != null) {
				protocolFile.setProtocol(dbProtocol);
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocolFile);

			// save to the file system fileData is not empty
			fileService.writeFile(protocolFile, fileData);

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

	public List<ProtocolFileBean> findProtocolFilesBy(String protocolType,
			String protocolName, String fileTitle) throws ProtocolException {
		List<ProtocolFileBean> protocolFileBeans = new ArrayList<ProtocolFileBean>();
		try {
			List<ProtocolFile> protocolFiles = helper.findProtocolFilesBy(
					protocolType, protocolName, fileTitle);

			for (ProtocolFile pf : protocolFiles) {
				ProtocolFileBean pfb = new ProtocolFileBean(pf);
				protocolFileBeans.add(pfb);
			}
			return protocolFileBeans;
		} catch (Exception e) {
			String err = "Problem finding protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocolFiles() throws ProtocolException {
		try {
			int count = helper.getNumberOfPublicProtocolFiles();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);

		}
	}
}
