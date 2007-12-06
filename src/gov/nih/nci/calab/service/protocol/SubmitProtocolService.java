package gov.nih.nci.calab.service.protocol;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.ProtocolType;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ProtocolException;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.hibernate.Session;

/**
 * 
 * @author chenhang
 * 
 */

/*
 * CVS $Id: SubmitProtocolService.java,v 1.6 2007-12-06 09:01:44 pansu Exp $
 */

public class SubmitProtocolService {
	private static Logger logger = Logger
			.getLogger(SubmitProtocolService.class);

	private UserService userService;

	public SubmitProtocolService() throws ProtocolException,
			CaNanoLabSecurityException {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}
	
	/**
	 * Create a brand new protocol based on user input
	 * 
	 * @param fileBean
	 * @param uploadedFile
	 * @throws ProtocolException
	 * @throws CaNanoLabSecurityException
	 */
	public void createProtocol(ProtocolFileBean fileBean, FormFile uploadedFile)
			throws ProtocolException, CaNanoLabSecurityException {

		String fileName = null;
		if (uploadedFile != null
				&& (uploadedFile.getFileName() == null || uploadedFile
						.getFileName().length() == 0)) {
			uploadedFile = null;
		}
		if (uploadedFile != null) {
			try {
				FileService fileService = new FileService();

				String rootPath = PropertyReader.getProperty(
						CaNanoLabConstants.FILEUPLOAD_PROPERTY,
						"fileRepositoryDir");
				if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
					rootPath = rootPath.substring(0, rootPath.length() - 1);

				fileName = fileService.writeUploadedFile(uploadedFile, rootPath
						+ File.separator + CaNanoLabConstants.FOLDER_PROTOCOL,
						true);
			} catch (Exception e) {
				logger.error("Error writing uploaded file to the file system.",
						e);
				throw new ProtocolException();
			}
		}
		ProtocolFile dataFile = new ProtocolFile();

		dataFile.setDescription(fileBean.getDescription());
		if (uploadedFile != null) {
			dataFile.setFilename(uploadedFile.getFileName());
			dataFile.setUri(File.separator + CaNanoLabConstants.FOLDER_PROTOCOL
					+ File.separator + fileName);
		}
		dataFile.setTitle(fileBean.getTitle().toUpperCase()); // convert to
		dataFile.setCreatedDate(fileBean.getCreatedDate());
		dataFile.setCreatedBy(fileBean.getCreatedBy());

		// If the id is
		Long fileId = null;
		Long protocolId = null;
		// It might be new or old
		if (isLong(fileBean.getId())) {
			fileId = new Long(fileBean.getId());
			dataFile.setId(fileId);
		}
		// It's a new version
		else {
			dataFile.setVersion(fileBean.getId());
		}
		Protocol protocol = new Protocol();
		// It can be a new name (numeric name) or an old id.
		if (isLong(fileBean.getProtocolBean().getId())) {
			protocolId = new Long(fileBean.getProtocolBean().getId());
			protocol.setId(protocolId);
		}
		// It's a new name
		else {
			protocol.setName(fileBean.getProtocolBean().getId());
		}
		// protocol.setName(fileBean.getProtocolBean().getName());
		protocol.setType(fileBean.getProtocolBean().getType());

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			if (protocol.getId() != null) {
				List results = session.createQuery(
						"from Protocol where id='" + protocol.getId() + "'")
						.list();
				Protocol pl = null;
				for (Object obj : results) {
					pl = (Protocol) obj;
				}
				if (pl == null) {
					protocol.setName(protocol.getId().toString());
					protocol.setId(null);
					session.saveOrUpdate(protocol);
				}
			} else {
				session.save(protocol);
			}
			// Check datafile
			if (dataFile.getId() != null) {
				List results = session
						.createQuery(
								"from ProtocolFile where id='"
										+ dataFile.getId() + "'").list();
				ProtocolFile pf = null;
				for (Object obj : results) {
					pf = (ProtocolFile) obj;
				}
				if (pf == null) {
					dataFile.setVersion(dataFile.getId().toString());
					dataFile.setId(null);
					dataFile.setProtocol(protocol);
					session.saveOrUpdate(dataFile);
				} else {
					LabFile file = (LabFile) session.load(LabFile.class,
							dataFile.getId());

					file.setTitle(dataFile.getTitle().toUpperCase());
					file.setDescription(dataFile.getDescription());
					if (dataFile.getVersion() != null
							&& dataFile.getVersion().length() > 0)
						file.setVersion(dataFile.getVersion());
					if (dataFile.getFilename() != null
							&& dataFile.getFilename().length() > 0)
						file.setFilename(dataFile.getFilename());
					if (dataFile.getUri() != null
							&& dataFile.getUri().length() > 0)
						file.setUri(dataFile.getUri());
				}
			} else {
				dataFile.setProtocol(protocol);
				session.save(dataFile);
			}

			// add protocol type to database
			ProtocolType protocolType = new ProtocolType();
			LookupService.addLookupType(session, protocolType, protocol
					.getType());
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem saving protocol data: ", e);
			throw new ProtocolException();
		} finally {
			HibernateUtil.closeSession();
		}

		this.userService.setVisiblity(dataFile.getId().toString(), fileBean
				.getVisibilityGroups());

	}

	public void updateProtocol(ProtocolFileBean fileBean, FormFile uploadedFile)
			throws ProtocolException, CaNanoLabSecurityException {

		String fileName = null;
		if (uploadedFile != null
				&& (uploadedFile.getFileName() == null || uploadedFile
						.getFileName().length() == 0)) {
			uploadedFile = null;
		}
		if (uploadedFile != null) {
			try {
				FileService fileService = new FileService();

				String rootPath = PropertyReader.getProperty(
						CaNanoLabConstants.FILEUPLOAD_PROPERTY,
						"fileRepositoryDir");
				if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
					rootPath = rootPath.substring(0, rootPath.length() - 1);

				fileName = fileService.writeUploadedFile(uploadedFile, rootPath
						+ File.separator + CaNanoLabConstants.FOLDER_PROTOCOL,
						true);
			} catch (Exception e) {
				logger.error("Error saving uploaded file the file system.", e);
				throw new ProtocolException();
			}
		}

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			LabFile file = (LabFile) session.load(LabFile.class, Long
					.parseLong(fileBean.getId()));
			file.setTitle(fileBean.getTitle().toUpperCase());
			file.setDescription(fileBean.getDescription());
			if (fileName != null && fileName.length() > 0) {
				file.setFilename(fileName);
				file.setUri(File.separator + CaNanoLabConstants.FOLDER_PROTOCOL
						+ File.separator + fileName);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem saving protocol data: ", e);
			throw new ProtocolException();
		} finally {
			HibernateUtil.closeSession();
		}

		this.userService.setVisiblity(fileBean.getId().toString(), fileBean
				.getVisibilityGroups());

	}

	private boolean isLong(String value) {
		boolean isLong = true;
		try {
			double d = Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			isLong = false;
		}
		return isLong;
	}
}
