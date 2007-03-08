package gov.nih.nci.calab.service.remote;

import gov.nih.nci.calab.db.HibernateDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

/**
 * This class provides implementations to the interface RemoteQueryFacade
 * through querying public data configured in CSM.
 * 
 * @author pansu
 * 
 */
public class RemoteQueryFacadeImpl implements RemoteQueryFacade {
	private static Logger logger = Logger
			.getLogger(RemoteQueryFacadeImpl.class);

	public RemoteQueryFacadeImpl() {
	}

	public boolean isPublicData(String data) throws Exception {
		Collection<String> publicDataCollection = getPublicProtectionGroupCollection();
		if (publicDataCollection.contains(data)) {
			return true;
		} else {
			return false;
		}
	}

	private Collection<String> getPublicProtectionGroupCollection() throws Exception {
		Collection<String> publicDataCollection=new ArrayList<String>();
		HibernateDataAccess hda = HibernateDataAccess.getInstance();

		String query = "select distinct a.PROTECTION_GROUP_NAME from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d	"
				+ "where a.PROTECTION_GROUP_ID=c.PROTECTION_GROUP_ID and b.ROLE_ID=c.ROLE_ID and c.GROUP_ID=d.GROUP_ID "
				+ " and b.ROLE_NAME='"
				+ CaNanoLabConstants.CSM_READ_ROLE
				+ "' and d.GROUP_NAME='"
				+ CaNanoLabConstants.CSM_PUBLIC_GROUP
				+ "'";

		try {
			hda.open();
			SQLQuery queryObj = hda.getNativeQuery(query);
			queryObj.addScalar("PROTECTION_GROUP_NAME", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				publicDataCollection.add(obj.toString());
			}
		} catch (Exception e) {
			logger.error("error getting public data from CSM database:" + e);
			throw new Exception("error getting public data from CSM database:"
					+ e);
		} finally {
			hda.close();
		}
		return publicDataCollection;
	}

	public Nanoparticle[] getPublicNanoparticles(List<Nanoparticle> particles)
			throws Exception {
		List<Nanoparticle> publicParticles = new ArrayList<Nanoparticle>();
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		for (Nanoparticle particle : particles) {
			if (publicDataCollection.contains(particle.getName().toString())) {
				publicParticles.add(particle);
			}
		}
		Nanoparticle[] publicParticleArray = new Nanoparticle[publicParticles
				.size()];
		return publicParticles.toArray(publicParticleArray);
	}

	public Report[] getPublicReports(List<Report> reports) throws Exception {
		List<Report> publicReports = new ArrayList<Report>();
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		for (Report report : reports) {
			if (publicDataCollection.contains(report.getId().toString())) {
				publicReports.add(report);
			}
		}
		Report[] publicReportArray = new Report[publicReports.size()];
		return publicReports.toArray(publicReportArray);
	}

	public AssociatedFile[] getPublicAssociatedFiles(
			List<AssociatedFile> associatedFiles) throws Exception {
		List<AssociatedFile> publicAssociatedFiles = new ArrayList<AssociatedFile>();
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		for (AssociatedFile associatedFile : associatedFiles) {
			if (publicDataCollection
					.contains(associatedFile.getId().toString())) {
				publicAssociatedFiles.add(associatedFile);
			}
		}
		AssociatedFile[] publicAssociatedFileArray = new AssociatedFile[publicAssociatedFiles
				.size()];
		return publicAssociatedFiles.toArray(publicAssociatedFileArray);
	}

	public OutputFile[] getPublicOutputFiles(List<OutputFile> files)
			throws Exception {
		List<OutputFile> publicFiles = new ArrayList<OutputFile>();
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		for (OutputFile file : files) {
			if (publicDataCollection.contains(file.getId().toString())) {
				publicFiles.add(file);
			}
		}
		OutputFile[] publicFileArray = new OutputFile[publicFiles.size()];
		return publicFiles.toArray(publicFileArray);
	}

	/**
	 * Filter out non-public data. Currently only Nanoparticle, Report,
	 * AssociatedFile and OutputFile have been set visibility in caNanoLab thus
	 * only they can return filtered results through CQL. Everthing else is
	 * treated non-public unless queried from either Nanoparticle, Report,
	 * AssociatedFile or OutputFile.
	 * 
	 */
	public List getPublicData(List dataObjects) throws Exception {
		List processed = new ArrayList();
		Object firstObj = dataObjects.get(0);
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		if (firstObj instanceof Nanoparticle) {
			for (Object obj : dataObjects) {
				if (publicDataCollection.contains(((Nanoparticle) obj)
						.getName())) {
					processed.add(obj);
				}
			}
		} else if (firstObj instanceof Report
				|| firstObj instanceof AssociatedFile
				|| firstObj instanceof OutputFile) {
			for (Object obj : dataObjects) {
				if (publicDataCollection.contains(((LabFile) obj).getId()
						.toString())) {
					processed.add(obj);
				}
			}
		}
		return processed;
	}

	public byte[] retrievePublicFileContent(Long fileId) throws Exception {
		byte[] fileData = null;
		Collection<String> publicDataCollection=getPublicProtectionGroupCollection();
		if (publicDataCollection.contains(fileId.toString())) {
			FileService fileService = new FileService();
			fileData = fileService.getFileContent(fileId);
		}
		return fileData;
	}
}
