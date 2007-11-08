package gov.nih.nci.calab.service.protocol;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * This class includes methods invovled in setting up protocol related forms and
 * searching protocols.
 * 
 * @author pansu
 * 
 */
public class SearchProtocolService {
	private static Logger logger = Logger.getLogger(SearchReportService.class);

	private UserService userService;

	public SearchProtocolService() throws Exception {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public ProtocolFileBean getProtocolFileBean(String fileId) throws Exception {
		ProtocolFileBean pfb = new ProtocolFileBean();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "select protocolFile from ProtocolFile protocolFile left join fetch "
					+ "protocolFile.protocol where protocolFile.id='"
					+ fileId
					+ "'";

			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				pfb = new ProtocolFileBean(pf);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding protocol info.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(pfb
				.getId(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		pfb.setVisibilityGroups(visibilityGroups);
		return pfb;
	}

	// used for Ajax
	public List<ProtocolFileBean> getProtocolFileBeans(String protocolName,
			String protocolType) throws Exception {
		if (protocolName == null || protocolName.length() == 0
				|| protocolType == null || protocolType.length() == 0) {
			return null;
		}
		List<ProtocolFileBean> files = new ArrayList<ProtocolFileBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "select protocolFile from ProtocolFile protocolFile join "
					+ "protocolFile.protocol protocol ";
			String where = "";
			if (protocolType != null && protocolType.length() > 0) {
				where += "where protocol.name='" + protocolName
						+ "' and protocol.type='" + protocolType + "'";
			} else if (protocolName != null && protocolName.length() > 0) {
				where += "where protocol.name='" + protocolName + "'";
			} else if (protocolType != null && protocolType.length() > 0) {
				where += "where protocol.type='" + protocolType + "'";
			}

			List results = session.createQuery(hqlString + where).list();

			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				files.add(new ProtocolFileBean(pf));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error(
							"Problem finding protocol files base on protocol name and type.",
							e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return files;
	}

	// used for Ajax
	public List<ProtocolBean> getProtocolBeans(String protocolType)
			throws Exception {
		if (protocolType == null || protocolType.length() == 0) {
			return null;
		}
		List<ProtocolBean> protocols = new ArrayList<ProtocolBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "from Protocol where type='" + protocolType
					+ "'";
			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				Protocol protocol = (Protocol) obj;
				protocols.add(new ProtocolBean(protocol));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding protocols base on protocol type.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return protocols;
	}

	public List<ProtocolFileBean> getProtocolFileBeans(String protocolType)
			throws Exception {

		List<ProtocolFileBean> files = new ArrayList<ProtocolFileBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "select protocolFile from ProtocolFile protocolFile join "
					+ "protocolFile.protocol protocol where protocol.type='"
					+ protocolType + "'";

			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				files.add(new ProtocolFileBean(pf));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(
					"Problem finding protocol files base on protocol type.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return files;
	}

	public List<ProtocolFileBean> searchProtocols(String fileTitle,
			String protocolType, String protocolName, UserBean user)
			throws Exception {
		List<ProtocolFileBean> protocols = new ArrayList<ProtocolFileBean>();
		List<LabFileBean> protocolFiles = new ArrayList<LabFileBean>();

		try {
			HibernateUtil.beginTransaction();
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();
			String where = "";

			if (fileTitle.length() > 0) {
				where = "where ";
				if (fileTitle.indexOf("*") != -1) {
					fileTitle = fileTitle.replace('*', '%');
					whereList.add("protocolFile.title like ?");
				} else {
					whereList.add("protocolFile.title=? ");
				}
				paramList.add(fileTitle.toUpperCase());
			}
			if (protocolType.length() > 0) {
				paramList.add(protocolType);
				where = "where ";
				whereList.add("p.type=? ");
			}
			if (protocolName.length() > 0) {
				where = "where ";
				if (protocolName.indexOf("*") != -1) {
					protocolName = protocolName.replace('*', '%');
					whereList.add("p.name like ?");
				} else {
					whereList.add("p.name=? ");
				}
				paramList.add(protocolName);
			}
			String whereStr = StringUtils.join(whereList, " and ");

			String hqlString = "select protocolFile from ProtocolFile protocolFile join fetch "
					+ "protocolFile.protocol p  ";

			hqlString = hqlString
					+ where
					+ whereStr
					+ " order by protocolFile.protocol.name, protocolFile.version ";
			List results = HibernateUtil.createQueryByParam(hqlString,
					paramList).list();

			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				LabFileBean pfb = new ProtocolFileBean(pf);
				protocolFiles.add(pfb);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding protocol info.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		if (protocolFiles.isEmpty())
			return protocols;

		List<LabFileBean> filteredProtocols = this.userService.getFilteredFiles(
				user, protocolFiles);
		if (!filteredProtocols.isEmpty()) {
			for (LabFileBean fb : filteredProtocols) {
				protocols.add((ProtocolFileBean) fb);
			}
		}
		// return returnProtocols;
		return protocols;
	}

	public SortedSet<String> getAllProtocolTypes() throws Exception {
		SortedSet<String> protocolTypes = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct protocol.type from Protocol protocol where protocol.type is not null";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				protocolTypes.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all protocol types.", e);
			throw new RuntimeException(
					"Problem to retrieve all protocol types.");
		} finally {
			HibernateUtil.closeSession();
		}
		return protocolTypes;
	}
}
