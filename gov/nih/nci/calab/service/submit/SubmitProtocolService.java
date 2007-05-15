package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * 
 * @author chenhang 
 * 
 */

/*
 * CVS $Id: SubmitProtocolService.java,v 1.3 2007-05-15 15:23:49 pansu Exp $
 */

public class SubmitProtocolService {
	private static Logger logger = Logger
			.getLogger(SubmitProtocolService.class);
	private UserService userService;
	
	public SubmitProtocolService() throws Exception {
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	/**
	 * Create a brand new protocol based on user input
	 * 
	 * @param fileBean,
	 *            the ProtocolFileBean
	 * @param uploadedFile,
	 *            the FormFile
	 * @throws Exception
	 */
	public void createProtocol(ProtocolFileBean fileBean,
			FormFile uploadedFile, boolean isNew) throws Exception {

		// TODO saves protocol file to the file system
		String fileName = null;
		if (uploadedFile != null) {

			FileService fileService = new FileService();

			String rootPath = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
				rootPath = rootPath.substring(0, rootPath.length() - 1);

			fileName = fileService
					.writeUploadedFile(uploadedFile, rootPath + File.separator
							+ CaNanoLabConstants.FOLDER_PROTOCOL, true);
		}
		ProtocolFile dataFile = new ProtocolFile();

		dataFile.setDescription(fileBean.getDescription());
		if (uploadedFile != null) {
			dataFile.setFilename(uploadedFile.getFileName());
			dataFile.setPath(CaNanoLabConstants.FOLDER_PROTOCOL
					+ File.separator + fileName);
		}
		dataFile.setTitle(fileBean.getTitle().toUpperCase()); // convert to
		// upper case
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setVersion(fileBean.getVersion());
		// dataFile.setComments(fileBean.getComments());

		Protocol protocol = new Protocol();
		protocol.setName(fileBean.getProtocolBean().getName());
		protocol.setType(fileBean.getProtocolBean().getType());
		// TODO daves reportFile path to the database
		// look up the samples for each particleNames
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();
			if (isNew) {
				ida.store(protocol);
			} else {
				List results = ida.search("from Protocol where name='"
						+ protocol.getName() + "'");
				Protocol pl = null;
				for (Object obj : results) {
					pl = (Protocol) obj;
				}
				protocol.setId(pl.getId());
			}
			dataFile.setProtocol(protocol);
			// protocol.getProtocolFileCollection().add(dataFile);
			// ida.store(protocol);
			ida.store(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving protocol data: ");
			throw e;
		} finally {
			ida.close();
		}

		userService.setVisiblity(protocol.getId().toString(), fileBean
				.getVisibilityGroups());

	}
}
