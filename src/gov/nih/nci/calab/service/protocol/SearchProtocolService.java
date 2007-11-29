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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.collection.PersistentSet;

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
	public List<ProtocolFileBean> getProtocolFiles(String protocolName,
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
	public SortedSet<String> getProtocolNames(String protocolType)
			throws Exception {
		if (protocolType == null || protocolType.length() == 0) {
			return null;
		}
		SortedSet<String> protocolNames = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "select name from Protocol where type='"
					+ protocolType + "'";
			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				String protocolName = (String) obj;
				protocolNames.add(protocolName);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding protocols base on protocol type.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return protocolNames;
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

		List<LabFileBean> filteredProtocols = this.userService
				.getFilteredFiles(user, protocolFiles);
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

	public Map<ProtocolBean, List<ProtocolFileBean>> getAllProtocolNameVersionByType(
			String type) throws Exception {
		Map<ProtocolBean, List<ProtocolFileBean>> nameVersions = new HashMap<ProtocolBean, List<ProtocolFileBean>>();
		Map<Protocol, ProtocolBean> keyMap = new HashMap<Protocol, ProtocolBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select protocolFile, protocolFile.protocol from ProtocolFile protocolFile"
					+ " where protocolFile.protocol.type = '" + type + "'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] array = (Object[]) obj;
				Object key = null;
				Object value = null;
				for (int i = 0; i < array.length; i++) {
					if (array[i] instanceof Protocol) {
						key = array[i];
					} else if (array[i] instanceof ProtocolFile) {
						value = array[i];
					}
				}

				if (keyMap.containsKey(key)) {
					ProtocolBean pb = keyMap.get(key);
					List<ProtocolFileBean> localList = nameVersions.get(pb);
					ProtocolFileBean fb = new ProtocolFileBean();
					fb.setVersion(((ProtocolFile) value).getVersion());
					fb.setId(((ProtocolFile) value).getId().toString());
					localList.add(fb);
				} else {
					List<ProtocolFileBean> localList = new ArrayList<ProtocolFileBean>();
					ProtocolFileBean fb = new ProtocolFileBean();
					fb.setVersion(((ProtocolFile) value).getVersion());
					fb.setId(((ProtocolFile) value).getId().toString());
					localList.add(fb);
					ProtocolBean protocolBean = new ProtocolBean();
					Protocol protocol = (Protocol) key;
					protocolBean.setId(protocol.getId().toString());
					protocolBean.setName(protocol.getName());
					protocolBean.setType(protocol.getType());
					nameVersions.put(protocolBean, localList);
					keyMap.put((Protocol) key, protocolBean);
				}
			}

		} catch (Exception e) {
			logger.error(
					"Problem to retrieve all protocol names and their versions by type "
							+ type, e);
			throw new RuntimeException(
					"Problem to retrieve all protocol names and their versions by type "
							+ type);
		} finally {
			HibernateUtil.closeSession();
		}
		return nameVersions;
	}

	public SortedSet<ProtocolBean> getAllProtocols(UserBean user)
			throws Exception {
		SortedSet<ProtocolBean> protocolBeans = new TreeSet<ProtocolBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from Protocol as protocol left join fetch protocol.protocolFileCollection";

			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Protocol p = (Protocol) obj;
				ProtocolBean pb = new ProtocolBean();
				pb.setId(p.getId().toString());
				pb.setName(p.getName());
				pb.setType(p.getType());
				PersistentSet set = (PersistentSet) p
						.getProtocolFileCollection();
				// HashSet hashSet = set.
				if (!set.isEmpty()) {
					List<ProtocolFileBean> list = new ArrayList<ProtocolFileBean>();
					for (Iterator it = set.iterator(); it.hasNext();) {
						ProtocolFile pf = (ProtocolFile) it.next();
						ProtocolFileBean pfb = new ProtocolFileBean();
						pfb.setId(pf.getId().toString());
						pfb.setVersion(pf.getVersion());
						list.add(pfb);
					}
					pb.setFileBeanList(filterProtocols(list, user));
				}
				if (!pb.getFileBeanList().isEmpty())
					protocolBeans.add(pb);
			}

		} catch (Exception e) {
			logger
					.error("Problem to retrieve all protocol names and types.",
							e);
			throw new RuntimeException(
					"Problem to retrieve all protocol names and types.");
		} finally {
			HibernateUtil.closeSession();
		}
		return protocolBeans;
	}

	private List<ProtocolFileBean> filterProtocols(
			List<ProtocolFileBean> protocolFiles, UserBean user)
			throws Exception {
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<LabFileBean> tempList = new ArrayList<LabFileBean>();
		for (ProtocolFileBean pfb : protocolFiles) {
			tempList.add(pfb);
		}
		List<LabFileBean> filteredProtocols = userService.getFilteredFiles(
				user, tempList);
		protocolFiles.clear();

		if (filteredProtocols == null || filteredProtocols.isEmpty())
			return protocolFiles;
		for (LabFileBean lfb : filteredProtocols) {
			protocolFiles.add((ProtocolFileBean) lfb);
		}
		return protocolFiles;
	}
}
