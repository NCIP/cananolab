package gov.nih.nci.cananolab.service.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * This class includes methods invovled in creating and searching protocols
 * 
 * @author pansu
 * 
 */
public class ProtocolService {
	private static Logger logger = Logger.getLogger(ProtocolService.class);

	public ProtocolFileBean findProtocolFileById(String fileId)
			throws ProtocolException {
		ProtocolFileBean protocolFileBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ProtocolFile.class).add(
					Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ProtocolFile pf = (ProtocolFile) result.get(0);
				protocolFileBean = new ProtocolFileBean(pf);
			}
			return protocolFileBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol file by id: " + fileId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	// for ajax on linux
	public String findProtocolFileUriById(String fileId)
			throws ProtocolException {
		String uri = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ProtocolFile.class).add(
					Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ProtocolFile pf = (ProtocolFile) result.get(0);
				uri = pf.getUri();
			}
			return uri;
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
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (protocolFile.getId() != null) {
				try {
					ProtocolFile dbProtocolFile = (ProtocolFile) appService
							.get(ProtocolFile.class, protocolFile.getId());
					// don't change createdBy and createdDate it is already
					// persisted
					protocolFile.setCreatedBy(dbProtocolFile.getCreatedBy());
					protocolFile
							.setCreatedDate(dbProtocolFile.getCreatedDate());
					// load fileName and uri if no new data has been uploaded or
					// no new url has been entered
					if (fileData == null) {
						protocolFile.setName(dbProtocolFile.getName());
					}
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ReportException(err, e);
				}
			}

			appService.saveOrUpdate(protocolFile);

			// save to the file system fileData is not empty
			FileService fileService = new FileService();
			fileService.writeFile(protocolFile, fileData);

		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	// used for Ajax
	public SortedSet<String> findProtocolNames(String protocolType)
			throws ProtocolException {
		if (protocolType == null || protocolType.length() == 0) {
			return null;
		}
		SortedSet<String> protocolNames = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
					.add(Property.forName("type").eq(protocolType));
			List results = appService.query(crit);
			for (Object obj : results) {
				Protocol protocol = (Protocol) obj;
				protocolNames.add(protocol.getName());
			}
			return protocolNames;
		} catch (Exception e) {
			String err = "Problem finding protocols base on protocol type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolFileBean> findProtocolFilesBy(String protocolType,
			String fileTitle, String protocolName) throws ProtocolException {
		List<ProtocolFileBean> protocolFiles = new ArrayList<ProtocolFileBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(ProtocolFile.class);
			if (protocolType != null && protocolType.length() > 0
					|| protocolName != null && protocolName.length() > 0) {
				crit.createAlias("protocol", "protocol",
						CriteriaSpecification.LEFT_JOIN);
				if (protocolType != null && protocolType.length() > 0) {
					crit.add(Restrictions.eq("protocol.type", protocolType));
				}
				if (protocolName != null && protocolName.length() > 0) {
					crit.add(Restrictions.ilike("protocol.name", protocolName,
							MatchMode.ANYWHERE));
				}
			}
			if (fileTitle != null && fileTitle.length() > 0) {
				crit.add(Restrictions.ilike("title", fileTitle,
						MatchMode.ANYWHERE));
			}
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				ProtocolFileBean pfb = new ProtocolFileBean(pf);
				protocolFiles.add(pfb);
			}
			return protocolFiles;
		} catch (Exception e) {
			String err = "Problem finding protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}
}
