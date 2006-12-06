package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * This class include services to submit the report to the file system and saves
 * the path to the database and sets user visibility for the report
 * 
 * @author pansu
 * 
 */
public class SubmitReportService {
	private static Logger logger = Logger.getLogger(SubmitReportService.class);

	public void submit(String[] particleNames, String reportType,
			FormFile report, String title, String description, String comment,
			String[] visibilities) throws Exception {

		// TODO saves reportFile to the file system
		String rootPath = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
			rootPath = rootPath.substring(0, rootPath.length() - 1);

		String path = File.separator + "reports" + File.separator;

		File pathDir = new File(rootPath + path);
		if (!pathDir.exists())
			pathDir.mkdirs();

		HttpFileUploadSessionData sData = new HttpFileUploadSessionData();
		String tagFileName = sData.getTimeStamp() + "_" + report.getFileName();
		String outputFilename = rootPath + path + tagFileName;

		FileOutputStream oStream = new FileOutputStream(
				new File(outputFilename));

		this.saveFile(report.getInputStream(), oStream);

		LabFile dataFile = null;
		if (reportType.equalsIgnoreCase(CananoConstants.NCL_REPORT))
			dataFile = new Report();
		else
			dataFile = new AssociatedFile();

		dataFile.setDescription(description);
		dataFile.setFilename(report.getFileName());

		dataFile.setPath(path + tagFileName);
		dataFile.setTitle(title);
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setComments(comment);

		// TODO daves reportFile path to the database
		// look up the samples for each particleNames
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();

			ida.store(dataFile);

		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving report File: ");
			throw e;
		} finally {
			ida.close();
		}

		Nanoparticle particle = null;
		SearchSampleService service = new SearchSampleService();

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
					if (reportType.equalsIgnoreCase(CananoConstants.NCL_REPORT))
						particle.getReportCollection().add(dataFile);
					else
						particle.getAssociatedFileCollection().add(dataFile);
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

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
//		String fileName = report.getFileName();

		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			// TODO once the files is successfully saved, use fileId instead of
			// fileName
			for (String defaultGroup : CananoConstants.DEFAULT_VISIBLE_GROUPS) {
				userService.secureObject(dataFile.getId().toString(), defaultGroup,
						CalabConstants.CSM_READ_ROLE);
			}
			userService.secureObject(dataFile.getId().toString(), visibility,
					CalabConstants.CSM_READ_ROLE);
		}
	}

	public void saveFile(InputStream is, FileOutputStream os) {
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

}
