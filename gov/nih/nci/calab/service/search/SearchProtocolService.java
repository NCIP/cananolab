package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * This class includes methods invovled in searching reports.
 * 
 * @author pansu
 * 
 */
public class SearchProtocolService {
	private static Logger logger = Logger.getLogger(SearchReportService.class);

	private UserService userService;

	public SearchProtocolService() throws Exception {
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);

	}

	public ProtocolFileBean getProtocolFileBean(String fileBeanId)
			throws Exception {
		ProtocolFileBean fileBean = null;
		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select protocolFile.title, protocolFile.description, protocolFile.filename, "
									+ "protocolFile.version from ProtocolFile protocolFile "
									+ "where protocolFile.id='" + fileBeanId
									+ "'").list();

			for (Object obj : results) {
				String title = (String) (((Object[]) obj)[0]);
				String description = (String) (((Object[]) obj)[1]);
				String fileName = (String) (((Object[]) obj)[2]);
				String version = (String) (((Object[]) obj)[3]);

				fileBean = new ProtocolFileBean();
				fileBean.setId(fileBeanId);
				fileBean.setDescription(description);
				fileBean.setTitle(title);
				fileBean.setName(fileName);
				fileBean.setVersion(version);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(
					"Problem finding protocol file info for protocol file Id: "
							+ fileBeanId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		// get visibilities
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		fileBean.setVisibilityGroups(visibilityGroups);
		return fileBean;
	}

	public ProtocolFileBean getWholeProtocolFileBean(String fileId)
			throws Exception {

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
				pfb.setId(pf.getId().toString());
				pfb.setVersion(pf.getVersion());
				pfb.setTitle(pf.getTitle());
				pfb.setName(pf.getFilename());
				pfb.setDescription(pf.getDescription());

				ProtocolBean pb = new ProtocolBean();
				Protocol p = pf.getProtocol();
				pb.setId(p.getId().toString());
				pb.setName(p.getName());
				pb.setType(p.getType());
				pfb.setProtocolBean(pb);
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
				LabFileBean pfb = new ProtocolFileBean();
				pfb.setId(pf.getId().toString());
				pfb.setVersion(pf.getVersion());
				pfb.setTitle(pf.getTitle());
				pfb.setDescription(pf.getDescription());

				ProtocolBean pb = new ProtocolBean();
				Protocol p = pf.getProtocol();
				pb.setId(p.getId().toString());
				pb.setName(p.getName());
				pb.setType(p.getType());
				((ProtocolFileBean) pfb).setProtocolBean(pb);
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

		List<LabFileBean> filteredProtocols = userService.getFilteredFiles(
				user, protocolFiles);
		if (!filteredProtocols.isEmpty()) {
			for (LabFileBean fb : filteredProtocols) {
				protocols.add((ProtocolFileBean) fb);
			}
		}
		// return returnProtocols;
		return protocols;
	}
}
