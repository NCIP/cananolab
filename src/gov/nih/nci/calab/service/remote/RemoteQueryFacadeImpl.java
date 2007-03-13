package gov.nih.nci.calab.service.remote;

import gov.nih.nci.calab.db.HibernateDataAccess;
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

	public boolean isPublicId(String dataId) throws Exception {
		Collection<String> publicDataCollection = getPublicProtectionGroupCollection();
		if (publicDataCollection.contains(dataId)) {
			return true;
		} else {
			return false;
		}
	}

	private Collection<String> getPublicProtectionGroupCollection()
			throws Exception {
		Collection<String> publicDataCollection = new ArrayList<String>();
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

	/**
	 * Filter out non-public data. Currently only Nanoparticle, Report,
	 * AssociatedFile and OutputFile have been set visibility in caNanoLab thus
	 * only they can return filtered results through CQL. Everthing else is
	 * treated non-public unless queried from either Nanoparticle, Report,
	 * AssociatedFile or OutputFile.
	 * 
	 */
	public List<String> getPublicDataIds(String[] dataIds) throws Exception {
		List<String> publicIds = new ArrayList<String>();
		Collection<String> publicDataCollection = getPublicProtectionGroupCollection();
		for (String id : dataIds) {
			if (publicDataCollection.contains(id)) {
				publicIds.add(id);
			}
		}
		return publicIds;
	}

	public byte[] retrievePublicFileContent(Long fileId) throws Exception {
		byte[] fileData = null;
		Collection<String> publicDataCollection = getPublicProtectionGroupCollection();
		if (publicDataCollection.contains(fileId.toString())) {
			FileService fileService = new FileService();
			fileData = fileService.getFileContent(fileId);
		}
		return fileData;
	}
}
