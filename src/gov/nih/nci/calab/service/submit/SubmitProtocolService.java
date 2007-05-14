package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.common.FileService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * 
 * @author pansu
 * 
 */

/*
 * CVS $Id: SubmitProtocolService.java,v 1.1 2007-05-14 14:16:05 chenhang Exp $
 */

public class SubmitProtocolService {
	private static Logger logger = Logger.getLogger(SubmitProtocolService.class);

	/**
	 * Create a brand new protocol based on user input 
	 * 
	 * @param fileBean, the ProtocolFileBean
	 * @param uploadedFile, the FormFile
	 * @throws Exception
	 */
	public void createProtocol(ProtocolFileBean fileBean, FormFile uploadedFile,
		boolean isNew) throws Exception {
		
		String path = null;
		String tagFileName = null;
		// TODO saves protocol file to the file system
		if (uploadedFile != null){
			
			FileService fileService = new FileService();
			
			String rootPath = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
			if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
				rootPath = rootPath.substring(0, rootPath.length() - 1);
			path = File.separator + "protocols" + File.separator;
			fileService.writeUploadedFile(uploadedFile, rootPath + path, true);
/*
			File pathDir = new File(rootPath + path);
			if (!pathDir.exists())
				pathDir.mkdirs();

			HttpFileUploadSessionData sData = new HttpFileUploadSessionData();
			tagFileName = sData.getTimeStamp() + "_"
				+ uploadedFile.getFileName();
			String outputFilename = rootPath + path + tagFileName;

			FileOutputStream oStream = new FileOutputStream(
				new File(outputFilename));

			this.writeFile(uploadedFile.getInputStream(), oStream);
			*/
		}
		ProtocolFile dataFile = new ProtocolFile();

		dataFile.setDescription(fileBean.getDescription());
		if (uploadedFile != null){
			dataFile.setFilename(uploadedFile.getFileName());
			dataFile.setPath(path + tagFileName);
		}
		dataFile.setTitle(fileBean.getTitle().toUpperCase()); // convert to
		// upper case
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setVersion(fileBean.getVersion());
		//dataFile.setComments(fileBean.getComments());

		Protocol protocol = new Protocol();
		protocol.setName(fileBean.getProtocolBean().getName());
		protocol.setType(fileBean.getProtocolBean().getType());
		// TODO daves reportFile path to the database
		// look up the samples for each particleNames
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();
			if (isNew){
				ida.store(protocol);
			}
			else {
				List results = ida.search("from Protocol where name='" + protocol.getName() + "'");
				Protocol pl = null;
				for (Object obj : results) {
					pl = (Protocol) obj;
				}
				protocol.setId(pl.getId());
			}
			dataFile.setProtocol(protocol);
			//protocol.getProtocolFileCollection().add(dataFile);
			//ida.store(protocol);
			ida.store(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving protocol data: ");
			throw e;
		} finally {
			ida.close();
		}
/*
		Nanoparticle particle = null;

		for (String particleName : particleNames) {
			try {
				ida.open();

				List results = ida
						.search("select particle from Nanoparticle particle left join fetch particle.reportCollection where particle.name='"
								+ particleName + "'");

				for (Object obj : results) {
					particle = (Nanoparticle) obj;
				}

				if (particle != null) {
					if (fileBean.getType().equalsIgnoreCase(
							CaNanoLabConstants.REPORT))
						particle.getReportCollection().add((Report) dataFile);
					else
						particle.getAssociatedFileCollection().add(
								(AssociatedFile) dataFile);
				}

			} catch (Exception e) {
				e.printStackTrace();
				ida.rollback();
				logger.error("Problem saving report File: ");
				throw e;
			} finally {
				ida.close();
			}
		}
		*/
		setVisiblity(protocol.getId().toString(), fileBean.getVisibilityGroups());
		
	}
	
	private void writeFile(InputStream is, FileOutputStream os) {
		byte[] bytes = new byte[32768];

		try {
			int numRead = 0;
			while ((numRead = is.read(bytes)) > 0) {
				os.write(bytes, 0, numRead);
			}
			os.close();

		} catch (Exception e) {

		}
	}
	
	private void setVisiblity(String dataToProtect, String[] visibilities)
		throws Exception {
		// remove existing visibilities for the data
		UserService userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
		userService.removeAllAccessibleGroups(dataToProtect,
		CaNanoLabConstants.CSM_READ_ROLE, null);

		// set new visibilities
		for (String visibility : visibilities) {
			userService.secureObject(dataToProtect, visibility,
			CaNanoLabConstants.CSM_READ_ROLE);
		}

		// set default visibilities
		for (String visibility : CaNanoLabConstants.VISIBLE_GROUPS) {
			userService.secureObject(dataToProtect, visibility,
			CaNanoLabConstants.CSM_READ_ROLE);
		}
	}
}
