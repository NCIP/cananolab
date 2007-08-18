package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.HibernateUtil;
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
import org.hibernate.Session;

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

		dataFile.setUri(File.separator + CaNanoLabConstants.FOLDER_REPORT
				+ File.separator + fileName);
		dataFile.setTitle(fileBean.getTitle().toUpperCase()); // convert to
		// upper case
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setComments(fileBean.getComments());

		// TODO daves reportFile uri to the database
		// look up the samples for each particleNames
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			session.save(dataFile);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem saving report File: ", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		Nanoparticle particle = null;

		for (String particleName : particleNames) {
			try {
				Session session = HibernateUtil.currentSession();
				HibernateUtil.beginTransaction();

				List results = session
						.createQuery(
								"select particle from Nanoparticle particle left join fetch particle.reportCollection where particle.name='"
										+ particleName + "'").list();

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
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				HibernateUtil.rollbackTransaction();
				logger.error("Problem saving report File: ", e);
				throw e;
			} finally {
				HibernateUtil.closeSession();
			}
		}
		userService.setVisiblity(dataFile.getId().toString(), fileBean
				.getVisibilityGroups());
	}
}
