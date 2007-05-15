package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.common.LabFileBean;
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
 * This class includes service calls involved in creating reports.
 * 
 * @author pansu
 * 
 */
public class SubmitReportService {
	private static Logger logger = Logger.getLogger(SubmitReportService.class);

	private UserService userService;

	public SubmitReportService() throws Exception {
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public void createReport(String[] particleNames, FormFile uploadedFile,
			LabFileBean fileBean) throws Exception {

		// TODO saves reportFile to the file system
		String rootPath = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
			rootPath = rootPath.substring(0, rootPath.length() - 1);

		FileService fileService = new FileService();
		String fileName = fileService.writeUploadedFile(uploadedFile, rootPath
				+ File.separator + CaNanoLabConstants.FOLDER_REPORT, true);

		LabFile dataFile = null;
		if (fileBean.getType().equalsIgnoreCase(CaNanoLabConstants.REPORT))
			dataFile = new Report();
		else
			dataFile = new AssociatedFile();

		dataFile.setDescription(fileBean.getDescription());
		dataFile.setFilename(uploadedFile.getFileName());

		dataFile.setPath(File.separator + CaNanoLabConstants.FOLDER_REPORT
				+ File.separator + fileName);
		dataFile.setTitle(fileBean.getTitle().toUpperCase()); // convert to
		// upper case
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setComments(fileBean.getComments());

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
		userService.setVisiblity(dataFile.getId().toString(), fileBean
				.getVisibilityGroups());
	}
}
